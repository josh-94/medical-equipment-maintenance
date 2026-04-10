# Guía: Configurar PostgreSQL 17 y Cargar Datos de Prueba

## 📋 Requisitos Previos

- PostgreSQL 17 instalado
- pgAdmin 4 (opcional, para interfaz gráfica) o psql (cliente de línea de comandos)
- Maven 3.8+
- Java 17+

---

## 🚀 Paso 1: Instalar PostgreSQL 17

### En Windows

1. Descargar el instalador desde: https://www.postgresql.org/download/windows/
2. Ejecutar el instalador `.exe`
3. Seguir el asistente:
   - Puerto: `5432` (default)
   - Contraseña del usuario `postgres`: `postgres` (o la que desees)
   - Componentes: Dejar todo marcado

4. Verificar la instalación:
```powershell
# En PowerShell
psql --version
```

### En Linux (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install postgresql-17 postgresql-contrib-17

# Iniciar el servicio
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Verificar
psql --version
```

### En macOS

```bash
brew install postgresql@17

# Iniciar el servicio
brew services start postgresql@17

# Verificar
psql --version
```

---

## 📂 Paso 2: Crear la Base de Datos

### Opción A: Usando psql (Línea de Comandos)

```powershell
# Conectar como usuario postgres
psql -U postgres

# En la consola psql, ejecutar:
CREATE DATABASE medical_equipment_db 
  WITH OWNER postgres 
  ENCODING 'UTF8' 
  LOCALE 'en_US.UTF-8';

# Verificar que se creó
\l

# Salir
\q
```

### Opción B: Usando pgAdmin (Interfaz Gráfica)

1. Abrir pgAdmin 4
2. Conectar al servidor PostgreSQL local
3. Click derecho en "Databases"
4. "Create" → "Database"
5. Nombre: `medical_equipment_db`
6. Click "Save"

---

## 🗄️ Paso 3: Cargar el Script SQL

### Opción A: Usando psql

```powershell
# Conectar a la BD e importar el script
psql -U postgres -d medical_equipment_db -f "C:\Users\jcabanillas\Documents\tareaTecsup\src\main\resources\db_init.sql"

# Si tienes contraseña, te pedirá que la ingreses
# Resultado esperado: Tablas creadas + datos insertados
```

### Opción B: Usando pgAdmin

1. Abrir pgAdmin
2. Navegar a `medical_equipment_db`
3. "Tools" → "Query Tool"
4. Copiar y pegar el contenido de `db_init.sql`
5. Click en "Execute" (o Ctrl+Enter)

### Opción C: Usando DBeaver (recomendado)

1. Descargar DBeaver: https://dbeaver.io/
2. Crear nueva conexión a PostgreSQL (localhost:5432)
3. Usuario: `postgres`, Contraseña: `postgres`
4. Click derecho en `medical_equipment_db` → "SQL Editor" → "Open SQL Script"
5. Cargar `db_init.sql`
6. Click en "Execute"

---

## ✨ Paso 4: Verificar que los Datos se Cargaron

```powershell
# Conectar a la BD
psql -U postgres -d medical_equipment_db

# Ejecutar estas consultas para verificar:

-- Ver instituciones
SELECT id, name, service FROM institution;

-- Ver técnicos
SELECT id, name, email, specialization FROM technician;

-- Ver equipos
SELECT id, name, brand, model, serial_number, status FROM equipment;

-- Ver cronogramas
SELECT id, equipment_id, scheduled_date, type, technician_id FROM maintenance_schedule;

-- Contar todos los registros
SELECT 'Instituciones' as tabla, COUNT(*) as cantidad FROM institution
UNION ALL
SELECT 'Equipos', COUNT(*) FROM equipment
UNION ALL
SELECT 'Técnicos', COUNT(*) FROM technician
UNION ALL
SELECT 'Cronogramas', COUNT(*) FROM maintenance_schedule;
```

**Resultado esperado:**
```
 tabla      | cantidad
------------+----------
 Instituciones |        2
 Equipos    |        3
 Técnicos   |        3
 Cronogramas |        2
(4 rows)
```

---

## 🔧 Paso 5: Configurar la Aplicación Spring Boot

### 1. Verificar las credenciales en `application.properties`

El archivo ya está configurado, pero verifica:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/medical_equipment_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

Si tu contraseña es diferente, actualiza:
```properties
spring.datasource.password=TU_CONTRASEÑA_AQUI
```

### 2. Compilar el proyecto

```powershell
cd c:\Users\jcabanillas\Documents\tareaTecsup
mvn clean install -DskipTests
```

---

## 🎯 Paso 6: Ejecutar la Aplicación

```powershell
# En PowerShell, en la carpeta del proyecto
mvn spring-boot:run
```

**Resultado esperado:**
```
.   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_|\__, | / / / /
 =========|_|==============|___/=/_/_/_/

