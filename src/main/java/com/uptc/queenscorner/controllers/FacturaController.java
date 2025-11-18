package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.models.dtos.requests.CrearFacturaRequest;
import com.uptc.queenscorner.models.dtos.responses.FacturaResponse;
import com.uptc.queenscorner.services.IFacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private IFacturaService facturaService;

    @PostMapping
    public ResponseEntity<FacturaResponse> crearFactura(
            @RequestBody CrearFacturaRequest request,
            Principal principal) {
        FacturaResponse response = facturaService.crearFactura(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/emitir")
    public ResponseEntity<FacturaResponse> emitirFactura(
            @PathVariable Long id,
            Principal principal) {
        FacturaResponse response = facturaService.emitirFactura(id, principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponse> obtenerFactura(@PathVariable Long id) {
        FacturaResponse response = facturaService.obtenerFactura(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FacturaResponse>> listarFacturas() {
        List<FacturaResponse> response = facturaService.listarFacturas();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/negocio/{negocioId}")
    public ResponseEntity<List<FacturaResponse>> listarPorNegocio(@PathVariable Long negocioId) {
        List<FacturaResponse> response = facturaService.listarPorNegocio(negocioId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/estado/{estado}")
    public ResponseEntity<FacturaResponse> cambiarEstado(
            @PathVariable Long id,
            @PathVariable String estado,
            Principal principal) {
        FacturaResponse response = facturaService.cambiarEstado(id, estado, principal.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> anularFactura(
            @PathVariable Long id,
            Principal principal) {
        facturaService.anularFactura(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
