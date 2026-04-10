package com.tecsup.medicalequipment.infrastructure.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para endpoints de health check y información de la API.
 */
@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        HealthResponse response = new HealthResponse(
            "UP",
            "Medical Equipment Maintenance API",
            "1.0.0"
        );
        return ResponseEntity.ok(response);
    }

    /**
     * DTO para respuesta de health check.
     */
    public static class HealthResponse {
        private String status;
        private String application;
        private String version;

        public HealthResponse(String status, String application, String version) {
            this.status = status;
            this.application = application;
            this.version = version;
        }

        public String getStatus() {
            return status;
        }

        public String getApplication() {
            return application;
        }

        public String getVersion() {
            return version;
        }
    }
}