Started MedicalEquipmentMaintenanceApplication in X.XXX seconds
```

---

## ✅ Paso 7: Probar los Endpoints

### Health Check

```powershell
curl http://localhost:8080/api/v1/health
```

**Respuesta esperada:**
```json
{
  "status": "UP",
  "application": "Medical Equipment Maintenance API",
  "version": "1.0.0"
}
```

### Programar Mantenimiento

```powershell
$body = @{
    equipment_id = "eq-001"
    scheduled_date = "2026-05-20T14:00:00"
    maintenance_type = "CORRECTIVE"
    technician_id = "tech-002"
} | ConvertTo-Json

curl -X POST http://localhost:8080/api/v1/maintenance-schedules `
  -ContentType "application/json" `
  -Body $body
```

**Respuesta esperada (201 Created):**
```json
{
  "schedule_id": "550e8400-e29b-41d4-a716-446655440000",
  "equipment_id": "eq-001",
  "status": "SUCCESS",
  "message": "Mantenimiento programado exitosamente"
}
```

---

## 📊 Inspeccionar los Datos desde PostgreSQL

```powershell
# Conectar a la BD
psql -U postgres -d medical_equipment_db

# Ver estructura de tabla
\d equipment

# Ver todas las tablas
\dt

# Ejecutar una consulta con JOIN
SELECT 
    m.id,
    e.name as equipo,
    t.name as tecnico,
    m.scheduled_date,
    m.type
FROM maintenance_schedule m
JOIN equipment e ON m.equipment_id = e.id
JOIN technician t ON m.technician_id = t.id
ORDER BY m.scheduled_date;
```

---

## 🐛 Solucionar Problemas

### Problema: "Error de conexión a PostgreSQL"

**Solución:**
1. Verificar que PostgreSQL está corriendo:
```powershell
# En Windows
Get-Service postgresql-x64-17 | Select-Object Status

# Debe mostrar: Status = Running
```

2. Verificar credenciales en `application.properties`

3. Verificar que la BD existe:
```powershell
psql -U postgres -l
```

### Problema: "FATAL: Ident authentication failed"

**Solución:** Editar `pg_hba.conf` (generalmente en `C:\Program Files\PostgreSQL\17\data\`)

Cambiar la línea de `ident` a `md5` o `scram-sha-256`:
```
# Cambiar esta línea
local   all             all                                     ident

# A esta
local   all             all                                     scram-sha-256
```

Reiniciar PostgreSQL después del cambio.

### Problema: "Contraseña rechazada"

**Solución:** Resetear la contraseña de postgres

```powershell
# En Windows, como Administrador
net stop postgresql-x64-17
```

Editar `postgresql.conf` y agregar:
```
password_encryption = scram-sha-256
```

O reseteando la contraseña en psql:
```powershell
psql -U postgres
ALTER USER postgres PASSWORD 'nueva_contraseña';
```

---

## 📈 Crear Más Datos de Prueba (Opcional)

```sql
-- Agregar más equipos
INSERT INTO equipment (id, name, brand, model, serial_number, warranty_end_date, installation_date, status) 
VALUES 
('eq-004', 'Ventilador Mecánico VM-2000', 'Siemens', 'VM-2000', 'SN-004-2024', '2027-03-15', '2024-03-10', 'OPERATIONAL'),
('eq-005', 'Ecógrafo ECHO-5000', 'Samsung', 'ECHO-5000', 'SN-005-2024', '2028-12-31', '2024-11-01', 'OPERATIONAL');

-- Agregar más técnicos
INSERT INTO technician (id, name, email, specialization) 
VALUES 
('tech-004', 'Ana López Gómez', 'ana.lopez@tecsup.com', 'Ventiladores'),
('tech-005', 'Roberto Díaz Castro', 'roberto.diaz@tecsup.com', 'Ultrasonografía');

-- Agregar más cronogramas
INSERT INTO maintenance_schedule (id, equipment_id, scheduled_date, type, technician_id, completed) 
VALUES 
('sch-003', 'eq-004', '2026-06-15 09:00:00', 'PREVENTIVE', 'tech-004', FALSE),
('sch-004', 'eq-005', '2026-05-30 11:00:00', 'INSPECTION', 'tech-005', FALSE);
```

---

## 📚 Recursos Útiles

- [Documentación PostgreSQL 17](https://www.postgresql.org/docs/17/)
- [pgAdmin Official](https://www.pgadmin.org/)
- [DBeaver Download](https://dbeaver.io/)
- [Spring Boot + PostgreSQL](https://spring.io/projects/spring-data-jpa)

---

**¡Listo!** Tu aplicación Spring Boot está configurada con PostgreSQL 17 y los datos de prueba están cargados.
