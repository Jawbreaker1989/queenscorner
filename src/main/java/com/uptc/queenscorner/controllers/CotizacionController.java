package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.models.dtos.requests.CotizacionRequest;
import com.uptc.queenscorner.models.dtos.requests.EstadoUpdateRequest;
import com.uptc.queenscorner.models.dtos.responses.ApiResponse;
import com.uptc.queenscorner.models.dtos.responses.CotizacionResponse;
import com.uptc.queenscorner.services.ICotizacionService;
import com.uptc.queenscorner.services.async.PdfAsyncService;
import com.uptc.queenscorner.repositories.ICotizacionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para gestionar cotizaciones (presupuestos)
 * Maneja creación, edición, cambio de estado y generación de PDF de cotizaciones
 * Las cotizaciones son base para crear negocios y facturas
 * Estados válidos: BORRADOR → ENVIADA → APROBADA o RECHAZADA
 */
@RestController
@RequestMapping("/api/cotizaciones")
@Tag(name = "Cotizaciones", description = "Gestión de cotizaciones y presupuestos")
public class CotizacionController {

    @Autowired
    private ICotizacionService cotizacionService;

    @Autowired
    private PdfAsyncService pdfAsyncService;

    @Autowired
    private ICotizacionRepository cotizacionRepository;

    /**
     * Obtiene todas las cotizaciones
     * Los resultados incluyen cliente, items y cálculos de totales
     * @return Lista completa de cotizaciones del sistema
     */
    @GetMapping
    @Operation(summary = "Listar todas las cotizaciones", description = "Obtiene el listado completo de cotizaciones del sistema")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado de cotizaciones obtenido exitosamente")
    })
    public ResponseEntity<ApiResponse<List<CotizacionResponse>>> getAll() {
        List<CotizacionResponse> cotizaciones = cotizacionService.findAll();
        ApiResponse<List<CotizacionResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cotizaciones obtenidas exitosamente");
        response.setData(cotizaciones);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene una cotización específica por su ID
     * @param id Identificador único de la cotización
     * @return Datos completos de la cotización con cliente e items
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener cotización por ID", description = "Recupera los detalles de una cotización específica")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cotización obtenida exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cotización no encontrada")
    })
    public ResponseEntity<ApiResponse<CotizacionResponse>> getById(
            @PathVariable @Parameter(description = "ID de la cotización") Long id) {
        CotizacionResponse cotizacion = cotizacionService.findById(id);
        ApiResponse<CotizacionResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cotización obtenida exitosamente");
        response.setData(cotizacion);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ApiResponse<CotizacionResponse>> getByCodigo(@PathVariable String codigo) {
        CotizacionResponse cotizacion = cotizacionService.findByCodigo(codigo);
        ApiResponse<CotizacionResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cotización obtenida exitosamente");
        response.setData(cotizacion);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CotizacionResponse>> create(@RequestBody CotizacionRequest request) {
        CotizacionResponse cotizacion = cotizacionService.create(request);
        ApiResponse<CotizacionResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cotización creada exitosamente");
        response.setData(cotizacion);
        response.setStatus(HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CotizacionResponse>> update(@PathVariable Long id, @RequestBody CotizacionRequest request) {
        CotizacionResponse cotizacion = cotizacionService.update(id, request);
        ApiResponse<CotizacionResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cotización actualizada exitosamente");
        response.setData(cotizacion);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<CotizacionResponse>> cambiarEstado(@PathVariable Long id, @RequestBody EstadoUpdateRequest request) {
        CotizacionResponse cotizacion = cotizacionService.cambiarEstado(id, request.getEstado());
        ApiResponse<CotizacionResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        
        var cotizacionEntity = cotizacionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));
        
        // PROCESOS SEGÚN ESTADO
        if ("ENVIADA".equals(request.getEstado())) {
            response.setMessage("Estado actualizado a ENVIADA");
        } 
        else if ("APROBADA".equals(request.getEstado())) {
            // Generar PDF de aprobación en hilo separado
            pdfAsyncService.generarPdfCotizacion(cotizacionEntity);
            response.setMessage("Estado actualizado a APROBADA");
        }
        else if ("RECHAZADA".equals(request.getEstado())) {
            response.setMessage("Estado actualizado a RECHAZADA");
        }
        
        response.setData(cotizacion);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        cotizacionService.delete(id);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cotización eliminada exitosamente");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/pdf")
    public ResponseEntity<ApiResponse<String>> generarPdf(@PathVariable Long id) {
        try {
            var cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));
            
            // Generar PDF de forma asíncrona usando hilos
            pdfAsyncService.generarPdfCotizacion(cotizacion);
            
            ApiResponse<String> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("PDF de cotización en generación (proceso asíncrono)");
            response.setData("Generando PDF para cotización: " + cotizacion.getCodigo());
            response.setStatus(HttpStatus.ACCEPTED.value());
            return ResponseEntity.accepted().body(response);
            
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("Error al generar PDF: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}