package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.models.dtos.requests.OrdenTrabajoRequest;
import com.uptc.queenscorner.models.dtos.requests.EstadoUpdateRequest;
import com.uptc.queenscorner.models.dtos.responses.ApiResponse;
import com.uptc.queenscorner.models.dtos.responses.OrdenTrabajoResponse;
import com.uptc.queenscorner.services.IOrdenTrabajoService;
import com.uptc.queenscorner.services.async.PdfAsyncService;
import com.uptc.queenscorner.repositories.IOrdenTrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes-trabajo")
public class OrdenTrabajoController {

    @Autowired
    private IOrdenTrabajoService ordenTrabajoService;

    @Autowired
    private PdfAsyncService pdfAsyncService;

    @Autowired
    private IOrdenTrabajoRepository ordenTrabajoRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrdenTrabajoResponse>>> getAll() {
        List<OrdenTrabajoResponse> ordenes = ordenTrabajoService.findAll();
        ApiResponse<List<OrdenTrabajoResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Órdenes de trabajo obtenidas exitosamente");
        response.setData(ordenes);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrdenTrabajoResponse>> getById(@PathVariable Long id) {
        OrdenTrabajoResponse orden = ordenTrabajoService.findById(id);
        ApiResponse<OrdenTrabajoResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Orden de trabajo obtenida exitosamente");
        response.setData(orden);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ApiResponse<OrdenTrabajoResponse>> getByCodigo(@PathVariable String codigo) {
        OrdenTrabajoResponse orden = ordenTrabajoService.findByCodigo(codigo);
        ApiResponse<OrdenTrabajoResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Orden de trabajo obtenida exitosamente");
        response.setData(orden);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<OrdenTrabajoResponse>>> getByEstado(@PathVariable String estado) {
        List<OrdenTrabajoResponse> ordenes = ordenTrabajoService.findByEstado(estado);
        ApiResponse<List<OrdenTrabajoResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Órdenes de trabajo obtenidas exitosamente");
        response.setData(ordenes);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<ApiResponse<List<OrdenTrabajoResponse>>> getByPrioridad(@PathVariable String prioridad) {
        List<OrdenTrabajoResponse> ordenes = ordenTrabajoService.findByPrioridad(prioridad);
        ApiResponse<List<OrdenTrabajoResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Órdenes de trabajo obtenidas exitosamente");
        response.setData(ordenes);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrdenTrabajoResponse>> create(@RequestBody OrdenTrabajoRequest request) {
        OrdenTrabajoResponse orden = ordenTrabajoService.create(request);
        ApiResponse<OrdenTrabajoResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Orden de trabajo creada exitosamente");
        response.setData(orden);
        response.setStatus(HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrdenTrabajoResponse>> update(@PathVariable Long id, @RequestBody OrdenTrabajoRequest request) {
        OrdenTrabajoResponse orden = ordenTrabajoService.update(id, request);
        ApiResponse<OrdenTrabajoResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Orden de trabajo actualizada exitosamente");
        response.setData(orden);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<OrdenTrabajoResponse>> cambiarEstado(@PathVariable Long id, @RequestBody EstadoUpdateRequest request) {
        OrdenTrabajoResponse orden = ordenTrabajoService.cambiarEstado(id, request.getEstado());
        ApiResponse<OrdenTrabajoResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Estado de orden de trabajo actualizado exitosamente");
        response.setData(orden);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/pdf")
    public ResponseEntity<ApiResponse<String>> generarPdf(@PathVariable Long id) {
        try {
            var orden = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada"));
            
            // Generar PDF de forma asíncrona usando hilos
            pdfAsyncService.generarPdfOrdenTrabajo(orden);
            
            ApiResponse<String> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("PDF de orden de trabajo en generación (proceso asíncrono)");
            response.setData("Generando PDF para orden: " + orden.getCodigo());
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