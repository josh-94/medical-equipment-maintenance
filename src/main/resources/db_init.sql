-- Script de inicialización de la base de datos PostgreSQL
-- Medical Equipment Maintenance System
-- Compatible con PostgreSQL 17

-- Crear esquema (opcional)
CREATE SCHEMA IF NOT EXISTS medical_equipment;

-- Crear tabla de Instituciones
CREATE TABLE IF NOT EXISTS institution (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    service VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_institution_name ON institution(name);

-- Crear tabla de Equipos
CREATE TABLE IF NOT EXISTS equipment (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    warranty_end_date DATE NOT NULL,
    installation_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'OPERATIONAL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_equipment_serial_number ON equipment(serial_number);
CREATE INDEX IF NOT EXISTS idx_equipment_status ON equipment(status);

-- Crear tabla de Técnicos
CREATE TABLE IF NOT EXISTS technician (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    specialization VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_technician_email ON technician(email);
CREATE INDEX IF NOT EXISTS idx_technician_specialization ON technician(specialization);

-- Crear tabla de Cronogramas de Mantenimiento
CREATE TABLE IF NOT EXISTS maintenance_schedule (
    id VARCHAR(36) PRIMARY KEY,
    equipment_id VARCHAR(36) NOT NULL,
    scheduled_date TIMESTAMP NOT NULL,
    type VARCHAR(50) NOT NULL,
    technician_id VARCHAR(36) NOT NULL,
    actual_start_date TIMESTAMP,
    actual_end_date TIMESTAMP,
    completed BOOLEAN DEFAULT FALSE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE,
    FOREIGN KEY (technician_id) REFERENCES technician(id) ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_maintenance_schedule_equipment ON maintenance_schedule(equipment_id);
CREATE INDEX IF NOT EXISTS idx_maintenance_schedule_technician ON maintenance_schedule(technician_id);
CREATE INDEX IF NOT EXISTS idx_maintenance_schedule_date ON maintenance_schedule(scheduled_date);

-- Crear tabla de Visitas de Mantenimiento
CREATE TABLE IF NOT EXISTS maintenance_visit (
    id VARCHAR(36) PRIMARY KEY,
    maintenance_schedule_id VARCHAR(36) NOT NULL,
    visit_date TIMESTAMP NOT NULL,
    observations TEXT,
    technician_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (maintenance_schedule_id) REFERENCES maintenance_schedule(id) ON DELETE CASCADE,
    FOREIGN KEY (technician_id) REFERENCES technician(id) ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_maintenance_visit_schedule ON maintenance_visit(maintenance_schedule_id);
CREATE INDEX IF NOT EXISTS idx_maintenance_visit_technician ON maintenance_visit(technician_id);
CREATE INDEX IF NOT EXISTS idx_maintenance_visit_date ON maintenance_visit(visit_date);

-- Crear tabla de auditoría (opcional)
CREATE TABLE IF NOT EXISTS audit_log (
    id SERIAL PRIMARY KEY,
    entity_type VARCHAR(100) NOT NULL,
    entity_id VARCHAR(36) NOT NULL,
    action VARCHAR(50) NOT NULL,
    old_value JSONB,
    new_value JSONB,
    changed_by VARCHAR(255),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_audit_log_entity ON audit_log(entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_audit_log_action ON audit_log(action);
CREATE INDEX IF NOT EXISTS idx_audit_log_changed_at ON audit_log(changed_at);

-- ============================================
-- DATOS DE PRUEBA
-- ============================================

-- Insertar instituciones de ejemplo
INSERT INTO institution (id, name, service) VALUES 
('inst-001', 'Hospital Central', 'Cardiología')
ON CONFLICT (id) DO NOTHING;

INSERT INTO institution (id, name, service) VALUES 
('inst-002', 'Clínica del Sur', 'Radiología')
ON CONFLICT (id) DO NOTHING;

-- Insertar técnicos de ejemplo
INSERT INTO technician (id, name, email, specialization) VALUES 
('tech-001', 'Carlos García López', 'carlos.garcia@tecsup.com', 'Cardiología')
ON CONFLICT (id) DO NOTHING;

INSERT INTO technician (id, name, email, specialization) VALUES 
('tech-002', 'María Rodríguez Pérez', 'maria.rodriguez@tecsup.com', 'Radiología')
ON CONFLICT (id) DO NOTHING;

INSERT INTO technician (id, name, email, specialization) VALUES 
('tech-003', 'Juan Martínez Silva', 'juan.martinez@tecsup.com', 'Cardiología')
ON CONFLICT (id) DO NOTHING;

-- Insertar equipos de ejemplo
INSERT INTO equipment (id, name, brand, model, serial_number, warranty_end_date, installation_date, status) VALUES 
('eq-001', 'Desfibrilador DEF-5000', 'Philips', 'DEF-5000', 'SN-001-2024', '2027-12-31', '2024-01-15', 'OPERATIONAL')
ON CONFLICT (id) DO NOTHING;

INSERT INTO equipment (id, name, brand, model, serial_number, warranty_end_date, installation_date, status) VALUES 
('eq-002', 'Monitor Cardíaco MC-3000', 'GE Healthcare', 'MC-3000', 'SN-002-2024', '2028-06-30', '2024-02-01', 'OPERATIONAL')
ON CONFLICT (id) DO NOTHING;

INSERT INTO equipment (id, name, brand, model, serial_number, warranty_end_date, installation_date, status) VALUES 
('eq-003', 'Bomba de Infusión BI-7000', 'Baxter', 'BI-7000', 'SN-003-2024', '2026-08-15', '2023-06-20', 'OPERATIONAL')
ON CONFLICT (id) DO NOTHING;

-- Insertar cronogramas de ejemplo
INSERT INTO maintenance_schedule (id, equipment_id, scheduled_date, type, technician_id, completed) VALUES 
('sch-001', 'eq-001', '2026-05-01 10:00:00', 'PREVENTIVE', 'tech-001', FALSE)
ON CONFLICT (id) DO NOTHING;

INSERT INTO maintenance_schedule (id, equipment_id, scheduled_date, type, technician_id, completed) VALUES 
('sch-002', 'eq-002', '2026-04-25 14:30:00', 'INSPECTION', 'tech-002', FALSE)
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- VERIFICACIÓN
-- ============================================

-- Contar registros
SELECT 'Instituciones' as tabla, COUNT(*) as cantidad FROM institution
UNION ALL
SELECT 'Equipos', COUNT(*) FROM equipment
UNION ALL
SELECT 'Técnicos', COUNT(*) FROM technician
UNION ALL
SELECT 'Cronogramas', COUNT(*) FROM maintenance_schedule;

