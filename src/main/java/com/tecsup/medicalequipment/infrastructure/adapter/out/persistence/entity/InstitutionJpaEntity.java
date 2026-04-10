package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa una Institución en la base de datos.
 */
@Entity
@Table(name = "institution", indexes = {
    @Index(name = "idx_institution_name", columnList = "name")
})
public class InstitutionJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "service", nullable = false, length = 255)
    private String service;

    // Constructores
    public InstitutionJpaEntity() {
    }

    public InstitutionJpaEntity(String id, String name, String service) {
        this.id = id;
        this.name = name;
        this.service = service;
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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
