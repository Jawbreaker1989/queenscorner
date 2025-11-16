package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.NegocioRequest;
import com.uptc.queenscorner.models.dtos.responses.NegocioResponse;
import com.uptc.queenscorner.models.entities.NegocioEntity;
import org.springframework.stereotype.Component;

@Component
public class NegocioMapper {

    public NegocioResponse toResponse(NegocioEntity entity) {
        NegocioResponse response = new NegocioResponse();
        response.setId(entity.getId());
        response.setCodigo(entity.getCodigo());
        response.setFechaCreacion(entity.getFechaCreacion());
        response.setEstado(entity.getEstado().name());
        response.setTotalNegocio(entity.getTotalNegocio());
        response.setAnticipo(entity.getAnticipo());
        response.setSaldoPendiente(entity.getSaldoPendiente());
        response.setFechaEntregaEstimada(entity.getFechaEntregaEstimada());
        response.setObservaciones(entity.getObservaciones());
        return response;
    }

    public void updateEntityFromRequest(NegocioRequest request, NegocioEntity entity) {
        if (request.getAnticipo() != null) entity.setAnticipo(request.getAnticipo());
        if (request.getFechaEntregaEstimada() != null) entity.setFechaEntregaEstimada(request.getFechaEntregaEstimada());
        if (request.getObservaciones() != null) entity.setObservaciones(request.getObservaciones());
        
        if (request.getAnticipo() != null && entity.getTotalNegocio() != null) {
            entity.setSaldoPendiente(entity.getTotalNegocio().subtract(request.getAnticipo()));
        }
    }
}