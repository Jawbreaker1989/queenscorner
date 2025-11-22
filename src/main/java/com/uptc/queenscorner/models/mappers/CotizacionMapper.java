package com.uptc.queenscorner.models.mappers;

import com.uptc.queenscorner.models.dtos.requests.CotizacionRequest;
import com.uptc.queenscorner.models.dtos.responses.CotizacionResponse;
import com.uptc.queenscorner.models.entities.CotizacionEntity;
import com.uptc.queenscorner.repositories.IItemCotizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre CotizacionRequest, CotizacionEntity y CotizacionResponse.
 * 
 * Responsabilidades:
 * - Convertir DTOs de entrada a entidades (toResponse)
 * - Convertir entidades a DTOs de salida
 * - Actualizar entidades desde DTOs (updateEntityFromRequest)
 * - Cargar items relacionados desde BD para garantizar consistencia
 * 
 * Nota: Este mapper tiene inyecciones de dependencia para acceder a los items
 * asociados a la cotización directamente desde BD.
 */
@Component
public class CotizacionMapper {

    @Autowired
    private ClienteMapper clienteMapper;
    
    @Autowired
    private ItemCotizacionMapper itemCotizacionMapper;
    
    @Autowired
    private IItemCotizacionRepository itemCotizacionRepository;

    /**
     * Convierte una CotizacionEntity en CotizacionResponse.
     * Incluye:
     * - Información del cliente
     * - Todos los items asociados (consultados directamente desde BD)
     * - Totales y cálculos
     * 
     * @param entity Entidad de cotización
     * @return DTO con todos los datos para enviar al cliente
     */
    public CotizacionResponse toResponse(CotizacionEntity entity) {
        CotizacionResponse response = new CotizacionResponse();
        response.setId(entity.getId());
        response.setCodigo(entity.getCodigo());
        
        // Mapear cliente embebido
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
        
        // Cargar items directamente desde BD para garantizar sincronización
        // (hay casos donde se agregan/eliminan items sin actualizar la entidad en memoria)
        if (entity.getId() != null) {
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

    /**
     * Actualiza una CotizacionEntity existente con datos de un CotizacionRequest.
     * Solo actualiza los campos permitidos para actualización (no el código, no el cliente).
     * 
     * @param request DTO con los nuevos datos
     * @param entity Entidad existente a actualizar
     */
    public void updateEntityFromRequest(CotizacionRequest request, CotizacionEntity entity) {
        if (request.getFechaValidez() != null) entity.setFechaValidez(request.getFechaValidez());
        if (request.getDescripcion() != null) entity.setDescripcion(request.getDescripcion());
        if (request.getObservaciones() != null) entity.setObservaciones(request.getObservaciones());
    }
}