package com.uptc.queenscorner.controllers;

import com.uptc.queenscorner.models.dtos.requests.ClienteRequest;
import com.uptc.queenscorner.models.dtos.responses.ApiResponse;
import com.uptc.queenscorner.models.dtos.responses.ClienteResponse;
import com.uptc.queenscorner.services.IClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para gestionar clientes
 * Proporciona endpoints CRUD para crear, leer, actualizar y eliminar clientes
 * Todos los datos se retornan en formato JSON con estructura ApiResponse
 */
@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
@Tag(name = "Clientes", description = "Gestión de clientes del sistema")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Obtiene todos los clientes activos
     * Los resultados se recuperan desde caché para mejor rendimiento
     * @return Lista de clientes con estado activo
     */
    @GetMapping
    @Operation(summary = "Listar clientes activos", description = "Obtiene el listado de todos los clientes activos del sistema")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado de clientes obtenido exitosamente")
    })
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> getAll() {
        List<ClienteResponse> clientes = clienteService.findAllActive();
        ApiResponse<List<ClienteResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Clientes obtenidos exitosamente");
        response.setData(clientes);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un cliente específico por su ID
     * Si el cliente no existe o está inactivo, lanza excepción 404
     * @param id Identificador único del cliente
     * @return Datos completos del cliente encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID", description = "Recupera los detalles de un cliente específico")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cliente obtenido exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ApiResponse<ClienteResponse>> getById(
            @PathVariable @Parameter(description = "ID del cliente") Long id) {
        ClienteResponse cliente = clienteService.findById(id);
        ApiResponse<ClienteResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cliente obtenido exitosamente");
        response.setData(cliente);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    /**
     * Crea un nuevo cliente en el sistema
     * Valida que los datos sean válidos antes de guardar
     * Los clientes se crean con estado activo por defecto
     * @param request Objeto con nombre, documento, email, teléfono, dirección
     * @return Cliente creado con ID asignado automáticamente
     */
    @PostMapping
    @Operation(summary = "Crear nuevo cliente", description = "Crea un nuevo cliente en el sistema")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Cliente creado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud")
    })
    public ResponseEntity<ApiResponse<ClienteResponse>> create(@RequestBody ClienteRequest request) {
        ClienteResponse cliente = clienteService.create(request);
        ApiResponse<ClienteResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cliente creado exitosamente");
        response.setData(cliente);
        response.setStatus(HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza los datos de un cliente existente
     * Solo actualiza los campos enviados en la solicitud
     * Invalida la caché de clientes después de actualizar
     * @param id ID del cliente a modificar
     * @param request Nuevos datos del cliente
     * @return Cliente actualizado con los cambios aplicados
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<ApiResponse<ClienteResponse>> update(
            @PathVariable @Parameter(description = "ID del cliente") Long id,
            @RequestBody ClienteRequest request) {
        ClienteResponse cliente = clienteService.update(id, request);
        ApiResponse<ClienteResponse> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cliente actualizado exitosamente");
        response.setData(cliente);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina (desactiva) un cliente del sistema
     * NOTA: Es una eliminación lógica - los datos permanecen en BD para auditoría
     * El cliente se marca como inactivo y no aparecerá en listados normales
     * @param id ID del cliente a eliminar
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente", description = "Marca un cliente como inactivo (eliminación lógica)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cliente eliminado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Parameter(description = "ID del cliente") Long id) {
        clienteService.delete(id);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Cliente eliminado exitosamente");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
} 