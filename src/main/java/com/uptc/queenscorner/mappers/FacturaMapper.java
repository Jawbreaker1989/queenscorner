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
        response.setEstado(entity.getEstado() != null ? entity.getEstado() : "ENVIADA");
        response.setSubtotal(entity.getSubtotal());
        response.setIva(entity.getIva());
        response.setTotal(entity.getTotal());
        response.setObservaciones(entity.getObservaciones());
        response.setUsuarioCreacion(entity.getUsuarioCreacion());
        response.setUsuarioEnvio(entity.getUsuarioEnvio());
        response.setPathPdf(entity.getPathPdf());
        response.setNegocio(toNegocioInfo(entity.getNegocio()));
        
        if (entity.getLineas() != null) {
            entity.getLineas().forEach(linea -> 
                response.getLineas().add(toLineaResponse(linea))
            );
        }
        
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
        response.setProyecto(entity.getDescripcion());
        
        // Obtener totales de la cotización si existe
        if (entity.getCotizacion() != null) {
            CotizacionEntity cot = entity.getCotizacion();
            response.setTotalCotizacion(cot.getTotal());
            response.setAnticipo(entity.getAnticipo());
            
            if (cot.getTotal() != null && entity.getAnticipo() != null) {
                BigDecimal saldoPendiente = cot.getTotal().subtract(entity.getAnticipo());
                response.setSaldoPendiente(saldoPendiente);
            }
            
            if (cot.getCliente() != null) {
                ClienteEntity cliente = cot.getCliente();
                response.setCliente(new ClienteInfoResponse(
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getDocumento(),
                    cliente.getEmail(),
                    cliente.getTelefono(),
                    cliente.getDireccion()
                ));
            }
        }
        
        return response;
    }
} 
