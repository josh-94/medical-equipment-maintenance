# Arquitectura del Sistema — Medical Equipment Maintenance API

## ¿Qué es Clean Architecture?

Este proyecto implementa **Clean Architecture** (Arquitectura Limpia), también conocida como Arquitectura Hexagonal o Ports & Adapters.

La regla fundamental es:

> **Las capas internas no conocen a las externas.** El Dominio no sabe que existe Spring, PostgreSQL ni HTTP.

Las dependencias solo fluyen hacia adentro:

```
┌──────────────────────────────────────────┐
│  INFRASTRUCTURE                          │
│  (Spring Boot, PostgreSQL, REST)         │
│  ┌────────────────────────────────────┐  │
│  │  APPLICATION                       │  │
│  │  (Casos de uso, Puertos)           │  │
│  │  ┌──────────────────────────────┐  │  │
│  │  │  DOMAIN                      │  │  │
│  │  │  (Reglas de negocio puras)   │  │  │
│  │  └──────────────────────────────┘  │  │
│  └────────────────────────────────────┘  │
└──────────────────────────────────────────┘
```

---

## Capa DOMAIN — El corazón del sistema

**Ubicación:** `src/main/java/com/tecsup/medicalequipment/domain/`

Es la capa más importante. Contiene las **reglas de negocio** escritas en Java puro, sin ningún framework.

### Qué contiene

| Carpeta | Qué hace |
|---|---|
| `model/` | Entidades: `Equipment`, `Technician`, `MaintenanceSchedule` |
| `model/valueobjects/` | Valores con significado: `Email`, `EquipmentId`, `SerialNumber` |
| `repository/` | Contratos (interfaces), sin implementación |
| `service/` | Reglas de negocio complejas que involucran varias entidades |
| `exception/` | Excepciones propias del negocio |

### Ejemplo: regla de negocio en `MaintenanceSchedule.java`

```java
// No se puede programar un mantenimiento en fecha pasada
if (scheduledDate.isBefore(LocalDateTime.now())) {
    throw new InvalidMaintenanceScheduleDateException(
        "No se puede programar un mantenimiento en una fecha pasada: " + scheduledDate
    );
}
```

Esta validación ocurre dentro del dominio, sin que importe si la petición viene de HTTP, una cola de mensajes o un test unitario.

### Ejemplo: interfaz de repositorio en `EquipmentRepository.java`

```java
// El dominio define el CONTRATO, sin saber cómo se implementa
public interface EquipmentRepository {
    Optional<Equipment> findById(EquipmentId id);
    List<Equipment> findAll();
    void save(Equipment equipment);
}
```

El dominio sabe *qué* necesita, pero no *cómo* se obtiene. La implementación real (JPA + PostgreSQL) vive en Infrastructure.

### Ejemplo: regla de conflicto de horarios en `MaintenanceScheduleService.java`

```java
// REGLA: Un técnico no puede tener dos cronogramas al mismo tiempo
public void validateScheduleForTechnician(MaintenanceSchedule schedule) {
    if (maintenanceScheduleRepository.existsForTechnicianAtDateTime(
            schedule.getTechnicianId(), schedule.getScheduledDate())) {
        throw new ConflictingScheduleException(
            "El técnico ya tiene un cronograma en " + schedule.getScheduledDate()
        );
    }
}
```

---

## Capa APPLICATION — Los casos de uso

**Ubicación:** `src/main/java/com/tecsup/medicalequipment/application/`

Orquesta las entidades del dominio para cumplir un objetivo concreto del sistema. No conoce Spring, JPA ni HTTP, solo usa interfaces.

### Qué contiene

| Carpeta | Qué hace |
|---|---|
| `port/in/` | Define qué puede hacer el sistema (`ScheduleMaintenanceUseCase`) |
| `port/out/` | Define qué necesita el sistema del exterior (`LoadEquipmentPort`, `SaveMaintenanceSchedulePort`) |
| `service/` | Implementación de los casos de uso |
| `dto/` | Objetos de transferencia entre capas (`ScheduleMaintenanceCommand`, `ScheduleMaintenanceResponse`) |

### Los Puertos (Ports)

Los **puertos** son interfaces que desacoplan la lógica de la infraestructura:

**Puerto de entrada** — lo que el mundo exterior puede pedirle al sistema:
```java
// application/port/in/ScheduleMaintenanceUseCase.java
public interface ScheduleMaintenanceUseCase {
    ScheduleMaintenanceResponse execute(ScheduleMaintenanceCommand command);
}
```

