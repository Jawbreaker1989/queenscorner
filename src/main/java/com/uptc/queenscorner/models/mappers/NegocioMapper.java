package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.NegocioRequest;
import com.uptc.queenscorner.models.dtos.responses.NegocioResponse;
import com.uptc.queenscorner.models.entities.NegocioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class NegocioMapper {

    @Autowired
    private CotizacionMapper cotizacionMapper;

    public NegocioResponse toResponse(NegocioEntity entity) {
        NegocioResponse response = new NegocioResponse();
        response.setId(entity.getId());
        response.setCodigo(entity.getCodigo());
        response.setFechaCreacion(entity.getFechaCreacion());
        response.setEstado(entity.getEstado().name());
        response.setDescripcion(entity.getDescripcion());
        response.setTotalNegocio(entity.getTotalNegocio());
        response.setAnticipo(entity.getAnticipo());
        response.setSaldoPendiente(entity.getSaldoPendiente());
        response.setFechaEntregaEstimada(entity.getFechaEntregaEstimada());
        response.setObservaciones(entity.getObservaciones());
        
        if (entity.getCotizacion() != null) {
            response.setCotizacion(cotizacionMapper.toResponse(entity.getCotizacion()));
        }
        
        return response;
    }

    public void updateEntityFromRequest(NegocioRequest request, NegocioEntity entity) {
        if (request.getDescripcion() != null && !request.getDescripcion().trim().isEmpty()) {
            entity.setDescripcion(request.getDescripcion());
        }
        if (request.getAnticipo() != null) {
            entity.setAnticipo(request.getAnticipo());
        }
        if (request.getFechaEntregaEstimada() != null) {
            entity.setFechaEntregaEstimada(request.getFechaEntregaEstimada());
        }
        if (request.getObservaciones() != null && !request.getObservaciones().trim().isEmpty()) {
            entity.setObservaciones(request.getObservaciones());
        }
        
        // Recalcular saldo pendiente si se actualiza el anticipo
        if (request.getAnticipo() != null && entity.getTotalNegocio() != null) {
            BigDecimal saldoPendiente = entity.getTotalNegocio().subtract(request.getAnticipo());
            entity.setSaldoPendiente(saldoPendiente.max(BigDecimal.ZERO)); // No permitir saldos negativos
        }
    }
}