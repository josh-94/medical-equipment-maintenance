# Medical Equipment Maintenance API

API REST para el mantenimiento de equipos médicos construida con **Clean Architecture** en Spring Boot 3.

## 📋 Índice

- [Características](#características)
- [Arquitectura](#arquitectura)
- [Estructura de Paquetes](#estructura-de-paquetes)
- [Casos de Uso Implementados](#casos-de-uso-implementados)
- [Cómo Ejecutar](#cómo-ejecutar)
- [Endpoints](#endpoints)
- [Reglas de Negocio](#reglas-de-negocio)

## ✨ Características

- ✅ **Clean Architecture** - Separación clara de capas
- ✅ **Spring Boot 3.2** - Última versión estable
- ✅ **Spring Data JPA** - Persistencia con Hibernate
- ✅ **REST API** - Endpoints HTTP con validación
- ✅ **Domain-Driven Design** - Lógica de negocio en el dominio
- ✅ **Value Objects** - Encapsulación de conceptos
- ✅ **Mappers** - Conversión entre capas sin rotura de encapsulación
- ✅ **Manejo de Excepciones** - GlobalExceptionHandler para HTTP
- ✅ **MySQL** - Base de datos relacional

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│           INFRASTRUCTURE (Adapters)                     │
│  ┌───────────────────────────────────────────────────┐  │
│  │  in/web (Controllers REST)                        │  │
│  │  - ScheduleMaintenanceController                  │  │
│  │  - HealthController                               │  │
│  └───────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────┐  │
│  │  out/persistence (JPA Repositories & Mappers)    │  │
│  │  - EquipmentJpaEntity, EquipmentJpaRepository    │  │
│  │  - Equipmentmapper, EquipmentRepositoryAdapter  │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
           ↑               ↑               ↑
           │               │               │
┌─────────────────────────────────────────────────────────┐
│          APPLICATION (Use Cases)                        │
│  ┌───────────────────────────────────────────────────┐  │
│  │  port.in (Interfaces de Input)                    │  │
│  │  - ScheduleMaintenanceUseCase                     │  │
│  └───────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────┐  │
│  │  service (Implementación de Use Cases)            │  │
│  │  - ScheduleMaintenanceApplicationService          │  │
│  └───────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────┐  │
│  │  port.out (Interfaces de Output)                  │  │
│  │  - LoadEquipmentPort, LoadTechnicianPort         │  │
│  │  - SaveMaintenanceSchedulePort                   │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
           ↑               ↑               ↑
           │               │               │
┌─────────────────────────────────────────────────────────┐
│              DOMAIN (Lógica de Negocio)                 │
│  ┌───────────────────────────────────────────────────┐  │
│  │  model (Entidades y Value Objects)                │  │
│  │  - Equipment, Institution, Technician             │  │
│  │  - MaintenanceSchedule, MaintenanceVisit         │  │
│  │  - Email, EquipmentId, SerialNumber              │  │
│  └───────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────┐  │
│  │  repository (Interfaces sin implementación)       │  │
│  │  - EquipmentRepository, InstitutionRepository    │  │
│  │  - TechnicianRepository, etc.                    │  │
│  └───────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────┐  │
│  │  service (Reglas de Negocio Complejas)            │  │
│  │  - MaintenanceScheduleService                    │  │
│  │  - MaintenanceVisitService                       │  │
│  └───────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────┐  │
│  │  exception (Excepciones del Dominio)              │  │
│  │  - DomainException                                │  │
│  │  - InvalidMaintenanceScheduleDateException       │  │
│  │  - ConflictingScheduleException                  │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

## 📂 Estructura de Paquetes

```
src/main/java/com/tecsup/medicalequipment/
├── domain/
│   ├── model/
│   │   ├── valueobjects/
│   │   │   ├── Email.java
│   │   │   ├── EquipmentId.java
│   │   │   ├── SerialNumber.java
│   │   │   └── MaintenanceStatus.java
│   │   ├── Equipment.java
│   │   ├── Institution.java
│   │   ├── Technician.java
│   │   ├── MaintenanceSchedule.java
│   │   ├── MaintenanceVisit.java
│   │   ├── MaintenanceType.java
│   ├── repository/
│   │   ├── EquipmentRepository.java
│   │   ├── InstitutionRepository.java
│   │   ├── TechnicianRepository.java
│   │   ├── MaintenanceScheduleRepository.java
│   │   └── MaintenanceVisitRepository.java
│   ├── service/
│   │   ├── MaintenanceScheduleService.java
│   │   └── MaintenanceVisitService.java
│   └── exception/
│       ├── DomainException.java
│       ├── InvalidMaintenanceScheduleDateException.java
│       ├── InvalidMaintenanceVisitException.java
│       └── ConflictingScheduleException.java
├── application/
│   ├── port/
│   │   ├── in/
│   │   │   └── ScheduleMaintenanceUseCase.java
│   │   └── out/
│   │       ├── LoadEquipmentPort.java
│   │       ├── LoadTechnicianPort.java
│   │       └── SaveMaintenanceSchedulePort.java
│   ├── service/
│   │   └── ScheduleMaintenanceApplicationService.java
│   └── dto/
│       ├── ScheduleMaintenanceCommand.java
│       └── ScheduleMaintenanceResponse.java
└── infrastructure/
    ├── adapter/
    │   ├── in/
    │   │   └── web/
    │   │       ├── ScheduleMaintenanceController.java
    │   │       ├── HealthController.java
    │   │       ├── dto/
    │   │       │   ├── ScheduleMaintenanceRequest.java
    │   │       │   └── ScheduleMaintenanceApiResponse.java
    │   │       └── exception/
    │   │           ├── ErrorResponse.java
    │   │           └── GlobalExceptionHandler.java
    │   └── out/
    │       └── persistence/
    │           ├── entity/
    │           │   ├── EquipmentJpaEntity.java
    │           │   ├── InstitutionJpaEntity.java
    │           │   ├── TechnicianJpaEntity.java
    │           │   ├── MaintenanceScheduleJpaEntity.java
    │           │   └── MaintenanceVisitJpaEntity.java
    │           ├── mapper/
    │           │   ├── EquipmentMapper.java
    │           │   ├── InstitutionMapper.java
    │           │   ├── TechnicianMapper.java
    │           │   ├── MaintenanceScheduleMapper.java
    │           │   └── MaintenanceVisitMapper.java
    │           └── repository/
    │               ├── EquipmentJpaRepositoryInterface.java
    │               ├── EquipmentRepositoryAdapter.java
    │               ├── LoadEquipmentPortAdapter.java
    │               └── (otros adapters)
└── MedicalEquipmentMaintenanceApplication.java
```

## 📝 Casos de Uso Implementados

### 1. Programar Mantenimiento
- **Use Case** → `ScheduleMaintenanceUseCase`
- **Implementación** → `ScheduleMaintenanceApplicationService`
- **Endpoint** → `POST /api/v1/maintenance-schedules`
- **Validaciones**
  - El equipo debe existir
  - El técnico debe existir
  - La fecha no puede ser en el pasado
  - No puede haber conflicto de horarios

## 🚀 Cómo Ejecutar

### Requisitos
- Java 17 o superior
- Maven 3.8+
- MySQL 8.0+

### Pasos

1. **Clonar o descargar el proyecto**
```bash
cd tareaTecsup
```

2. **Crear base de datos MySQL**
```sql
CREATE DATABASE medical_equipment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **Actualizar credenciales en `application.properties`**
```properties
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

4. **Compilar con Maven**
```bash
mvn clean install
```

5. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 📡 Endpoints

### Health Check
```
GET /api/v1/health
```

**Response (200):**
```json
{
  "status": "UP",
  "application": "Medical Equipment Maintenance API",
  "version": "1.0.0"
}
```

### Programar Mantenimiento
```
POST /api/v1/maintenance-schedules
```

**Request Body:**
```json
{
  "equipment_id": "550e8400-e29b-41d4-a716-446655440000",
  "scheduled_date": "2026-04-20T10:00:00",
  "maintenance_type": "PREVENTIVE",
  "technician_id": "660e8400-e29b-41d4-a716-446655440000"
}
```

**Response (201):**
```json
{
  "schedule_id": "770e8400-e29b-41d4-a716-446655440000",
  "equipment_id": "550e8400-e29b-41d4-a716-446655440000",
  "status": "SUCCESS",
  "message": "Mantenimiento programado exitosamente"
}
```

**Error Response (400):**
```json
{
  "error": "DOMAIN_ERROR",
  "message": "No se puede programar un mantenimiento en una fecha pasada",
  "timestamp": 1712756400000,
  "path": "/api/v1/maintenance-schedules"
}
```

## 🎯 Reglas de Negocio

### 1. Programación de Mantenimiento
- ❌ No puede programarse en fecha pasada
- ❌ El equipo debe existir
- ❌ El técnico debe existir
- ❌ No puede haber conflicto de horarios para el tecnician (±1 hora)

### 2. Visita de Mantenimiento
- ❌ Solo se puede registrar sobre un cronograma existente
- ❌ No puede haber dos visitas del mismo técnico a la misma hora

### 3. Equipo
- ✅ Campos obligatorios: nombre, marca, modelo, número de serie
- ✅ Número de serie es único
- ✅ Estado inicial: OPERATIONAL

### 4. Técnico
- ✅ Email único y válido
- ✅ Especialización obligatoria

## 🔐 Seguridad (Próximas mejoras)
- [ ] JWT Authentication
- [ ] Role-based Authorization
- [ ] HTTPS enforzado
- [ ] Rate Limiting
- [ ] Input Validation mejorada

## 📚 Tecnologías

| Tecnología | Versión |
|------------|---------|
| Spring Boot | 3.2.0 |
| Spring Data JPA | 3.2.0 |
| Hibernate | 6.2+ |
| MySQL | 8.0+ |
| Java | 17+ |
| Maven | 3.8+ |

## 📖 Referencias

- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Domain-Driven Design by Eric Evans](https://www.domainlanguage.com/ddd/)
- [Spring Boot Official Documentation](https://spring.io/projects/spring-boot)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)

## 📄 Licencia

Este proyecto está bajo licencia MIT.

---

**Construido con ❤️ usando Clean Architecture**
