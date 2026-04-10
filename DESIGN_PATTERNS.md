# Patrones de Diseño Implementados

Este documento describe los patrones de diseño utilizados en la arquitectura Clean Architecture del sistema.

## 1. 🏗️ Arquitectura en Capas (Layered Architecture)

La aplicación se divide en tres capas independientes:

```
┌─────────────────────────┐
│   INFRASTRUCTURE        │  (Frameworks, BD, Web)
├─────────────────────────┤
│   APPLICATION           │  (Casos de Uso)
├─────────────────────────┤
│   DOMAIN                │  (Lógica de Negocio)
└─────────────────────────┘
```

### Características:
- **Independencia**: Cada capa es independiente de las capas superiores
- **Dependencias**: Solo fluyen hacia adentro (hacia el dominio)
- **Testabilidad**: Las capas inferiores no dependen de las superiores

## 2. 🔌 Patrón Hexagonal (Ports & Adapters)

Utilizado para desacoplar la lógica de negocio de la infraestructura.

### Puertos de Entrada (In)
```java
// application/port/in/ScheduleMaintenanceUseCase.java
public interface ScheduleMaintenanceUseCase {
    ScheduleMaintenanceResponse execute(ScheduleMaintenanceCommand command);
}
```

**Adapters:**
- `infrastructure/adapter/in/web/ScheduleMaintenanceController` (HTTP REST)

### Puertos de Salida (Out)
```java
// application/port/out/LoadEquipmentPort.java
public interface LoadEquipmentPort {
    Optional<Equipment> loadEquipmentById(String equipmentId);
}
```

**Adapters:**
- `infrastructure/adapter/out/persistence/repository/LoadEquipmentPortAdapter` (BD)

### Ventajas:
- ✅ Fácil cambiar de BD (MySQL → PostgreSQL)
- ✅ Fácil cambiar de tecnología web (REST → gRPC)
- ✅ Testing sin dependencias externas

## 3. 📦 Value Objects

Objetos que representan conceptos del dominio con identidad basada en valor.

```java
// domain/model/valueobjects/Email.java
public class Email {
    private final String value;
    
    public static Email create(String email) {
        // Validaciones
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new DomainException("Email inválido");
        }
        return new Email(email);
    }
    
    @Override
    public boolean equals(Object o) {
        // Igualdad por valor, no por referencia
        return Objects.equals(value, ((Email)o).value);
    }
}
```

### Características:
- Encapsulación de validaciones
- Inmutables
- Igualdad por valor
- No tienen ID

## 4. 🏛️ Entidades del Dominio

Objetos con identidad única que encapsulan lógica de negocio.

```java
// domain/model/Equipment.java
public class Equipment {
    private final EquipmentId id;  // Identidad única
    private MaintenanceStatus status;  // Estado mutable
    
    // Factory method para crear nuevas instancias
    public static Equipment create(String name, String brand, ...) {
        validateEquipment(...);
        return new Equipment(EquipmentId.generate(), ...);
    }
    
    // Comportamiento del dominio
    public boolean isUnderWarranty() {
        return LocalDate.now().isBefore(warrantyEndDate);
    }
    
    public void changeStatus(MaintenanceStatus newStatus) {
        if (newStatus == null) {
            throw new DomainException("Estado no válido");
        }
        this.status = newStatus;
    }
}
```

### Características:
- Factory methods para creación
- Método `reconstruct()` para reconstruir desde BD
- Comportamiento encapsulado
- Validaciones en constructor

## 5. 🔒 Patrón Repository

Abstrae el acceso a datos sin exponer detalles de persistencia.

```java
// domain/repository/EquipmentRepository.java (Interfaz)
public interface EquipmentRepository {
    void save(Equipment equipment);
    Optional<Equipment> findById(EquipmentId id);
    List<Equipment> findAll();
}

// infrastructure/adapter/out/persistence/repository/EquipmentRepositoryAdapter.java
@Component
public class EquipmentRepositoryAdapter implements EquipmentRepository {
    private final EquipmentJpaRepositoryInterface jpaRepository;
    
    @Override
    public void save(Equipment equipment) {
        var jpaEntity = EquipmentMapper.toJpaEntity(equipment);
        jpaRepository.save(jpaEntity);
    }
}
```

### Ventajas:
- El dominio no conoce de JPA
- Fácil cambiar de BD
- Testing con double objects

## 6. 🗺️ Patrón Mapper

Convierte entre capas sin contaminación.

```java
// infrastructure/adapter/out/persistence/mapper/EquipmentMapper.java
public class EquipmentMapper {
    
    // De JPA a Dominio
    public static Equipment toDomain(EquipmentJpaEntity jpaEntity) {
        return Equipment.reconstruct(
            EquipmentId.create(jpaEntity.getId()),
            jpaEntity.getName(),
            ...
        );
    }
    
    // De Dominio a JPA
    public static EquipmentJpaEntity toJpaEntity(Equipment equipment) {
        return new EquipmentJpaEntity(
            equipment.getId().getValue(),
            equipment.getName(),
            ...
        );
    }
}
```

### Características:
- Separación clara entre capas
- Sin contaminación de imports de JPA en dominio
- Conversion de Value Objects

## 7. 📋 Patrón Command

Encapsula solicitudes como objetos.

```java
// application/dto/ScheduleMaintenanceCommand.java
public class ScheduleMaintenanceCommand {
    private final String equipmentId;
    private final LocalDateTime scheduledDate;
    private final String maintenanceType;
    
    public static ScheduleMaintenanceCommand create(
        String equipmentId, 
        LocalDateTime scheduledDate,
        String maintenanceType,
        String technicianId) {
        
        // Validaciones básicas
        if (equipmentId == null) throw new IllegalArgumentException(...);
        
        return new ScheduleMaintenanceCommand(...);
    }
}
```

