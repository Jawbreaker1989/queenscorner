package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.OrdenTrabajoRequest;
import com.uptc.queenscorner.models.dtos.responses.OrdenTrabajoResponse;
import com.uptc.queenscorner.models.entities.OrdenTrabajoEntity;
import org.springframework.stereotype.Component;

@Component
public class OrdenTrabajoMapper {

    public OrdenTrabajoEntity toEntity(OrdenTrabajoRequest request) {
        OrdenTrabajoEntity entity = new OrdenTrabajoEntity();
        entity.setDescripcion(request.getDescripcion());
        entity.setPrioridad(OrdenTrabajoEntity.PrioridadOrden.valueOf(request.getPrioridad()));
        entity.setFechaInicioEstimada(request.getFechaInicioEstimada());
        entity.setFechaFinEstimada(request.getFechaFinEstimada());
        entity.setObservaciones(request.getObservaciones());
        return entity;
    }

    public OrdenTrabajoResponse toResponse(OrdenTrabajoEntity entity) {
        OrdenTrabajoResponse response = new OrdenTrabajoResponse();
        response.setId(entity.getId());
        response.setCodigo(entity.getCodigo());
        response.setFechaCreacion(entity.getFechaCreacion());
        response.setEstado(entity.getEstado().name());
        response.setDescripcion(entity.getDescripcion());
        response.setPrioridad(entity.getPrioridad().name());
        response.setFechaInicioEstimada(entity.getFechaInicioEstimada());
        response.setFechaFinEstimada(entity.getFechaFinEstimada());
        response.setFechaEntregaReal(entity.getFechaEntregaReal());
        response.setObservaciones(entity.getObservaciones());
        return response;
    }

    public void updateEntityFromRequest(OrdenTrabajoRequest request, OrdenTrabajoEntity entity) {
        if (request.getDescripcion() != null) entity.setDescripcion(request.getDescripcion());
        if (request.getPrioridad() != null) entity.setPrioridad(OrdenTrabajoEntity.PrioridadOrden.valueOf(request.getPrioridad()));
        if (request.getFechaInicioEstimada() != null) entity.setFechaInicioEstimada(request.getFechaInicioEstimada());
        if (request.getFechaFinEstimada() != null) entity.setFechaFinEstimada(request.getFechaFinEstimada());
        if (request.getObservaciones() != null) entity.setObservaciones(request.getObservaciones());
    }
}