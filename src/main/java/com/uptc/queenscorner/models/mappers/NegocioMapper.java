package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.NegocioRequest;
import com.uptc.queenscorner.models.dtos.responses.ClienteResponse;
import com.uptc.queenscorner.models.dtos.responses.NegocioResponse;
import com.uptc.queenscorner.models.entities.NegocioEntity;
import com.uptc.queenscorner.models.entities.CotizacionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre NegocioRequest, NegocioEntity y NegocioResponse.
 * 
 * Responsabilidades:
 * - Convertir entidades a DTOs de salida (toResponse)
 * - Actualizar entidades desde DTOs (updateEntityFromRequest)
 * - Copiar datos denormalizados de cotización a negocio (populateDesnormalizedFields)
 * 
 * Complejidad adicional:
 * El negocio tiene datos denormalizados de la cotización para evitar JOINs costosos.
 * Este mapper maneja tanto los datos del negocio como los datos denormalizados.
 */
@Component
public class NegocioMapper {

    @Autowired
    private ClienteMapper clienteMapper;
    
    @Autowired
    private CotizacionMapper cotizacionMapper;

    /**
     * Convierte una NegocioEntity en NegocioResponse.
     * 
     * Incluye:
     * - Información básica del negocio
     * - Datos denormalizados de la cotización original
     * - Datos del proyecto (fechas, presupuesto, estado)
     * - Cliente y cotización completa (cuando están disponibles)
     * - Cálculo de saldo pendiente
     * 
     * @param entity Entidad de negocio
     * @return DTO con todos los datos para enviar al cliente
     */
    public NegocioResponse toResponse(NegocioEntity entity) {
        if (entity == null) {
            return null;
        }
        
        NegocioResponse response = new NegocioResponse();
        
        // DATOS BÁSICOS DEL NEGOCIO
        response.setId(entity.getId());
        response.setCodigo(entity.getCodigo());
        response.setFechaCreacion(entity.getFechaCreacion());
        response.setFechaActualizacion(entity.getFechaActualizacion());
        
        // DATOS DENORMALIZADOS DE COTIZACIÓN
        // Se copian para consultas rápidas sin JOINs costosos
        response.setCodigoCotizacion(entity.getCodigoCotizacion());
        response.setEstadoCotizacion(entity.getEstadoCotizacion());
        response.setFechaCotizacion(entity.getFechaCotizacion());
        response.setFechaValidezCotizacion(entity.getFechaValidezCotizacion());
        response.setDescripcionCotizacion(entity.getDescripcionCotizacion());
        response.setSubtotalCotizacion(entity.getSubtotalCotizacion());
        response.setImpuestosCotizacion(entity.getImpuestosCotizacion());
        response.setTotalCotizacion(entity.getTotalCotizacion());
        response.setObservacionesCotizacion(entity.getObservacionesCotizacion());
        
        // DATOS EDITABLES DEL NEGOCIO/PROYECTO
        response.setFechaInicio(entity.getFechaInicio());
        response.setFechaFinEstimada(entity.getFechaFinEstimada());
        response.setEstado(entity.getEstado() != null ? entity.getEstado().name() : "EN_REVISION");
        response.setPresupuestoAsignado(entity.getPresupuestoAsignado());
        response.setAnticipo(entity.getAnticipo());
        
        // Calcular saldo pendiente: total cotización - anticipo
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
        
        // DATOS RELACIONALES (si existen en memoria)
        if (entity.getCotizacion() != null) {
            CotizacionEntity cotizacion = entity.getCotizacion();
            response.setCotizacionId(cotizacion.getId());
            // Mapear la cotización completa con sus items
            response.setCotizacion(cotizacionMapper.toResponse(cotizacion));
            
            // Extraer cliente de la cotización (el cliente es mandatorio)
            if (cotizacion.getCliente() != null) {
                ClienteResponse cliente = clienteMapper.toResponse(cotizacion.getCliente());
                response.setCliente(cliente);
            }
        }
        
        return response;
    }

    /**
     * Copia datos denormalizados de una CotizacionEntity a una NegocioEntity.
     * 
     * Esta operación ocurre cuando se crea un negocio a partir de una cotización aprobada.
     * Los datos se copian para evitar JOINs costosos en futuros queries del negocio.
     * 
     * Datos copiados:
     * - Código, estado, descripción, fechas
     * - Totales: subtotal, impuestos, total
     * - Observaciones
     * 
     * @param entity NegocioEntity destino
     * @param cotizacion CotizacionEntity origen de datos
     */
    public void populateDesnormalizedFields(NegocioEntity entity, CotizacionEntity cotizacion) {
        if (entity == null || cotizacion == null) {
            return;
        }
        
        // Copiar datos denormalizados de cotización
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

    /**
     * Actualiza una NegocioEntity existente con datos de un NegocioRequest.
     * Solo actualiza los campos editables del negocio/proyecto.
     * 
     * Campos que se actualizan:
     * - Descripción, fechas de inicio/fin
     * - Presupuesto asignado, anticipo
     * - Responsable, observaciones
     * 
     * Campos que NO se actualizan (inmutables):
     * - Código, datos denormalizados de cotización
     * 
     * @param request DTO con los nuevos datos
     * @param entity NegocioEntity existente a actualizar
     */
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