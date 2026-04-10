package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa un Cronograma de Mantenimiento en la base de datos.
 */
@Entity
@Table(name = "maintenance_schedule", indexes = {
    @Index(name = "idx_schedule_equipment", columnList = "equipment_id"),
    @Index(name = "idx_schedule_technician", columnList = "technician_id"),
    @Index(name = "idx_schedule_date", columnList = "scheduled_date")
})
public class MaintenanceScheduleJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "equipment_id", nullable = false, length = 36)
    private String equipmentId;

    @Column(name = "scheduled_date", nullable = false)
    private java.time.LocalDateTime scheduledDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private MaintenanceTypeJpaEnum type;

    @Column(name = "technician_id", nullable = false, length = 36)
    private String technicianId;

    @Column(name = "actual_start_date")
    private java.time.LocalDateTime actualStartDate;

    @Column(name = "actual_end_date")
    private java.time.LocalDateTime actualEndDate;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    // Constructores
    public MaintenanceScheduleJpaEntity() {
    }

    public MaintenanceScheduleJpaEntity(String id, String equipmentId, java.time.LocalDateTime scheduledDate,
                                       MaintenanceTypeJpaEnum type, String technicianId,
                                       java.time.LocalDateTime actualStartDate, java.time.LocalDateTime actualEndDate,
                                       boolean completed) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.scheduledDate = scheduledDate;
        this.type = type;
        this.technicianId = technicianId;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
        this.completed = completed;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public java.time.LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(java.time.LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public MaintenanceTypeJpaEnum getType() {
        return type;
    }

    public void setType(MaintenanceTypeJpaEnum type) {
        this.type = type;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public java.time.LocalDateTime getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(java.time.LocalDateTime actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public java.time.LocalDateTime getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(java.time.LocalDateTime actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
