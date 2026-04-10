package com.tecsup.medicalequipment.infrastructure.adapter.in.web;

import com.tecsup.medicalequipment.application.dto.ScheduleMaintenanceCommand;
import com.tecsup.medicalequipment.application.dto.ScheduleMaintenanceResponse;
import com.tecsup.medicalequipment.application.port.in.ScheduleMaintenanceUseCase;
import com.tecsup.medicalequipment.domain.model.MaintenanceSchedule;
import com.tecsup.medicalequipment.domain.repository.MaintenanceScheduleRepository;
import com.tecsup.medicalequipment.infrastructure.adapter.in.web.dto.ScheduleMaintenanceRequest;
import com.tecsup.medicalequipment.infrastructure.adapter.in.web.dto.ScheduleMaintenanceApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller REST que expone el caso de uso "Programar Mantenimiento".
 * 
 * Puerto de entrada (Adapter) para la capa de aplicación.
 * Responsabilidades:
 * - Convertir HTTP requests a comandos de aplicación
 * - Ejecutar el caso de uso inyectado
 * - Convertir respuestas de aplicación a HTTP responses
 * 
 * Sin lógica de negocio, sin acceso a repositorios.
 */
@RestController
@RequestMapping("/api/v1/maintenance-schedules")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ScheduleMaintenanceController {

    private final ScheduleMaintenanceUseCase scheduleMaintenanceUseCase;
    private final MaintenanceScheduleRepository maintenanceScheduleRepository;

    public ScheduleMaintenanceController(ScheduleMaintenanceUseCase scheduleMaintenanceUseCase,
                                         MaintenanceScheduleRepository maintenanceScheduleRepository) {
        this.scheduleMaintenanceUseCase = scheduleMaintenanceUseCase;
        this.maintenanceScheduleRepository = maintenanceScheduleRepository;
    }

    /**
     * Endpoint POST para programar un new mantenimiento.
     *
     * @param request DTO con los datos del mantenimiento
     * @return ResponseEntity con el resultado de la operación
     * 
     * HTTP Status:
     * - 201 Created: Si el mantenimiento fue programado exitosamente
     * - 400 Bad Request: Si hay error de validación o violación de regla de negocio
     * - 500 Internal Server Error: Si hay un error inesperado
     */
    @PostMapping
    public ResponseEntity<ScheduleMaintenanceApiResponse> scheduleNewMaintenance(
            @RequestBody ScheduleMaintenanceRequest request) {

        // Validar el request
        validateRequest(request);

        // Convertir HTTP request a comando de aplicación
        ScheduleMaintenanceCommand command = ScheduleMaintenanceCommand.create(
            request.getEquipmentId(),
            request.getScheduledDate(),
            request.getMaintenanceType(),
            request.getTechnicianId()
        );

        // Ejecutar el caso de uso
        ScheduleMaintenanceResponse response = scheduleMaintenanceUseCase.execute(command);

        // Convertir respuesta de aplicación a HTTP response
        ScheduleMaintenanceApiResponse apiResponse = ScheduleMaintenanceApiResponse.success(
            response.getScheduleId(),
            response.getEquipmentId()
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(apiResponse);
    }

    /**
     * Valida que el request contiene todos los datos requeridos.
     */
    private void validateRequest(ScheduleMaintenanceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("El request no puede ser nulo");
        }
        
        if (request.getEquipmentId() == null || request.getEquipmentId().isBlank()) {
            throw new IllegalArgumentException("El ID del equipo es requerido");
        }
        
        if (request.getScheduledDate() == null) {
            throw new IllegalArgumentException("La fecha programada es requerida");
        }
        
        if (request.getMaintenanceType() == null || request.getMaintenanceType().isBlank()) {
            throw new IllegalArgumentException("El tipo de mantenimiento es requerido");
        }
        
        if (request.getTechnicianId() == null || request.getTechnicianId().isBlank()) {
            throw new IllegalArgumentException("El ID del técnico es requerido");
        }
    }

    // GET /api/v1/maintenance-schedules - Listar todos los mantenimientos
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllSchedules() {
        List<MaintenanceSchedule> schedules = maintenanceScheduleRepository.findAll();

        List<Map<String, Object>> response = schedules.stream().map(s -> Map.<String, Object>of(
            "id", s.getId(),
            "equipmentId", s.getEquipmentId(),
            "scheduledDate", s.getScheduledDate().toString(),
            "type", s.getType().name(),
            "technicianId", s.getTechnicianId(),
            "completed", s.isCompleted()
        )).toList();

        return ResponseEntity.ok(response);
    }

    // GET /api/v1/maintenance-schedules/{id} - Obtener un mantenimiento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getScheduleById(@PathVariable String id) {
        Optional<MaintenanceSchedule> schedule = maintenanceScheduleRepository.findById(id);

        if (schedule.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MaintenanceSchedule s = schedule.get();
        Map<String, Object> response = Map.of(
            "id", s.getId(),
            "equipmentId", s.getEquipmentId(),
            "scheduledDate", s.getScheduledDate().toString(),
            "type", s.getType().name(),
            "technicianId", s.getTechnicianId(),
            "completed", s.isCompleted()
        );

        return ResponseEntity.ok(response);
    }

    // GET /api/v1/maintenance-schedules/pending - Listar mantenimientos pendientes
    @GetMapping("/pending")
    public ResponseEntity<List<Map<String, Object>>> getPendingSchedules() {
        List<MaintenanceSchedule> schedules = maintenanceScheduleRepository.findPendingSchedules();

        List<Map<String, Object>> response = schedules.stream().map(s -> Map.<String, Object>of(
            "id", s.getId(),
            "equipmentId", s.getEquipmentId(),
            "scheduledDate", s.getScheduledDate().toString(),
            "type", s.getType().name(),
            "technicianId", s.getTechnicianId(),
            "completed", s.isCompleted()
        )).toList();

        return ResponseEntity.ok(response);
    }

    /**
     * Health check del controller.
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Maintenance Schedule API is running");
    }
}
