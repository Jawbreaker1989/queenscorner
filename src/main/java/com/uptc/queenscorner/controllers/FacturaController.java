package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.dtos.AgregarLineaRequest;
import com.uptc.queenscorner.dtos.CrearFacturaRequest;
import com.uptc.queenscorner.dtos.FacturaResponse;
import com.uptc.queenscorner.services.IFacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@Tag(name = "Facturas", description = "Gestión de facturas del sistema")
public class FacturaController {

    @Autowired
    private IFacturaService facturaService;

    @PostMapping
    @Operation(summary = "Crear nueva factura")
    public ResponseEntity<FacturaResponse> crearFactura(
            @Valid @RequestBody CrearFacturaRequest request,
            Principal principal) {
        FacturaResponse response = facturaService.crearFactura(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/lineas")
    @Operation(summary = "Agregar línea a factura")
    public ResponseEntity<FacturaResponse> agregarLinea(
            @PathVariable Long id,
            @RequestBody AgregarLineaRequest request) {
        FacturaResponse response = facturaService.agregarLinea(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{facturaId}/lineas/{lineaId}")
    @Operation(summary = "Remover línea de factura")
    public ResponseEntity<FacturaResponse> removerLinea(
            @PathVariable Long facturaId,
            @PathVariable Long lineaId) {
        FacturaResponse response = facturaService.removerLinea(facturaId, lineaId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/enviar")
    @Operation(summary = "Enviar factura")
    public ResponseEntity<FacturaResponse> enviarFactura(
            @PathVariable Long id,
            Principal principal) {
        FacturaResponse response = facturaService.enviarFactura(id, principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener factura por ID")
    public ResponseEntity<FacturaResponse> obtenerFactura(
            @PathVariable Long id) {
        FacturaResponse response = facturaService.obtenerFactura(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas las facturas")
    public ResponseEntity<List<FacturaResponse>> listarFacturas() {
        List<FacturaResponse> response = facturaService.listarFacturas();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/negocio/{negocioId}")
    @Operation(summary = "Listar facturas por negocio")
    public ResponseEntity<List<FacturaResponse>> listarPorNegocio(
            @PathVariable Long negocioId) {
        List<FacturaResponse> response = facturaService.listarPorNegocio(negocioId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/resumen")
    @Operation(summary = "Obtener resumen de factura")
    public ResponseEntity<FacturaResponse> obtenerResumen(
            @PathVariable Long id) {
        FacturaResponse response = facturaService.obtenerResumen(id);
        return ResponseEntity.ok(response);
    }
}
