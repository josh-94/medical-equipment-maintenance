package com.tecsup.medicalequipment.infrastructure.adapter.in.web;

import com.tecsup.medicalequipment.domain.model.Equipment;
import com.tecsup.medicalequipment.domain.model.MaintenanceSchedule;
import com.tecsup.medicalequipment.domain.model.valueobjects.EquipmentId;
import com.tecsup.medicalequipment.domain.repository.EquipmentRepository;
import com.tecsup.medicalequipment.domain.repository.MaintenanceScheduleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/equipment")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EquipmentController {

    private final EquipmentRepository equipmentRepository;
    private final MaintenanceScheduleRepository maintenanceScheduleRepository;

    public EquipmentController(EquipmentRepository equipmentRepository,
                               MaintenanceScheduleRepository maintenanceScheduleRepository) {
        this.equipmentRepository = equipmentRepository;
        this.maintenanceScheduleRepository = maintenanceScheduleRepository;
    }

    // GET /api/v1/equipment - Listar todos los equipos
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllEquipment() {
        List<Equipment> equipment = equipmentRepository.findAll();

        List<Map<String, Object>> response = equipment.stream().map(e -> Map.<String, Object>of(
            "id", e.getId().getValue(),
            "name", e.getName(),
            "brand", e.getBrand(),
            "model", e.getModel(),
            "serialNumber", e.getSerialNumber().getValue(),
            "status", e.getStatus().name(),
            "warrantyEndDate", e.getWarrantyEndDate().toString(),
            "installationDate", e.getInstallationDate().toString()
        )).toList();

        return ResponseEntity.ok(response);
    }

    // GET /api/v1/equipment/{id} - Obtener un equipo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEquipmentById(@PathVariable String id) {
        Optional<Equipment> equipment = equipmentRepository.findById(EquipmentId.create(id));

        if (equipment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Equipment e = equipment.get();
        Map<String, Object> response = Map.of(
            "id", e.getId().getValue(),
            "name", e.getName(),
            "brand", e.getBrand(),
            "model", e.getModel(),
            "serialNumber", e.getSerialNumber().getValue(),
            "status", e.getStatus().name(),
            "warrantyEndDate", e.getWarrantyEndDate().toString(),
            "installationDate", e.getInstallationDate().toString()
        );

        return ResponseEntity.ok(response);
    }

    // GET /api/v1/equipment/{id}/schedules - Ver mantenimientos de un equipo
    @GetMapping("/{id}/schedules")
    public ResponseEntity<Map<String, Object>> getEquipmentSchedules(@PathVariable String id) {
        Optional<Equipment> equipment = equipmentRepository.findById(EquipmentId.create(id));

        if (equipment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<MaintenanceSchedule> schedules = maintenanceScheduleRepository.findByEquipmentId(id);

        Equipment e = equipment.get();
        List<Map<String, Object>> scheduleList = schedules.stream().map(s -> Map.<String, Object>of(
            "id", s.getId(),
            "scheduledDate", s.getScheduledDate().toString(),
            "type", s.getType().name(),
            "technicianId", s.getTechnicianId(),
            "completed", s.isCompleted()
        )).toList();

        Map<String, Object> response = Map.of(
            "equipment", Map.of(
                "id", e.getId().getValue(),
                "name", e.getName(),
                "brand", e.getBrand(),
                "status", e.getStatus().name()
            ),
            "totalSchedules", scheduleList.size(),
            "schedules", scheduleList
        );

        return ResponseEntity.ok(response);
    }
}
