package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.CotizacionRequest;
import com.uptc.queenscorner.models.dtos.responses.CotizacionResponse;
import com.uptc.queenscorner.models.entities.CotizacionEntity;
import com.uptc.queenscorner.repositories.IItemCotizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class CotizacionMapper {

    @Autowired
    private ClienteMapper clienteMapper;
    
    @Autowired
    private ItemCotizacionMapper itemCotizacionMapper;
    
    @Autowired
    private IItemCotizacionRepository itemCotizacionRepository;

    public CotizacionResponse toResponse(CotizacionEntity entity) {
        CotizacionResponse response = new CotizacionResponse();
        response.setId(entity.getId());
        response.setCodigo(entity.getCodigo());
        
        
        if (entity.getCliente() != null) {
            response.setCliente(clienteMapper.toResponse(entity.getCliente()));
        }
        
        response.setFechaCreacion(entity.getFechaCreacion());
        response.setFechaValidez(entity.getFechaValidez());
        response.setEstado(entity.getEstado().name());
        response.setDescripcion(entity.getDescripcion());
        response.setSubtotal(entity.getSubtotal());
        response.setImpuestos(entity.getImpuestos());
        response.setTotal(entity.getTotal());
        response.setObservaciones(entity.getObservaciones());
        
        // CRÍTICO: Siempre usar la lista de items más fresca de BD
        // Primero intentar con items de la entidad si fueron cargados recientemente
        if (entity.getId() != null) {
            // Query a BD para garantizar items actualizados (eliminaciones/inserciones)
            response.setItems(
                itemCotizacionRepository.findByCotizacionId(entity.getId()).stream()
                    .map(itemCotizacionMapper::toResponse)
                    .collect(Collectors.toList())
            );
        } else {
            response.setItems(java.util.Collections.emptyList());
        }
        
        return response;
    }

    public void updateEntityFromRequest(CotizacionRequest request, CotizacionEntity entity) {
        if (request.getFechaValidez() != null) entity.setFechaValidez(request.getFechaValidez());
        if (request.getDescripcion() != null) entity.setDescripcion(request.getDescripcion());
        if (request.getObservaciones() != null) entity.setObservaciones(request.getObservaciones());
    }
}