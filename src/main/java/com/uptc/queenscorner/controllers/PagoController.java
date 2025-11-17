package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.models.dtos.requests.PagoRequest;
import com.uptc.queenscorner.models.dtos.responses.ApiResponse;
import com.uptc.queenscorner.models.dtos.responses.PagoResponse;
import com.uptc.queenscorner.services.IPagoService;
import com.uptc.queenscorner.services.async.PdfAsyncService;
import com.uptc.queenscorner.repositories.IPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private IPagoService pagoService;

    @Autowired
    private PdfAsyncService pdfAsyncService;

    @Autowired
    private IPagoRepository pagoRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PagoResponse>>> getAll() {
        List<PagoResponse> pagos = pagoService.findAll();
        ApiResponse<List<PagoResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Pagos obtenidos exitosamente");
        response.setData(pagos);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PagoResponse>> getById(@PathVariable Long id) {
        PagoResponse pago = pagoService.findById(id);
        ApiResponse<PagoResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Pago obtenido exitosamente");
        response.setData(pago);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/negocio/{negocioId}")
    public ResponseEntity<ApiResponse<List<PagoResponse>>> getByNegocioId(@PathVariable Long negocioId) {
        List<PagoResponse> pagos = pagoService.findByNegocioId(negocioId);
        ApiResponse<List<PagoResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Pagos obtenidos exitosamente");
        response.setData(pagos);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PagoResponse>> create(@RequestBody PagoRequest request) {
        PagoResponse pago = pagoService.create(request);
        ApiResponse<PagoResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Pago registrado exitosamente");
        response.setData(pago);
        response.setStatus(HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        pagoService.delete(id);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Pago eliminado exitosamente");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/pdf")
    public ResponseEntity<ApiResponse<String>> generarComprobante(@PathVariable Long id) {
        try {
            var pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
            
            // Generar PDF de comprobante de pago de forma asíncrona
            pdfAsyncService.generarPdfComprobantePago(pago);
            
            ApiResponse<String> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("PDF de comprobante en generación (proceso asíncrono)");
            response.setData("Generando comprobante de pago ID: " + pago.getId());
            response.setStatus(HttpStatus.ACCEPTED.value());
            return ResponseEntity.accepted().body(response);
            
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("Error al generar comprobante: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}