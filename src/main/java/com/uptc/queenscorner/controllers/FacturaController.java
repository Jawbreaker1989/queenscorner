package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.models.dtos.requests.FacturaRequest;
import com.uptc.queenscorner.models.dtos.requests.EstadoUpdateRequest;
import com.uptc.queenscorner.models.dtos.responses.ApiResponse;
import com.uptc.queenscorner.models.dtos.responses.FacturaResponse;
import com.uptc.queenscorner.services.IFacturaService;
import com.uptc.queenscorner.services.async.PdfAsyncService;
import com.uptc.queenscorner.repositories.IFacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private IFacturaService facturaService;

    @Autowired
    private PdfAsyncService pdfAsyncService;

    @Autowired
    private IFacturaRepository facturaRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FacturaResponse>>> getAll() {
        List<FacturaResponse> facturas = facturaService.findAll();
        ApiResponse<List<FacturaResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Facturas obtenidas exitosamente");
        response.setData(facturas);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FacturaResponse>> getById(@PathVariable Long id) {
        FacturaResponse factura = facturaService.findById(id);
        ApiResponse<FacturaResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Factura obtenida exitosamente");
        response.setData(factura);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ApiResponse<FacturaResponse>> getByCodigo(@PathVariable String codigo) {
        FacturaResponse factura = facturaService.findByCodigo(codigo);
        ApiResponse<FacturaResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Factura obtenida exitosamente");
        response.setData(factura);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<FacturaResponse>>> getByEstado(@PathVariable String estado) {
        List<FacturaResponse> facturas = facturaService.findByEstado(estado);
        ApiResponse<List<FacturaResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Facturas obtenidas exitosamente");
        response.setData(facturas);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FacturaResponse>> create(@RequestBody FacturaRequest request) {
        FacturaResponse factura = facturaService.create(request);
        ApiResponse<FacturaResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Factura creada exitosamente");
        response.setData(factura);
        response.setStatus(HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<FacturaResponse>> cambiarEstado(@PathVariable Long id, @RequestBody EstadoUpdateRequest request) {
        FacturaResponse factura = facturaService.cambiarEstado(id, request.getEstado());
        ApiResponse<FacturaResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Estado de factura actualizado exitosamente");
        response.setData(factura);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/pdf")
    public ResponseEntity<ApiResponse<String>> generarPdf(@PathVariable Long id) {
        try {
            var factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
            
            // Generar PDF de forma asíncrona usando hilos
            pdfAsyncService.generarFacturaPdfAsync(factura);
            
            ApiResponse<String> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("PDF de factura en generación (proceso asíncrono)");
            response.setData("Generando PDF para factura: " + factura.getCodigo());
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