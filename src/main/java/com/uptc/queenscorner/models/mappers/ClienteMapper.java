package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.ClienteRequest;
import com.uptc.queenscorner.models.dtos.responses.ClienteResponse;
import com.uptc.queenscorner.models.entities.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteEntity toEntity(ClienteRequest request) {
        ClienteEntity entity = new ClienteEntity();
        entity.setNombre(request.getNombre());
        entity.setEmail(request.getEmail());
        entity.setTelefono(request.getTelefono());
        entity.setDireccion(request.getDireccion());
        entity.setCiudad(request.getCiudad());
        return entity;
    }

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

    public void updateEntityFromRequest(ClienteRequest request, ClienteEntity entity) {
        if (request.getNombre() != null) entity.setNombre(request.getNombre());
        if (request.getEmail() != null) entity.setEmail(request.getEmail());
        if (request.getTelefono() != null) entity.setTelefono(request.getTelefono());
        if (request.getDireccion() != null) entity.setDireccion(request.getDireccion());
        if (request.getCiudad() != null) entity.setCiudad(request.getCiudad());
    }
}