package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.NegocioRequest;
import com.uptc.queenscorner.models.dtos.responses.ClienteResponse;
import com.uptc.queenscorner.models.dtos.responses.NegocioResponse;
import com.uptc.queenscorner.models.entities.NegocioEntity;
import com.uptc.queenscorner.models.entities.CotizacionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NegocioMapper {

    @Autowired
    private ClienteMapper clienteMapper;
    
    @Autowired
    private CotizacionMapper cotizacionMapper;

    public NegocioResponse toResponse(NegocioEntity entity) {
        if (entity == null) {
            return null;
        }
        
        NegocioResponse response = new NegocioResponse();
        
   
        response.setId(entity.getId());
        response.setCodigo(entity.getCodigo());
        response.setFechaCreacion(entity.getFechaCreacion());
        response.setFechaActualizacion(entity.getFechaActualizacion());
        
        // DATOS  DE COTIZACION 
        response.setCodigoCotizacion(entity.getCodigoCotizacion());
        response.setEstadoCotizacion(entity.getEstadoCotizacion());
        response.setFechaCotizacion(entity.getFechaCotizacion());
        response.setFechaValidezCotizacion(entity.getFechaValidezCotizacion());
        response.setDescripcionCotizacion(entity.getDescripcionCotizacion());
        response.setSubtotalCotizacion(entity.getSubtotalCotizacion());
        response.setImpuestosCotizacion(entity.getImpuestosCotizacion());
        response.setTotalCotizacion(entity.getTotalCotizacion());
        response.setObservacionesCotizacion(entity.getObservacionesCotizacion());
        
        // DATOS EDITABLES DEL NEGOCIO 
        response.setFechaInicio(entity.getFechaInicio());
        response.setFechaFinEstimada(entity.getFechaFinEstimada());
        response.setEstado(entity.getEstado() != null ? entity.getEstado().name() : "EN_REVISION");
        response.setPresupuestoAsignado(entity.getPresupuestoAsignado());
        response.setAnticipo(entity.getAnticipo());
        if (entity.getTotalCotizacion() != null && entity.getAnticipo() != null) {
            response.setSaldoPendiente(entity.getTotalCotizacion().subtract(entity.getAnticipo()));
        } else if (entity.getTotalCotizacion() != null) {
            response.setSaldoPendiente(entity.getTotalCotizacion());
        } else {
            response.setSaldoPendiente(java.math.BigDecimal.ZERO);
        }
        response.setDescripcion(entity.getDescripcion());
        response.setObservaciones(entity.getObservaciones());
        response.setResponsable(entity.getResponsable());
        
        // Extraer cotización ID, cliente y COTIZACIÓN COMPLETA de la cotización
        if (entity.getCotizacion() != null) {
            CotizacionEntity cotizacion = entity.getCotizacion();
            response.setCotizacionId(cotizacion.getId());
            // Mapear la cotización completa con sus items
            response.setCotizacion(cotizacionMapper.toResponse(cotizacion));
            
            // SIEMPRE extraer el cliente de la cotización
            // El cliente es mandatorio en cotizacion
            if (cotizacion.getCliente() != null) {
                ClienteResponse cliente = clienteMapper.toResponse(cotizacion.getCliente());
                response.setCliente(cliente);
            }
        }
        
        return response;
    }

    public void populateDesnormalizedFields(NegocioEntity entity, CotizacionEntity cotizacion) {
        if (entity == null || cotizacion == null) {
            return;
        }
        
        // Copiar datos de cotización
        entity.setCodigoCotizacion(cotizacion.getCodigo());
        entity.setEstadoCotizacion(cotizacion.getEstado() != null ? cotizacion.getEstado().name() : "APROBADA");
        entity.setFechaCotizacion(cotizacion.getFechaCreacion());
        entity.setFechaValidezCotizacion(cotizacion.getFechaValidez());
        entity.setDescripcionCotizacion(cotizacion.getDescripcion());
        entity.setSubtotalCotizacion(cotizacion.getSubtotal());
        entity.setImpuestosCotizacion(cotizacion.getImpuestos());
        entity.setTotalCotizacion(cotizacion.getTotal());
        entity.setObservacionesCotizacion(cotizacion.getObservaciones());
    }

    public void updateEntityFromRequest(NegocioRequest request, NegocioEntity entity) {
        if (request.getDescripcion() != null && !request.getDescripcion().trim().isEmpty()) {
            entity.setDescripcion(request.getDescripcion());
        }
        if (request.getFechaInicio() != null) {
            entity.setFechaInicio(request.getFechaInicio());
        }
        if (request.getFechaFinEstimada() != null) {
            entity.setFechaFinEstimada(request.getFechaFinEstimada());
        }
        if (request.getPresupuestoAsignado() != null) {
            entity.setPresupuestoAsignado(request.getPresupuestoAsignado());
        }
        if (request.getAnticipo() != null) {
            entity.setAnticipo(request.getAnticipo());
        }
        if (request.getResponsable() != null && !request.getResponsable().trim().isEmpty()) {
            entity.setResponsable(request.getResponsable());
        }
        if (request.getObservaciones() != null && !request.getObservaciones().trim().isEmpty()) {
            entity.setObservaciones(request.getObservaciones());
        }
    }
}