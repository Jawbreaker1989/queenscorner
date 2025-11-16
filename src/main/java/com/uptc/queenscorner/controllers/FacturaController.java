package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.models.dtos.requests.FacturaRequest;
import com.uptc.queenscorner.models.dtos.requests.EstadoUpdateRequest;
import com.uptc.queenscorner.models.dtos.responses.ApiResponse;
import com.uptc.queenscorner.models.dtos.responses.FacturaResponse;
import com.uptc.queenscorner.services.IFacturaService;
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
}