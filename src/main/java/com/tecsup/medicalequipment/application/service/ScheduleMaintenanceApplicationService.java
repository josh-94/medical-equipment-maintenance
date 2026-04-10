package com.tecsup.medicalequipment.application.service;

import com.tecsup.medicalequipment.application.dto.ScheduleMaintenanceCommand;
import com.tecsup.medicalequipment.application.dto.ScheduleMaintenanceResponse;
import com.tecsup.medicalequipment.application.port.in.ScheduleMaintenanceUseCase;
import com.tecsup.medicalequipment.application.port.out.LoadEquipmentPort;
import com.tecsup.medicalequipment.application.port.out.LoadTechnicianPort;
import com.tecsup.medicalequipment.application.port.out.SaveMaintenanceSchedulePort;
import com.tecsup.medicalequipment.domain.exception.DomainException;
import com.tecsup.medicalequipment.domain.model.Equipment;
import com.tecsup.medicalequipment.domain.model.MaintenanceSchedule;
import com.tecsup.medicalequipment.domain.model.MaintenanceType;
import com.tecsup.medicalequipment.domain.model.Technician;
import com.tecsup.medicalequipment.domain.model.valueobjects.EquipmentId;
import com.tecsup.medicalequipment.domain.repository.MaintenanceScheduleRepository;
import com.tecsup.medicalequipment.domain.service.MaintenanceScheduleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementación del caso de uso: Programar un Mantenimiento.
 * 
 * NO importa nada de Spring ni JPA.
 * Solo usa las interfaces del dominio y los puertos de aplicación.
 * 
 * Orquesta:
 * 1. Cargar el equipo y validar que existe
 * 2. Cargar el técnico y validar que existe
 * 3. Crear el cronograma de mantenimiento usando la lógica del dominio
 * 4. Validar conflictos de horarios mediante el servicio de dominio
 * 5. Persistir el cronograma
 */
@Service
public class ScheduleMaintenanceApplicationService implements ScheduleMaintenanceUseCase {

    private final LoadEquipmentPort loadEquipmentPort;
    private final LoadTechnicianPort loadTechnicianPort;
    private final SaveMaintenanceSchedulePort saveMaintenanceSchedulePort;
    private final MaintenanceScheduleRepository maintenanceScheduleRepository;
    private final MaintenanceScheduleService maintenanceScheduleService;

    public ScheduleMaintenanceApplicationService(
            LoadEquipmentPort loadEquipmentPort,
            LoadTechnicianPort loadTechnicianPort,
            SaveMaintenanceSchedulePort saveMaintenanceSchedulePort,
            MaintenanceScheduleRepository maintenanceScheduleRepository,
            MaintenanceScheduleService maintenanceScheduleService) {
        this.loadEquipmentPort = loadEquipmentPort;
        this.loadTechnicianPort = loadTechnicianPort;
        this.saveMaintenanceSchedulePort = saveMaintenanceSchedulePort;
        this.maintenanceScheduleRepository = maintenanceScheduleRepository;
        this.maintenanceScheduleService = maintenanceScheduleService;
    }

    /**
     * Ejecuta el caso de uso: Programar un Mantenimiento.
     *
     * Pasos:
     * 1. Validar el comando
     * 2. Cargar el equipo desde el puerto
     * 3. Cargar el técnico desde el puerto
     * 4. Crear el cronograma de mantenimiento (dominio)
     * 5. Validar conflictos de horarios (servicio de dominio)
     * 6. Persistir el cronograma
     * 7. Retornar respuesta
     *
     * @param command comando con los datos del mantenimiento a programar
     * @return respuesta con el ID del cronograma creado
     * @throws IllegalArgumentException si el comando es inválido
     * @throws DomainException si se viola una regla de negocio (fecha pasada, conflictos, etc.)
     */
    @Override
    public ScheduleMaintenanceResponse execute(ScheduleMaintenanceCommand command) {
        // Paso 1: Validar el comando (ya se valida en el factory method del command)
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo");
        }

        // Paso 2: Cargar el equipo
        Optional<Equipment> equipmentOpt = loadEquipmentPort.loadEquipmentById(command.getEquipmentId());
        if (equipmentOpt.isEmpty()) {
            throw new DomainException("No existe equipo con ID: " + command.getEquipmentId());
        }
        Equipment equipment = equipmentOpt.get();

        // Paso 3: Cargar el técnico
        Optional<Technician> technicianOpt = loadTechnicianPort.loadTechnicianById(command.getTechnicianId());
        if (technicianOpt.isEmpty()) {
            throw new DomainException("No existe técnico con ID: " + command.getTechnicianId());
        }
        Technician technician = technicianOpt.get();

        // Paso 4: Crear el cronograma de mantenimiento
        MaintenanceType maintenanceType = parseMaintenanceType(command.getMaintenanceType());
        
        MaintenanceSchedule schedule = MaintenanceSchedule.create(
            equipment.getId().getValue(),
            command.getScheduledDate(),
            maintenanceType,
            technician.getId()
        );

        // Paso 5: Validar conflictos de horarios usando el servicio de dominio
        maintenanceScheduleService.validateScheduleForTechnician(schedule);

        // Paso 6: Persistir el cronograma
        saveMaintenanceSchedulePort.saveSchedule(schedule);

        // Paso 7: Retornar respuesta exitosa
        return ScheduleMaintenanceResponse.success(schedule.getId(), equipment.getId().getValue());
    }

    /**
     * Convierte un string a MaintenanceType.
     * Método auxiliar para convertir datos del comando a entidades de dominio.
     */
    private MaintenanceType parseMaintenanceType(String typeString) {
        try {
            return MaintenanceType.valueOf(typeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Tipo de mantenimiento inválido: " + typeString + 
                ". Valores válidos: " + getValidMaintenanceTypes()
            );
        }
    }

    /**
     * Retorna los tipos de mantenimiento válidos para mensajes de error.
     */
    private String getValidMaintenanceTypes() {
        StringBuilder types = new StringBuilder();
        for (MaintenanceType type : MaintenanceType.values()) {
            if (types.length() > 0) {
                types.append(", ");
            }
            types.append(type.name());
        }
        return types.toString();
    }
}
