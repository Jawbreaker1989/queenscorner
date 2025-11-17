package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.OrdenTrabajoRequest;
import com.uptc.queenscorner.models.dtos.responses.OrdenTrabajoResponse;
import com.uptc.queenscorner.models.entities.OrdenTrabajoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrdenTrabajoMapper {

    @Autowired
    private NegocioMapper negocioMapper;

    public OrdenTrabajoEntity toEntity(OrdenTrabajoRequest request) {
        OrdenTrabajoEntity entity = new OrdenTrabajoEntity();
        entity.setDescripcion(request.getDescripcion());
        
        // Manejar prioridad null o vacía
        if (request.getPrioridad() != null && !request.getPrioridad().isEmpty()) {
            try {
                entity.setPrioridad(OrdenTrabajoEntity.PrioridadOrden.valueOf(request.getPrioridad().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Si la prioridad no es válida, usar MEDIA como default
                entity.setPrioridad(OrdenTrabajoEntity.PrioridadOrden.MEDIA);
            }
        }
        
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
        response.setRutaPdfNotificacion(entity.getRutaPdfNotificacion());
        
        // ✅ CARGAR NEGOCIO
        if (entity.getNegocio() != null) {
            response.setNegocio(negocioMapper.toResponse(entity.getNegocio()));
        }
        
        return response;
    }

    public void updateEntityFromRequest(OrdenTrabajoRequest request, OrdenTrabajoEntity entity) {
        if (request.getDescripcion() != null && !request.getDescripcion().trim().isEmpty()) {
            entity.setDescripcion(request.getDescripcion());
        }
        
        if (request.getPrioridad() != null && !request.getPrioridad().trim().isEmpty()) {
            try {
                entity.setPrioridad(OrdenTrabajoEntity.PrioridadOrden.valueOf(request.getPrioridad().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Si la prioridad no es válida, usar MEDIA como default
                entity.setPrioridad(OrdenTrabajoEntity.PrioridadOrden.MEDIA);
            }
        }
        
        if (request.getFechaInicioEstimada() != null) {
            entity.setFechaInicioEstimada(request.getFechaInicioEstimada());
        }
        if (request.getFechaFinEstimada() != null) {
            entity.setFechaFinEstimada(request.getFechaFinEstimada());
        }
        if (request.getObservaciones() != null && !request.getObservaciones().trim().isEmpty()) {
            entity.setObservaciones(request.getObservaciones());
        }
    }
}