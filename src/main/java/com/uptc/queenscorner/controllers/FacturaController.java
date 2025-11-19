package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.models.dtos.requests.CrearFacturaRequest;
import com.uptc.queenscorner.models.dtos.responses.FacturaResponse;
import com.uptc.queenscorner.services.IFacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Operation(summary = "Crear nueva factura", description = "Crea una nueva factura a partir de un negocio y cotización")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Factura creada exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = FacturaResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
        @ApiResponse(responseCode = "404", description = "Negocio o cotización no encontrados"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    public ResponseEntity<FacturaResponse> crearFactura(
            @RequestBody CrearFacturaRequest request,
            Principal principal) {
        FacturaResponse response = facturaService.crearFactura(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/emitir")
    @Operation(summary = "Emitir factura", description = "Cambia el estado de la factura a EMITIDA y genera el PDF de forma asincrónica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura emitida exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = FacturaResponse.class))),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada"),
        @ApiResponse(responseCode = "400", description = "Factura no puede ser emitida en este estado")
    })
    public ResponseEntity<FacturaResponse> emitirFactura(
            @PathVariable @Parameter(description = "ID de la factura") Long id,
            Principal principal) {
        FacturaResponse response = facturaService.emitirFactura(id, principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener factura por ID", description = "Recupera los detalles de una factura específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura obtenida exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = FacturaResponse.class))),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    public ResponseEntity<FacturaResponse> obtenerFactura(
            @PathVariable @Parameter(description = "ID de la factura") Long id) {
        FacturaResponse response = facturaService.obtenerFactura(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas las facturas", description = "Obtiene el listado completo de facturas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de facturas obtenido exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = FacturaResponse.class)))
    })
    public ResponseEntity<List<FacturaResponse>> listarFacturas() {
        List<FacturaResponse> response = facturaService.listarFacturas();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/negocio/{negocioId}")
    @Operation(summary = "Listar facturas por negocio", description = "Obtiene todas las facturas asociadas a un negocio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de facturas obtenido exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = FacturaResponse.class))),
        @ApiResponse(responseCode = "404", description = "Negocio no encontrado")
    })
    public ResponseEntity<List<FacturaResponse>> listarPorNegocio(
            @PathVariable @Parameter(description = "ID del negocio") Long negocioId) {
        List<FacturaResponse> response = facturaService.listarPorNegocio(negocioId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/estado/{estado}")
    @Operation(summary = "Cambiar estado de factura", description = "Actualiza el estado de una factura (EMITIDA, ENVIADA, PAGADA, ANULADA)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = FacturaResponse.class))),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada"),
        @ApiResponse(responseCode = "400", description = "Estado inválido")
    })
    public ResponseEntity<FacturaResponse> cambiarEstado(
            @PathVariable @Parameter(description = "ID de la factura") Long id,
            @PathVariable @Parameter(description = "Nuevo estado") String estado,
            Principal principal) {
        FacturaResponse response = facturaService.cambiarEstado(id, estado, principal.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Anular factura", description = "Cambia el estado de la factura a ANULADA (solo si no está pagada)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Factura anulada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada"),
        @ApiResponse(responseCode = "400", description = "No se puede anular una factura pagada")
    })
    public ResponseEntity<Void> anularFactura(
            @PathVariable @Parameter(description = "ID de la factura") Long id,
            Principal principal) {
        facturaService.anularFactura(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
