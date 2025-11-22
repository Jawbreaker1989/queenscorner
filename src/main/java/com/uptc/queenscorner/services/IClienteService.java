package com.uptc.queenscorner.services;

import com.uptc.queenscorner.models.dtos.requests.ClienteRequest;
import com.uptc.queenscorner.models.dtos.responses.ClienteResponse;
import java.util.List;

/**
 * Servicio de clientes
 * Define operaciones CRUD para gestionar clientes del sistema
 * Implementación: ClienteServiceImpl
 */
public interface IClienteService {
    
    /**
     * Obtiene todos los clientes que estén activos
     * @return Lista de clientes con estado activo
     */
    List<ClienteResponse> findAllActive();
    
    /**
     * Busca un cliente específico por su ID
     * @param id Identificador del cliente
     * @return Datos del cliente encontrado
     */
    ClienteResponse findById(Long id);
    
    /**
     * Crea un nuevo cliente en el sistema
     * @param request Datos del cliente (nombre, documento, email, etc)
     * @return Cliente creado con ID asignado
     */
    ClienteResponse create(ClienteRequest request);
    
    /**
     * Actualiza los datos de un cliente existente
     * @param id ID del cliente a actualizar
     * @param request Nuevos datos del cliente
     * @return Cliente actualizado
     */
    ClienteResponse update(Long id, ClienteRequest request);
    
    /**
     * Elimina (desactiva) un cliente
     * @param id ID del cliente a eliminar
     */
    void delete(Long id);
} 