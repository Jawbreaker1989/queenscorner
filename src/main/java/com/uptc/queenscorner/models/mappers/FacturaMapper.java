package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.responses.FacturaResponse;
import com.uptc.queenscorner.models.dtos.responses.LineaFacturaResponse;
import com.uptc.queenscorner.models.dtos.responses.NegocioInfoResponse;
import com.uptc.queenscorner.models.dtos.responses.ClienteInfoResponse;
import com.uptc.queenscorner.models.entities.*;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * Mapper para convertir FacturaEntity y LineaFacturaEntity a DTOs de respuesta.
 * 
 * Responsabilidades:
 * - Convertir FacturaEntity a FacturaResponse (toResponse)
 * - Convertir LineaFacturaEntity a LineaFacturaResponse (toLineaResponse)
 * - Convertir NegocioEntity a información resumida (toNegocioInfo)
 * - Calcular saldo pendiente durante la conversión
 * 
 * Nota: No hay conversión de Request a Entity porque las facturas se crean
 * a través de la lógica de negocio, no directamente desde DTOs.
 */
@Component
public class FacturaMapper {
    
    /**
     * Convierte una FacturaEntity en FacturaResponse.
     * 
     * Incluye:
     * - Información básica de la factura
     * - Totales e IVA
     * - Información de auditoría (usuario, fechas)
     * - Información resumida del negocio
     * - Todas las líneas de detalle
     * - Cálculo de saldo pendiente
     * 
     * @param entity Entidad de factura
     * @return DTO con todos los datos para enviar al cliente
     */
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
        response.setAnticipo(entity.getAnticipo());
        response.setObservaciones(entity.getObservaciones());
        response.setUsuarioCreacion(entity.getUsuarioCreacion());
        response.setUsuarioEnvio(entity.getUsuarioEnvio());
        response.setPathPdf(entity.getPathPdf());
        response.setNegocio(toNegocioInfo(entity.getNegocio()));
        
        // Calcular saldo pendiente: total - anticipo
        // Representa lo que aún debe pagar el cliente
        if (entity.getTotal() != null && entity.getAnticipo() != null) {
            BigDecimal saldoPendiente = entity.getTotal().subtract(entity.getAnticipo());
            response.setSaldoPendiente(saldoPendiente);
        } else if (entity.getTotal() != null) {
            response.setSaldoPendiente(entity.getTotal());
        }
        
        // Mapear todas las líneas de la factura
        if (entity.getLineas() != null) {
            entity.getLineas().forEach(linea -> 
                response.getLineas().add(toLineaResponse(linea))
            );
        }
        
        return response;
    }
    
    /**
     * Convierte una LineaFacturaEntity en LineaFacturaResponse.
     * 
     * Una línea representa un servicio/producto específico facturado.
     * 
     * @param entity Entidad de línea de factura
     * @return DTO con los datos de la línea
     */
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
    
    /**
     * Convierte una NegocioEntity en información resumida (NegocioInfoResponse).
     * 
     * Se utiliza cuando la información del negocio se embebe en la respuesta de factura.
     * Solo incluye datos financieros y de identificación esenciales.
     * 
     * Información incluida:
     * - ID, código, descripción, fecha
     * - Totales: cotización, anticipo, saldo pendiente
     * - Información resumida del cliente
     * 
     * @param entity Entidad de negocio
     * @return DTO con información resumida, o null si entity es null
     */
    public NegocioInfoResponse toNegocioInfo(NegocioEntity entity) {
        if (entity == null) return null;
        
        NegocioInfoResponse response = new NegocioInfoResponse();
        response.setId(entity.getId());
        response.setNumero(entity.getCodigo());
        response.setFechaCreacion(entity.getFechaCreacion());
        response.setProyecto(entity.getDescripcion());
        
        // Obtener totales desde la cotización asociada
        if (entity.getCotizacion() != null) {
            CotizacionEntity cot = entity.getCotizacion();
            response.setTotalCotizacion(cot.getTotal());
            response.setAnticipo(entity.getAnticipo());
            
            // Calcular saldo pendiente: total cotización - anticipo
            if (cot.getTotal() != null && entity.getAnticipo() != null) {
                BigDecimal saldoPendiente = cot.getTotal().subtract(entity.getAnticipo());
                response.setSaldoPendiente(saldoPendiente);
            }
            
            // Incluir información resumida del cliente desde la cotización
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