### Ventajas:
- Validación de entrada
- Encapsulación de parámetros
- Fácil de loguear y auditar

## 8. 🎯 Use Case / Application Service

Orquesta la lógica de negocio usando entidades del dominio.

```java
// application/service/ScheduleMaintenanceApplicationService.java
public class ScheduleMaintenanceApplicationService 
    implements ScheduleMaintenanceUseCase {
    
    private final LoadEquipmentPort loadEquipmentPort;
    private final MaintenanceScheduleService maintenanceScheduleService;
    
    public ScheduleMaintenanceResponse execute(ScheduleMaintenanceCommand command) {
        // 1. Cargar recursos necesarios
        Equipment equipment = loadEquipmentPort
            .loadEquipmentById(command.getEquipmentId())
            .orElseThrow(() -> new DomainException("Equipo no encontrado"));
        
        // 2. Crear entidad de dominio
        MaintenanceSchedule schedule = MaintenanceSchedule.create(
            equipment.getId().getValue(),
            command.getScheduledDate(),
            ...
        );
        
        // 3. Aplicar reglas de negocio del servicio de dominio
        maintenanceScheduleService.validateScheduleForTechnician(schedule);
        
        // 4. Persistir
        saveMaintenanceSchedulePort.saveSchedule(schedule);
        
        // 5. Retornar respuesta
        return ScheduleMaintenanceResponse.success(schedule.getId(), ...);
    }
}
```

### Responsabilidades:
- Orquestar llamadas a puertos y servicios de dominio
- Sin lógica de negocio (eso va en el dominio)
- Traducción entre Command y entidades de dominio

## 9. 🍃 Domain Service

Implementa lógica de negocio que afecta múltiples entidades.

```java
// domain/service/MaintenanceScheduleService.java
public class MaintenanceScheduleService {
    
    public void validateScheduleForTechnician(MaintenanceSchedule schedule) {
        String technicianId = schedule.getTechnicianId();
        LocalDateTime scheduledDate = schedule.getScheduledDate();
        
        // Regla: No puede haber dos visitas del mismo técnico en el mismo tiempo
        if (maintenanceScheduleRepository
            .existsForTechnicianAtDateTime(technicianId, scheduledDate)) {
            throw new ConflictingScheduleException("Conflicto de horarios");
        }
    }
}
```

### Diferencia con Model:
- **Entity** = Lógica que afecta a UNA entidad
- **Domain Service** = Lógica que afecta a MÚLTIPLES entidades

## 10. 🚫 Global Exception Handler

Centraliza el manejo de excepciones HTTP.

```java
// infrastructure/adapter/in/web/exception/GlobalExceptionHandler.java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(
        DomainException ex,
        HttpServletRequest request) {
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("DOMAIN_ERROR", ex.getMessage()));
    }
}
```

### Ventajas:
- Manejo consistente
- No necesita try-catch en controllers
- Logging centralizado

## 11. 📊 Data Transfer Objects (DTOs)

Separan la representación HTTP del dominio.

```
HTTP Request → ScheduleMaintenanceRequest (Web DTO)
    ↓
ScheduleMaintenanceCommand (Application DTO)
    ↓
MaintenanceSchedule (Domain Entity)
    ↓
ScheduleMaintenanceResponse (Application DTO)
    ↓
HTTP Response → ScheduleMaintenanceApiResponse (Web DTO)
```

### Ventajas:
- No exponer estructua del dominio
- Validaciones específicas de entrada
- Formatos de respuesta consistentes

## 12. 🔄 Patrón Adapter

Implementa interfaces de puertos para adaptadores específicos.

```
LoadEquipmentPort (Interface)
    ↓
LoadEquipmentPortAdapter (Adapter)
    ↓
EquipmentRepositoryAdapter (Adapter)
    ↓
EquipmentJpaRepositoryInterface (JPA)
```

## Flujo de una Solicitud

```
1. HTTP POST /api/v1/maintenance-schedules
↓
2. ScheduleMaintenanceController (Adapter In)
↓
3. Validar y convertir a ScheduleMaintenanceRequest
↓
4. Crear ScheduleMaintenanceCommand
↓
5. ScheduleMaintenanceApplicationService.execute()
↓
6. LoadEquipmentPortAdapter.loadEquipmentById()
↓
7. EquipmentRepositoryAdapter.findById()
↓
8. EquipmentJpaRepositoryInterface.findById()
↓
9. BD MySQL
↓
10. EquipmentMapper.toDomain() (JPA Entity → Domain Entity)
↓
11. MaintenanceScheduleService.validateScheduleForTechnician()
↓
12. MaintenanceSchedule.create() (Regla de negocio)
↓
13. SaveMaintenanceSchedulePortAdapter.saveSchedule()
↓
14. EquipmentRepositoryAdapter.save()
↓
15. EquipmentMapper.toJpaEntity() (Domain Entity → JPA Entity)
↓
16. BD MySQL
↓
17. ScheduleMaintenanceResponse
↓
18. ScheduleMaintenanceApiResponse
↓
19. HTTP 201 Created + JSON
```

## Conclusión

Estos patrones trabajan juntos para crear una arquitectura:

- ✅ **Mantenible**: Código limpio y bien organizado
- ✅ **Testeable**: Bajo acoplamiento
- ✅ **Escalable**: Fácil agregar nuevas funcionalidades
- ✅ **Flexible**: Cambios mínimos al migrar tecnologías
- ✅ **Independiente de Framework**: Dominio puro
