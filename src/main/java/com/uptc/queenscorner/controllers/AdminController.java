package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.models.dtos.responses.ApiResponse;
import com.uptc.queenscorner.models.dtos.responses.SystemStats;
import com.uptc.queenscorner.services.IAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de administración del sistema
 * Proporciona endpoints para mantenimiento y gestión del sistema
 * Solo accesible para usuarios con rol ADMIN
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
@Tag(name = "Administración", description = "Operaciones de administración del sistema")
public class AdminController {

    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Limpia completamente todos los datos del sistema
     * Útil para demos y testing - elimina todos los registros
     */
    @DeleteMapping("/clean-data")
    @Operation(summary = "Limpiar todos los datos", description = "Elimina todos los registros del sistema manteniendo la estructura. Útil para demos.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Datos limpiados exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<String>> cleanAllData() {
        try {
            adminService.cleanAllData();

            ApiResponse<String> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Todos los datos han sido limpiados exitosamente");
            response.setData("Sistema listo para demo");
            response.setStatus(HttpStatus.OK.value());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("Error al limpiar los datos: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtiene estadísticas del sistema
     */
    @GetMapping("/stats")
    @Operation(summary = "Estadísticas del sistema", description = "Obtiene estadísticas generales del sistema")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente")
    })
    public ResponseEntity<ApiResponse<SystemStats>> getSystemStats() {
        SystemStats stats = adminService.getSystemStats();

        ApiResponse<SystemStats> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Estadísticas del sistema obtenidas exitosamente");
        response.setData(stats);
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }
}