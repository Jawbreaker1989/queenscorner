package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.ItemCotizacionRequest;
import com.uptc.queenscorner.models.dtos.responses.ItemCotizacionResponse;
import com.uptc.queenscorner.models.entities.ItemCotizacionEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre ItemCotizacionRequest, ItemCotizacionEntity e ItemCotizacionResponse.
 * 
 * Responsabilidades:
 * - Convertir DTOs de entrada a entidades de BD (toEntity)
 * - Convertir entidades de BD a DTOs de salida (toResponse)
 * - Calcular subtotales durante la conversión
 * 
 * Un item representa un servicio/producto en una cotización.
 */
@Component
public class ItemCotizacionMapper {

    /**
     * Convierte un ItemCotizacionRequest en ItemCotizacionEntity.
     * Calcula automáticamente el subtotal (cantidad * precio unitario).
     * 
     * @param request DTO con los datos del item
     * @return Entidad preparada para persistir
     */
    public ItemCotizacionEntity toEntity(ItemCotizacionRequest request) {
        ItemCotizacionEntity entity = new ItemCotizacionEntity();
        entity.setDescripcion(request.getDescripcion());
        entity.setCantidad(request.getCantidad());
        entity.setPrecioUnitario(request.getPrecioUnitario());
        
        // Calcular subtotal automáticamente
        if (request.getCantidad() != null && request.getPrecioUnitario() != null) {
            entity.setSubtotal(request.getPrecioUnitario().multiply(
                java.math.BigDecimal.valueOf(request.getCantidad())
            ));
        }
        
        return entity;
    }

    /**
     * Convierte una ItemCotizacionEntity en ItemCotizacionResponse.
     * 
     * @param entity Entidad de BD
     * @return DTO con los datos para enviar al cliente
     */
    public ItemCotizacionResponse toResponse(ItemCotizacionEntity entity) {
        ItemCotizacionResponse response = new ItemCotizacionResponse();
        response.setId(entity.getId());
        response.setDescripcion(entity.getDescripcion());
        response.setCantidad(entity.getCantidad());
        response.setPrecioUnitario(entity.getPrecioUnitario());
        response.setSubtotal(entity.getSubtotal());
        return response;
    }
}  