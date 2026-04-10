package com.tecsup.medicalequipment.domain.repository;

import com.tecsup.medicalequipment.domain.model.Institution;

import java.util.Optional;
import java.util.List;

/**
 * Interfaz de repositorio para la entidad Institution.
 * Define contratos sin dependencia de la infraestructura.
 */
public interface InstitutionRepository {

    /**
     * Guardar una nueva institución o actualizar una existente.
     */
    void save(Institution institution);

    /**
     * Buscar una institución por su ID.
     */
    Optional<Institution> findById(String id);

    /**
     * Buscar instituciones por nombre (búsqueda parcial).
     */
    List<Institution> findByNameContaining(String name);

    /**
     * Obtener todas las instituciones.
     */
    List<Institution> findAll();

    /**
     * Eliminar una institución por su ID.
     */
    void deleteById(String id);
}
