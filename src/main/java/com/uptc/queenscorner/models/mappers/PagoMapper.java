package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.PagoRequest;
import com.uptc.queenscorner.models.dtos.responses.PagoResponse;
import com.uptc.queenscorner.models.entities.PagoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    @Autowired
    private NegocioMapper negocioMapper;

    public PagoEntity toEntity(PagoRequest request) {
        PagoEntity entity = new PagoEntity();
        entity.setMonto(request.getMonto());
        entity.setMetodoPago(PagoEntity.MetodoPago.valueOf(request.getMetodoPago()));
        entity.setReferencia(request.getReferencia());
        entity.setObservaciones(request.getObservaciones());
        return entity;
    }

    public PagoResponse toResponse(PagoEntity entity) {
        PagoResponse response = new PagoResponse();
        response.setId(entity.getId());
        response.setFechaPago(entity.getFechaPago());
        response.setMonto(entity.getMonto());
        response.setMetodoPago(entity.getMetodoPago().name());
        response.setReferencia(entity.getReferencia());
        response.setObservaciones(entity.getObservaciones());
        
        // ✅ CARGAR NEGOCIO
        if (entity.getNegocio() != null) {
            response.setNegocio(negocioMapper.toResponse(entity.getNegocio()));
        }
        
        return response;
    }
}