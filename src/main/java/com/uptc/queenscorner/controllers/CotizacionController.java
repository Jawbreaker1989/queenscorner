package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.models.dtos.requests.CotizacionRequest;
import com.uptc.queenscorner.models.dtos.requests.EstadoUpdateRequest;
import com.uptc.queenscorner.models.dtos.responses.ApiResponse;
import com.uptc.queenscorner.models.dtos.responses.CotizacionResponse;
import com.uptc.queenscorner.services.ICotizacionService;
import com.uptc.queenscorner.services.async.PdfAsyncService;
import com.uptc.queenscorner.repositories.ICotizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cotizaciones")
public class CotizacionController {

    @Autowired
    private ICotizacionService cotizacionService;

    @Autowired
    private PdfAsyncService pdfAsyncService;

    @Autowired
    private ICotizacionRepository cotizacionRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CotizacionResponse>>> getAll() {
        List<CotizacionResponse> cotizaciones = cotizacionService.findAll();
        ApiResponse<List<CotizacionResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cotizaciones obtenidas exitosamente");
        response.setData(cotizaciones);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CotizacionResponse>> getById(@PathVariable Long id) {
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
        response.setMessage("Estado de cotización actualizado exitosamente");
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