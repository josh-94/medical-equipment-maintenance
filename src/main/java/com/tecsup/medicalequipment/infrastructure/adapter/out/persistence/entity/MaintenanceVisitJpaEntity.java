package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa una Visita de Mantenimiento en la base de datos.
 */
@Entity
@Table(name = "maintenance_visit", indexes = {
    @Index(name = "idx_visit_schedule", columnList = "maintenance_schedule_id"),
    @Index(name = "idx_visit_technician", columnList = "technician_id"),
    @Index(name = "idx_visit_date", columnList = "visit_date")
})
public class MaintenanceVisitJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "maintenance_schedule_id", nullable = false, length = 36)
    private String maintenanceScheduleId;

    @Column(name = "visit_date", nullable = false)
    private java.time.LocalDateTime visitDate;

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;

    @Column(name = "technician_id", nullable = false, length = 36)
    private String technicianId;

    // Constructores
    public MaintenanceVisitJpaEntity() {
    }

    public MaintenanceVisitJpaEntity(String id, String maintenanceScheduleId,
                                    java.time.LocalDateTime visitDate, String observations,
                                    String technicianId) {
        this.id = id;
        this.maintenanceScheduleId = maintenanceScheduleId;
        this.visitDate = visitDate;
        this.observations = observations;
        this.technicianId = technicianId;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaintenanceScheduleId() {
        return maintenanceScheduleId;
    }

    public void setMaintenanceScheduleId(String maintenanceScheduleId) {
        this.maintenanceScheduleId = maintenanceScheduleId;
    }

    public java.time.LocalDateTime getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(java.time.LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }
}
