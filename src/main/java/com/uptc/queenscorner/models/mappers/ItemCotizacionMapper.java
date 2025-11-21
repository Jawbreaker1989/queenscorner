package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.ItemCotizacionRequest;
import com.uptc.queenscorner.models.dtos.responses.ItemCotizacionResponse;
import com.uptc.queenscorner.models.entities.ItemCotizacionEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemCotizacionMapper {

    public ItemCotizacionEntity toEntity(ItemCotizacionRequest request) {
        ItemCotizacionEntity entity = new ItemCotizacionEntity();
        entity.setDescripcion(request.getDescripcion());
        entity.setCantidad(request.getCantidad());
        entity.setPrecioUnitario(request.getPrecioUnitario());
        
        if (request.getCantidad() != null && request.getPrecioUnitario() != null) {
            entity.setSubtotal(request.getPrecioUnitario().multiply(
                java.math.BigDecimal.valueOf(request.getCantidad())
            ));
        }
        
        return entity;
    }

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