package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.models.dtos.requests.PagoRequest;
import com.uptc.queenscorner.models.dtos.responses.ApiResponse;
import com.uptc.queenscorner.models.dtos.responses.PagoResponse;
import com.uptc.queenscorner.services.IPagoService;
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
}