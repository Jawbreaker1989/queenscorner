package com.uptc.queenscorner.mappers;

import com.uptc.queenscorner.dtos.*;
import com.uptc.queenscorner.models.entities.*;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class FacturaMapper {
    
    public FacturaResponse toResponse(FacturaEntity entity) {
        if (entity == null) return null;
        
        FacturaResponse response = new FacturaResponse();
        response.setId(entity.getId());
        response.setNumeroFactura(entity.getNumeroFactura());
        response.setFechaCreacion(entity.getFechaCreacion());
        response.setFechaEnvio(entity.getFechaEnvio());
        response.setEstado(entity.getEstado().toString());
        response.setSubtotal(entity.getSubtotal());
        response.setIva(entity.getIva());
        response.setTotal(entity.getTotal());
        response.setObservaciones(entity.getObservaciones());
        response.setUsuarioCreacion(entity.getUsuarioCreacion());
        response.setUsuarioEnvio(entity.getUsuarioEnvio());
        response.setPathPdf(entity.getPathPdf());
        response.setNegocio(toNegocioInfo(entity.getNegocio()));
        
        entity.getLineas().forEach(linea -> 
            response.getLineas().add(toLineaResponse(linea))
        );
        
        return response;
    }
    
    public LineaFacturaResponse toLineaResponse(LineaFacturaEntity entity) {
        if (entity == null) return null;
        
        LineaFacturaResponse response = new LineaFacturaResponse();
        response.setId(entity.getId());
        response.setNumeroLinea(entity.getNumeroLinea());
        response.setDescripcion(entity.getDescripcion());
        response.setCantidad(BigDecimal.valueOf(entity.getCantidad().doubleValue()));
        response.setValorUnitario(entity.getValorUnitario());
        response.setTotal(entity.getTotal());
        return response;
    }
    
    public NegocioInfoResponse toNegocioInfo(NegocioEntity entity) {
        if (entity == null) return null;
        
        NegocioInfoResponse response = new NegocioInfoResponse();
        response.setId(entity.getId());
        response.setNumero(entity.getCodigo());
        response.setFechaCreacion(entity.getFechaCreacion());
        response.setProyecto(entity.getDescripcionCotizacion());
        response.setTotalCotizacion(entity.getTotalCotizacion());
        response.setAnticipo(entity.getAnticipo());
        
        BigDecimal saldoPendiente = entity.getTotalCotizacion().subtract(entity.getAnticipo());
        response.setSaldoPendiente(saldoPendiente);
        
        if (entity.getCotizacion() != null && entity.getCotizacion().getCliente() != null) {
            ClienteEntity cliente = entity.getCotizacion().getCliente();
            response.setCliente(new ClienteInfoResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getDocumento(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getDireccion()
            ));
        }
        
        return response;
    }
}