**Puerto de salida** — lo que el sistema necesita del mundo exterior:
```java
// application/port/out/LoadEquipmentPort.java
public interface LoadEquipmentPort {
    Optional<Equipment> loadEquipmentById(String equipmentId);
}
```

### Ejemplo: flujo del caso de uso `ScheduleMaintenanceApplicationService`

```java
@Override
public ScheduleMaintenanceResponse execute(ScheduleMaintenanceCommand command) {

    // 1. Cargar el equipo (usa puerto, no sabe que hay JPA detrás)
    Equipment equipment = loadEquipmentPort.loadEquipmentById(command.getEquipmentId())
        .orElseThrow(() -> new DomainException("No existe equipo con ID: " + command.getEquipmentId()));

    // 2. Cargar el técnico
    Technician technician = loadTechnicianPort.loadTechnicianById(command.getTechnicianId())
        .orElseThrow(() -> new DomainException("No existe técnico con ID: " + command.getTechnicianId()));

    // 3. Crear el cronograma (aquí el DOMINIO valida la fecha)
    MaintenanceSchedule schedule = MaintenanceSchedule.create(
        equipment.getId().getValue(),
        command.getScheduledDate(),
        maintenanceType,
        technician.getId()
    );

    // 4. Validar conflictos de horario (servicio de dominio)
    maintenanceScheduleService.validateScheduleForTechnician(schedule);

    // 5. Persistir (usa puerto, no sabe que hay PostgreSQL detrás)
    saveMaintenanceSchedulePort.saveSchedule(schedule);

    return ScheduleMaintenanceResponse.success(schedule.getId(), equipment.getId().getValue());
}
```

---

## Capa INFRASTRUCTURE — El mundo exterior

**Ubicación:** `src/main/java/com/tecsup/medicalequipment/infrastructure/`

Implementa los contratos definidos por el dominio y la aplicación. Aquí viven Spring Boot, JPA y PostgreSQL.

### Qué contiene

| Carpeta | Qué hace |
|---|---|
| `adapter/in/web/` | Controllers REST (reciben peticiones HTTP) |
| `adapter/out/persistence/entity/` | Entidades JPA (representación en BD) |
| `adapter/out/persistence/repository/` | Implementaciones de los repositorios y puertos |
| `adapter/out/persistence/mapper/` | Conversores entre objetos de dominio y JPA |

### Ejemplo: Controller en `ScheduleMaintenanceController.java`

```java
@RestController
@RequestMapping("/api/v1/maintenance-schedules")
public class ScheduleMaintenanceController {

    private final ScheduleMaintenanceUseCase scheduleMaintenanceUseCase;

    @PostMapping
    public ResponseEntity<ScheduleMaintenanceApiResponse> scheduleNewMaintenance(
            @RequestBody ScheduleMaintenanceRequest request) {

        // Convierte HTTP request → comando de aplicación
        ScheduleMaintenanceCommand command = ScheduleMaintenanceCommand.create(
            request.getEquipmentId(),
            request.getScheduledDate(),
            request.getMaintenanceType(),
            request.getTechnicianId()
        );

        // Delega al caso de uso (no contiene lógica de negocio)
        ScheduleMaintenanceResponse response = scheduleMaintenanceUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ScheduleMaintenanceApiResponse.success(
                response.getScheduleId(), response.getEquipmentId()
            ));
    }
}
```

### Ejemplo: Mapper en `EquipmentMapper.java`

```java
// Convierte entidad JPA → objeto de dominio
public static Equipment toDomain(EquipmentJpaEntity jpa) {
    return Equipment.reconstruct(
        EquipmentId.create(jpa.getId()),
        jpa.getName(),
        jpa.getBrand(),
        jpa.getModel(),
        SerialNumber.create(jpa.getSerialNumber()),
        jpa.getWarrantyEndDate(),
        mapStatus(jpa.getStatus()),
        jpa.getInstallationDate()
    );
}
```

El mapper es el "traductor" entre lo que guarda la base de datos y lo que entiende el dominio.

---

## Flujo completo de una petición

```
Thunder Client
    │  POST /api/v1/maintenance-schedules
    ▼
ScheduleMaintenanceController        ← INFRASTRUCTURE
    │  Convierte Request → Command
    ▼
ScheduleMaintenanceApplicationService ← APPLICATION
    │  loadEquipmentPort.loadEquipmentById("eq-001")
    │  loadTechnicianPort.loadTechnicianById("tech-001")
    ▼
MaintenanceSchedule.create(...)      ← DOMAIN (valida reglas de negocio)
    ▼
MaintenanceScheduleService
    .validateScheduleForTechnician() ← DOMAIN (valida conflictos)
    ▼
SaveMaintenanceSchedulePortAdapter   ← INFRASTRUCTURE
    │  JPA → PostgreSQL
    ▼
HTTP 201 Created → Thunder Client
```

