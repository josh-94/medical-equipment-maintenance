package com.tecsup.medicalequipment.domain.repository;

import com.tecsup.medicalequipment.domain.model.Equipment;
import com.tecsup.medicalequipment.domain.model.valueobjects.EquipmentId;

import java.util.Optional;
import java.util.List;

/**
 * Interfaz de repositorio para la entidad Equipment.
 * Define contratos sin dependencia de la infraestructura.
 */
public interface EquipmentRepository {

    /**
     * Guardar un nuevo equipo o actualizar uno existente.
     */
    void save(Equipment equipment);

    /**
     * Buscar un equipo por su ID.
     */
    Optional<Equipment> findById(EquipmentId id);

    /**
     * Buscar un equipo por su número de serie.
     */
    Optional<Equipment> findBySerialNumber(String serialNumber);

    /**
     * Obtener todos los equipos.
     */
    List<Equipment> findAll();

    /**
     * Eliminar un equipo por su ID.
     */
    void deleteById(EquipmentId id);

    /**
     * Verificar si existe un equipo con un número de serie dado.
     */
    boolean existsBySerialNumber(String serialNumber);
}
