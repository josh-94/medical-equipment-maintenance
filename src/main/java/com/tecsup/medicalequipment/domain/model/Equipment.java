package com.tecsup.medicalequipment.domain.model;

import com.tecsup.medicalequipment.domain.exception.DomainException;
import com.tecsup.medicalequipment.domain.model.valueobjects.EquipmentId;
import com.tecsup.medicalequipment.domain.model.valueobjects.MaintenanceStatus;
import com.tecsup.medicalequipment.domain.model.valueobjects.SerialNumber;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad del dominio que representa un Equipo Médico.
 * Contiene datos y comportamiento relacionado con equipos médicos.
 */
public class Equipment {

    private final EquipmentId id;
    private final String name;
    private final String brand;
    private final String model;
    private final SerialNumber serialNumber;
    private final LocalDate warrantyEndDate;
    private MaintenanceStatus status;
    private final LocalDate installationDate;

    private Equipment(EquipmentId id, String name, String brand, String model,
                      SerialNumber serialNumber, LocalDate warrantyEndDate,
                      MaintenanceStatus status, LocalDate installationDate) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.warrantyEndDate = warrantyEndDate;
        this.status = status;
        this.installationDate = installationDate;
    }

    /**
     * Factory method para crear un nuevo Equipo.
     */
    public static Equipment create(String name, String brand, String model,
                                    SerialNumber serialNumber, LocalDate warrantyEndDate,
                                    LocalDate installationDate) {
        validateEquipment(name, brand, model, warrantyEndDate, installationDate);
        return new Equipment(
            EquipmentId.generate(),
            name,
            brand,
            model,
            serialNumber,
            warrantyEndDate,
            MaintenanceStatus.OPERATIONAL,
            installationDate
        );
    }

    /**
     * Reconstruir una entidad existente desde persistencia.
     */
    public static Equipment reconstruct(EquipmentId id, String name, String brand, String model,
                                        SerialNumber serialNumber, LocalDate warrantyEndDate,
                                        MaintenanceStatus status, LocalDate installationDate) {
        validateEquipment(name, brand, model, warrantyEndDate, installationDate);
        return new Equipment(id, name, brand, model, serialNumber, warrantyEndDate, status, installationDate);
    }

    private static void validateEquipment(String name, String brand, String model,
                                          LocalDate warrantyEndDate, LocalDate installationDate) {
        if (name == null || name.isBlank()) {
            throw new DomainException("El nombre del equipo no puede estar vacío");
        }
        if (brand == null || brand.isBlank()) {
            throw new DomainException("La marca del equipo no puede estar vacía");
        }
        if (model == null || model.isBlank()) {
            throw new DomainException("El modelo del equipo no puede estar vacío");
        }
        if (warrantyEndDate == null) {
            throw new DomainException("La fecha de fin de garantía no puede ser nula");
        }
        if (installationDate == null) {
            throw new DomainException("La fecha de instalación no puede ser nula");
        }
        if (installationDate.isAfter(warrantyEndDate)) {
            throw new DomainException("La fecha de instalación no puede ser posterior a la garantía");
        }
    }

    public void changeStatus(MaintenanceStatus newStatus) {
        if (newStatus == null) {
            throw new DomainException("El estado no puede ser nulo");
        }
        this.status = newStatus;
    }

    public boolean isUnderWarranty() {
        return LocalDate.now().isBefore(warrantyEndDate);
    }

    public boolean isWarrantyExpired() {
        return LocalDate.now().isAfter(warrantyEndDate);
    }

    // Getters
    public EquipmentId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public SerialNumber getSerialNumber() {
        return serialNumber;
    }

    public LocalDate getWarrantyEndDate() {
        return warrantyEndDate;
    }

    public MaintenanceStatus getStatus() {
        return status;
    }

    public LocalDate getInstallationDate() {
        return installationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return Objects.equals(id, equipment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", serialNumber=" + serialNumber +
                ", status=" + status +
                '}';
    }
}
