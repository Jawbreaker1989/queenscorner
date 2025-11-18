package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.models.dtos.requests.NegocioRequest;
import com.uptc.queenscorner.models.dtos.requests.EstadoUpdateRequest;
import com.uptc.queenscorner.models.dtos.responses.ApiResponse;
import com.uptc.queenscorner.models.dtos.responses.NegocioResponse;
import com.uptc.queenscorner.services.INegocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/negocios")
public class NegocioController {

    @Autowired
    private INegocioService negocioService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NegocioResponse>>> getAll() {
        List<NegocioResponse> negocios = negocioService.findAll();
        ApiResponse<List<NegocioResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Negocios obtenidos exitosamente");
        response.setData(negocios);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NegocioResponse>> getById(@PathVariable Long id) {
        NegocioResponse negocio = negocioService.findById(id);
        ApiResponse<NegocioResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Negocio obtenido exitosamente");
        response.setData(negocio);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ApiResponse<NegocioResponse>> getByCodigo(@PathVariable String codigo) {
        NegocioResponse negocio = negocioService.findByCodigo(codigo);
        ApiResponse<NegocioResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Negocio obtenido exitosamente");
        response.setData(negocio);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<NegocioResponse>>> getByEstado(@PathVariable String estado) {
        List<NegocioResponse> negocios = negocioService.findByEstado(estado);
        ApiResponse<List<NegocioResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Negocios obtenidos exitosamente");
        response.setData(negocios);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NegocioResponse>> create(@RequestBody NegocioRequest request) {
        NegocioResponse negocio = negocioService.create(request);
        ApiResponse<NegocioResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Negocio creado exitosamente");
        response.setData(negocio);
        response.setStatus(HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/desde-cotizacion/{cotizacionId}")
    public ResponseEntity<ApiResponse<NegocioResponse>> crearDesdeAprobada(@PathVariable Long cotizacionId, @RequestBody NegocioRequest request) {
        request.setCotizacionId(cotizacionId);
        NegocioResponse negocio = negocioService.crearDesdeAprobada(cotizacionId, request);
        ApiResponse<NegocioResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Negocio creado exitosamente desde cotización aprobada");
        response.setData(negocio);
        response.setStatus(HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NegocioResponse>> update(@PathVariable Long id, @RequestBody NegocioRequest request) {
        NegocioResponse negocio = negocioService.update(id, request);
        ApiResponse<NegocioResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Negocio actualizado exitosamente");
        response.setData(negocio);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<NegocioResponse>> cambiarEstado(@PathVariable Long id, @RequestBody EstadoUpdateRequest request) {
        NegocioResponse negocio = negocioService.cambiarEstado(id, request.getEstado());
        ApiResponse<NegocioResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Estado de negocio actualizado exitosamente");
        response.setData(negocio);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
}