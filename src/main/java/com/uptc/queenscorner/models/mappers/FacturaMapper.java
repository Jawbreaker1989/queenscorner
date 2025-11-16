package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.FacturaRequest;
import com.uptc.queenscorner.models.dtos.responses.FacturaResponse;
import com.uptc.queenscorner.models.entities.FacturaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FacturaMapper {

    @Autowired
    private NegocioMapper negocioMapper;

    public FacturaResponse toResponse(FacturaEntity entity) {
        FacturaResponse response = new FacturaResponse();
        response.setId(entity.getId());
        response.setCodigo(entity.getCodigo());
        response.setFechaEmision(entity.getFechaEmision());
        response.setSubtotal(entity.getSubtotal());
        response.setImpuestos(entity.getImpuestos());
        response.setTotal(entity.getTotal());
        response.setEstado(entity.getEstado().name());
        response.setPdfPath(entity.getPdfPath());
        response.setFechaEnvio(entity.getFechaEnvio());
        response.setObservaciones(entity.getObservaciones());
        
        // ✅ CARGAR NEGOCIO
        if (entity.getNegocio() != null) {
            response.setNegocio(negocioMapper.toResponse(entity.getNegocio()));
        }
        
        return response;
    }

    public void updateEntityFromRequest(FacturaRequest request, FacturaEntity entity) {
        if (request.getObservaciones() != null) {
            entity.setObservaciones(request.getObservaciones());
        }
    }
}