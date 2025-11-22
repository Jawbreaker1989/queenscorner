package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.models.dtos.requests.AgregarLineaRequest;
import com.uptc.queenscorner.models.dtos.requests.CrearFacturaRequest;
import com.uptc.queenscorner.models.dtos.responses.FacturaResponse;
import com.uptc.queenscorner.services.IFacturaService;
import com.uptc.queenscorner.services.async.PdfAsyncService;
import com.uptc.queenscorner.repositories.IFacturaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * Controlador REST para gestionar facturas (invoices)
 * Maneja creación, líneas, envío y generación de PDF de facturas
 * Las facturas se crean a partir de negocios finalizados
 * Incluye cálculo automático de totales, IVA y saldo pendiente
 */
@RestController
@RequestMapping("/api/facturas")
@Tag(name = "Facturas", description = "Gestión de facturas del sistema")
public class FacturaController {

    @Autowired
    private IFacturaService facturaService;

    @Autowired
    private PdfAsyncService pdfAsyncService;

    @Autowired
    private IFacturaRepository facturaRepository;

    /**
     * Crea una nueva factura para un negocio
     * Valida que el negocio exista y tenga cotización asociada
     * @param request Datos de la factura (negocio, líneas, etc)
     * @param principal Usuario autenticado que crea la factura
     * @return Factura creada con número generado y totales calculados
     */
    @PostMapping
    @Operation(summary = "Crear nueva factura")
    public ResponseEntity<FacturaResponse> crearFactura(
            @Valid @RequestBody CrearFacturaRequest request,
            Principal principal) {
        FacturaResponse response = facturaService.crearFactura(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Agrega una línea de producto/servicio a una factura
     * Solo permite agregar si factura está en estado EN_REVISION
     * Recalcula totales automáticamente
     * @param id ID de la factura
     * @param request Datos de la línea (descripción, cantidad, valor)
     * @return Factura actualizada con la línea y totales recalculados
     */
    @PostMapping("/{id}/lineas")
    @Operation(summary = "Agregar línea a factura")
    public ResponseEntity<FacturaResponse> agregarLinea(
            @PathVariable Long id,
            @RequestBody AgregarLineaRequest request) {
        FacturaResponse response = facturaService.agregarLinea(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Remueve una línea de la factura
     * Recalcula totales e IVA automáticamente
     * @param facturaId ID de la factura
     * @param lineaId ID de la línea a remover
     * @return Factura sin la línea y totales actualizados
     */
    @DeleteMapping("/{facturaId}/lineas/{lineaId}")
    @Operation(summary = "Remover línea de factura")
    public ResponseEntity<FacturaResponse> removerLinea(
            @PathVariable Long facturaId,
            @PathVariable Long lineaId) {
        FacturaResponse response = facturaService.removerLinea(facturaId, lineaId);
        return ResponseEntity.ok(response);
    }

    /**
     * Envía/emite una factura al cliente
     * Valida que tenga al menos una línea
     * Cambia estado a ENVIADA y genera PDF asincronamente
     * @param id ID de la factura
     * @param principal Usuario que envía (para auditoría)
     * @return Factura con estado ENVIADA
     */
    @PostMapping("/{id}/enviar")
    @Operation(summary = "Enviar factura")
    public ResponseEntity<FacturaResponse> enviarFactura(
            @PathVariable Long id,
            Principal principal) {
        FacturaResponse response = facturaService.enviarFactura(id, principal.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene una factura por su ID
     * @param id ID de la factura
     * @return Datos completos de la factura con líneas y cálculos
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener factura por ID")
    public ResponseEntity<FacturaResponse> obtenerFactura(
            @PathVariable Long id) {
        FacturaResponse response = facturaService.obtenerFactura(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todas las facturas del sistema
     * Incluye facturas en todos los estados
     * @return Lista completa de facturas
     */
    @GetMapping
    @Operation(summary = "Listar todas las facturas")
    public ResponseEntity<List<FacturaResponse>> listarFacturas() {
        List<FacturaResponse> response = facturaService.listarFacturas();
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todas las facturas de un negocio específico
     * Útil para seguimiento de facturas por proyecto
     * @param negocioId ID del negocio
     * @return Lista de facturas del negocio
     */
    @GetMapping("/negocio/{negocioId}")
    @Operation(summary = "Listar facturas por negocio")
    public ResponseEntity<List<FacturaResponse>> listarPorNegocio(
            @PathVariable Long negocioId) {
        List<FacturaResponse> response = facturaService.listarPorNegocio(negocioId);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un resumen de la factura con cálculos totales
     * Incluye: subtotal, IVA (19%), total, saldo pendiente
     * @param id ID de la factura
     * @return Factura con datos de resumen calculados
     */
    @GetMapping("/{id}/resumen")
    @Operation(summary = "Obtener resumen de factura")
    public ResponseEntity<FacturaResponse> obtenerResumen(
            @PathVariable Long id) {
        FacturaResponse response = facturaService.obtenerResumen(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Inicia generación asincrónica de PDF para la factura
     * El PDF se genera en background sin bloquear la respuesta HTTP
     * Se almacena en: queenscornerarchives/facturas/
     * @param id ID de la factura
     * @return Factura con indicador de generación en progreso
     */
    @PostMapping("/{id}/generar-pdf")
    @Operation(summary = "Generar PDF de factura")
    public ResponseEntity<FacturaResponse> generarPdf(
            @PathVariable Long id) {
        FacturaResponse response = facturaService.obtenerFactura(id);
        // Obtener la entidad para pasar al servicio de PDF
        var factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        // Generar PDF en background
        pdfAsyncService.generarPdfFacturaAsync(factura);
        return ResponseEntity.accepted().body(response);
    }

    /**
     * Descarga el PDF generado de una factura
     * El PDF debe haber sido generado previamente
     * @param id ID de la factura
     * @return Archivo PDF para descargar
     * @throws IOException si el archivo no existe o no puede leerse
     */
    @GetMapping("/{id}/pdf")
    @Operation(summary = "Descargar PDF de factura")
    public ResponseEntity<byte[]> descargarPdf(
            @PathVariable Long id) throws IOException {
        FacturaResponse factura = facturaService.obtenerFactura(id);
        Path pdfPath = Paths.get(factura.getPathPdf());
        byte[] pdfContent = Files.readAllBytes(pdfPath);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", pdfPath.getFileName().toString());
        
        return ResponseEntity.ok().headers(headers).body(pdfContent);
    }
}