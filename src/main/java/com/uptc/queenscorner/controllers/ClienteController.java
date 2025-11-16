package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.models.dtos.requests.ClienteRequest;
import com.uptc.queenscorner.models.dtos.responses.ApiResponse;
import com.uptc.queenscorner.models.dtos.responses.ClienteResponse;
import com.uptc.queenscorner.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> getAll() {
        List<ClienteResponse> clientes = clienteService.findAllActive();
        ApiResponse<List<ClienteResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Clientes obtenidos exitosamente");
        response.setData(clientes);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> getById(@PathVariable Long id) {
        ClienteResponse cliente = clienteService.findById(id);
        ApiResponse<ClienteResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cliente obtenido exitosamente");
        response.setData(cliente);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponse>> create(@RequestBody ClienteRequest request) {
        ClienteResponse cliente = clienteService.create(request);
        ApiResponse<ClienteResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cliente creado exitosamente");
        response.setData(cliente);
        response.setStatus(HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> update(@PathVariable Long id, @RequestBody ClienteRequest request) {
        ClienteResponse cliente = clienteService.update(id, request);
        ApiResponse<ClienteResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cliente actualizado exitosamente");
        response.setData(cliente);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        clienteService.delete(id);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cliente eliminado exitosamente");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
}