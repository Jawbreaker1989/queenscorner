package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.ClienteRequest;
import com.uptc.queenscorner.models.dtos.responses.ClienteResponse;
import com.uptc.queenscorner.models.entities.ClienteEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre ClienteRequest, ClienteEntity y ClienteResponse.
 * 
 * Responsabilidades:
 * - Convertir DTOs de entrada a entidades de BD (toEntity)
 * - Convertir entidades de BD a DTOs de salida (toResponse)
 * - Actualizar entidades desde DTOs sin sobrescribir campos nulos (updateEntityFromRequest)
 * 
 * El patrón Mapper separa la lógica de transformación de datos de la lógica de negocio.
 */
@Component
public class ClienteMapper {

    /**
     * Convierte un ClienteRequest en una ClienteEntity para persistir en BD.
     * Solo copia los campos básicos del cliente.
     * 
     * @param request DTO con los datos del cliente desde el cliente web
     * @return Entidad con los datos preparada para guardar en BD
     */
    public ClienteEntity toEntity(ClienteRequest request) {
        ClienteEntity entity = new ClienteEntity();
        entity.setNombre(request.getNombre());
        entity.setEmail(request.getEmail());
        entity.setTelefono(request.getTelefono());
        entity.setDireccion(request.getDireccion());
        entity.setCiudad(request.getCiudad());
        return entity;
    }

    /**
     * Convierte una ClienteEntity en un ClienteResponse para enviar al cliente.
     * Incluye información de auditoría (fechaCreacion, estado).
     * 
     * @param entity Entidad de BD
     * @return DTO con los datos para enviar al cliente
     */
    public ClienteResponse toResponse(ClienteEntity entity) {
        ClienteResponse response = new ClienteResponse();
        response.setId(entity.getId());
        response.setNombre(entity.getNombre());
        response.setEmail(entity.getEmail());
        response.setTelefono(entity.getTelefono());
        response.setDireccion(entity.getDireccion());
        response.setCiudad(entity.getCiudad());
        response.setFechaCreacion(entity.getFechaCreacion());
        response.setEstado(entity.getActivo() ? "ACTIVO" : "INACTIVO");
        return response;
    }

    /**
     * Actualiza una ClienteEntity existente con datos de un ClienteRequest.
     * Solo actualiza los campos no nulos, respetando los valores existentes.
     * Útil para actualizaciones parciales (PATCH).
     * 
     * @param request DTO con los nuevos datos
     * @param entity Entidad existente a actualizar
     */
    public void updateEntityFromRequest(ClienteRequest request, ClienteEntity entity) {
        if (request.getNombre() != null) entity.setNombre(request.getNombre());
        if (request.getEmail() != null) entity.setEmail(request.getEmail());
        if (request.getTelefono() != null) entity.setTelefono(request.getTelefono());
        if (request.getDireccion() != null) entity.setDireccion(request.getDireccion());
        if (request.getCiudad() != null) entity.setCiudad(request.getCiudad());
    }
} 