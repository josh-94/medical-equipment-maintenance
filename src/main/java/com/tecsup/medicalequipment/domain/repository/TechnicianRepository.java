package com.tecsup.medicalequipment.domain.repository;

import com.tecsup.medicalequipment.domain.model.Technician;

import java.util.Optional;
import java.util.List;

/**
 * Interfaz de repositorio para la entidad Technician.
 * Define contratos sin dependencia de la infraestructura.
 */
public interface TechnicianRepository {

    /**
     * Guardar un nuevo técnico o actualizar uno existente.
     */
    void save(Technician technician);

    /**
     * Buscar un técnico por su ID.
     */
    Optional<Technician> findById(String id);

    /**
     * Buscar un técnico por su correo electrónico.
     */
    Optional<Technician> findByEmail(String email);

    /**
     * Buscar técnicos por especialización.
     */
    List<Technician> findBySpecialization(String specialization);

    /**
     * Obtener todos los técnicos.
     */
    List<Technician> findAll();

    /**
     * Eliminar un técnico por su ID.
     */
    void deleteById(String id);
}
