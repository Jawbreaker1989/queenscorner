package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.CotizacionRequest;
import com.uptc.queenscorner.models.dtos.responses.CotizacionResponse;
import com.uptc.queenscorner.models.entities.CotizacionEntity;
import org.springframework.stereotype.Component;

@Component
public class CotizacionMapper {

    public CotizacionResponse toResponse(CotizacionEntity entity) {
        CotizacionResponse response = new CotizacionResponse();
        response.setId(entity.getId());
        response.setCodigo(entity.getCodigo());
        response.setFechaCreacion(entity.getFechaCreacion());
        response.setFechaValidez(entity.getFechaValidez());
        response.setEstado(entity.getEstado().name());
        response.setDescripcion(entity.getDescripcion());
        response.setSubtotal(entity.getSubtotal());
        response.setImpuestos(entity.getImpuestos());
        response.setTotal(entity.getTotal());
        response.setObservaciones(entity.getObservaciones());
        return response;
    }

    public void updateEntityFromRequest(CotizacionRequest request, CotizacionEntity entity) {
        if (request.getFechaValidez() != null) entity.setFechaValidez(request.getFechaValidez());
        if (request.getDescripcion() != null) entity.setDescripcion(request.getDescripcion());
        if (request.getObservaciones() != null) entity.setObservaciones(request.getObservaciones());
    }
}