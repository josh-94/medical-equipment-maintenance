package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa un Técnico de Mantenimiento en la base de datos.
 */
@Entity
@Table(name = "technician", indexes = {
    @Index(name = "idx_technician_email", columnList = "email", unique = true),
    @Index(name = "idx_technician_specialization", columnList = "specialization")
})
public class TechnicianJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "specialization", nullable = false, length = 255)
    private String specialization;

    // Constructores
    public TechnicianJpaEntity() {
    }

    public TechnicianJpaEntity(String id, String name, String email, String specialization) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.specialization = specialization;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
