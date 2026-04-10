package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa un Equipo Médico en la base de datos.
 * Es el equivalente técnico de Equipment del dominio.
 * NO debe contener lógica de negocio.
 */
@Entity
@Table(name = "equipment", indexes = {
    @Index(name = "idx_serial_number", columnList = "serial_number", unique = true),
    @Index(name = "idx_status", columnList = "status")
})
public class EquipmentJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Column(name = "model", nullable = false, length = 100)
    private String model;

    @Column(name = "serial_number", nullable = false, unique = true, length = 100)
    private String serialNumber;

    @Column(name = "warranty_end_date", nullable = false)
    private java.time.LocalDate warrantyEndDate;

    @Column(name = "installation_date", nullable = false)
    private java.time.LocalDate installationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private MaintenanceStatusJpaEnum status;

    // Constructores
    public EquipmentJpaEntity() {
        // Constructor vacío para JPA
    }

    public EquipmentJpaEntity(String id, String name, String brand, String model,
                             String serialNumber, java.time.LocalDate warrantyEndDate,
                             java.time.LocalDate installationDate, MaintenanceStatusJpaEnum status) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.warrantyEndDate = warrantyEndDate;
        this.installationDate = installationDate;
        this.status = status;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public java.time.LocalDate getWarrantyEndDate() {
        return warrantyEndDate;
    }

    public void setWarrantyEndDate(java.time.LocalDate warrantyEndDate) {
        this.warrantyEndDate = warrantyEndDate;
    }

    public java.time.LocalDate getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(java.time.LocalDate installationDate) {
        this.installationDate = installationDate;
    }

    public MaintenanceStatusJpaEnum getStatus() {
        return status;
    }

    public void setStatus(MaintenanceStatusJpaEnum status) {
        this.status = status;
    }
}