---

## Demo con Thunder Client

Abre Thunder Client en VS Code (`Ctrl+Shift+P` → "Thunder Client") y prueba los siguientes casos:

### 1. Health Check
```
GET http://localhost:8080/api/v1/health
```
**Respuesta esperada (200 OK):**
```json
{
  "status": "UP",
  "application": "Medical Equipment Maintenance API",
  "version": "1.0.0"
}
```

---

### 2. Ver todos los equipos
```
GET http://localhost:8080/api/v1/equipment
```
**Respuesta esperada (200 OK):**
```json
[
  {
    "id": "eq-001",
    "name": "Desfibrilador DEF-5000",
    "brand": "Philips",
    "model": "DEF-5000",
    "serialNumber": "SN-001-2024",
    "status": "OPERATIONAL",
    "warrantyEndDate": "2027-12-31",
    "installationDate": "2024-01-15"
  }
]
```

---

### 3. Ver un equipo específico
```
GET http://localhost:8080/api/v1/equipment/eq-001
```

---

### 4. Ver los mantenimientos programados de un equipo
```
GET http://localhost:8080/api/v1/equipment/eq-001/schedules
```
**Respuesta esperada (200 OK):**
```json
{
  "equipment": {
    "id": "eq-001",
    "name": "Desfibrilador DEF-5000",
    "brand": "Philips",
    "status": "OPERATIONAL"
  },
  "totalSchedules": 1,
  "schedules": [
    {
      "id": "sch-001",
      "scheduledDate": "2026-05-01T10:00:00",
      "type": "PREVENTIVE",
      "technicianId": "tech-001",
      "completed": false
    }
  ]
}
```

---

### 5. Ver mantenimientos pendientes
```
GET http://localhost:8080/api/v1/maintenance-schedules/pending
```

---

### 6. Programar un nuevo mantenimiento ✅ (caso exitoso)
```
POST http://localhost:8080/api/v1/maintenance-schedules
Content-Type: application/json

{
  "equipmentId": "eq-002",
  "technicianId": "tech-002",
  "maintenanceType": "INSPECTION",
  "scheduledDate": "2026-07-10T09:00:00"
}
```
**Respuesta esperada (201 Created):**
```json
{
  "scheduleId": "550e8400-e29b-41d4-a716-446655440000",
  "equipmentId": "eq-002",
  "status": "SUCCESS",
  "message": "Mantenimiento programado exitosamente"
}
```

---

### 7. Demostrar regla de negocio — fecha pasada ❌
```
POST http://localhost:8080/api/v1/maintenance-schedules
Content-Type: application/json

{
  "equipmentId": "eq-001",
  "technicianId": "tech-001",
  "maintenanceType": "PREVENTIVE",
  "scheduledDate": "2024-01-01T10:00:00"
}
```
**Respuesta esperada (400 Bad Request):**
```json
{
  "error": "DOMAIN_ERROR",
  "message": "No se puede programar un mantenimiento en una fecha pasada: 2024-01-01T10:00"
}
```
> Esta validación la hace el **Dominio**, no el Controller ni la BD.

---

### 8. Demostrar regla de negocio — equipo inexistente ❌
```
POST http://localhost:8080/api/v1/maintenance-schedules
Content-Type: application/json

{
  "equipmentId": "eq-999",
  "technicianId": "tech-001",
  "maintenanceType": "CORRECTIVE",
  "scheduledDate": "2026-08-15T10:00:00"
}
```
**Respuesta esperada (400 Bad Request):**
```json
{
  "error": "DOMAIN_ERROR",
  "message": "No existe equipo con ID: eq-999"
}
```

---

## IDs disponibles para pruebas

| Equipos | Nombre |
|---|---|
| `eq-001` | Desfibrilador DEF-5000 (Philips) |
| `eq-002` | Monitor Cardíaco MC-3000 (GE Healthcare) |
| `eq-003` | Bomba de Infusión BI-7000 (Baxter) |

| Técnicos | Nombre |
|---|---|
| `tech-001` | Carlos García López |
| `tech-002` | María Rodríguez Pérez |
| `tech-003` | Juan Martínez Silva |

| Tipos de mantenimiento ||
|---|---|
| `PREVENTIVE` | Mantenimiento preventivo |
| `CORRECTIVE` | Mantenimiento correctivo |
| `INSPECTION` | Inspección |
