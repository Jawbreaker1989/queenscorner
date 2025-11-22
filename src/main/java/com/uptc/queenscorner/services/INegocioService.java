package com.uptc.queenscorner.services;

import com.uptc.queenscorner.models.dtos.requests.NegocioRequest;
import com.uptc.queenscorner.models.dtos.responses.NegocioResponse;
import java.util.List;

/**
 * Servicio de negocios (proyectos)
 * Gestiona los negocios creados a partir de cotizaciones aprobadas
 * Un negocio representa la ejecución del proyecto con presupuesto y seguimiento
 * Implementación: NegocioServiceImpl
 */
public interface INegocioService {
    
    /**
     * Obtiene todos los negocios del sistema
     * @return Lista completa de negocios
     */
    List<NegocioResponse> findAll();
    
    /**
     * Busca un negocio específico por ID
     * @param id Identificador del negocio
     * @return Datos del negocio encontrado
     */
    NegocioResponse findById(Long id);
    
    /**
     * Busca un negocio por su código único
     * Formato del código: NEG-YYYYMMDD-XXXXX
     * @param codigo Código único del negocio
     * @return Datos del negocio encontrado
     */
    NegocioResponse findByCodigo(String codigo);
    
    /**
     * Crea un nuevo negocio manualmente
     * No requiere una cotización aprobada
     * @param request Datos del negocio (presupuesto, descripción, fechas, etc)
     * @return Negocio creado con código generado
     */
    NegocioResponse create(NegocioRequest request);
    
    /**
     * Crea un negocio a partir de una cotización APROBADA
     * Este es el flujo principal: cotización aprobada → negocio
     * Copia automáticamente datos de la cotización al negocio
     * @param cotizacionId ID de la cotización aprobada
     * @param request Datos adicionales del negocio
     * @return Negocio creado con datos copiados de la cotización
     */
    NegocioResponse crearDesdeAprobada(Long cotizacionId, NegocioRequest request);
    
    /**
     * Actualiza los datos de un negocio existente
     * @param id ID del negocio a actualizar
     * @param request Nuevos datos del negocio
     * @return Negocio actualizado
     */
    NegocioResponse update(Long id, NegocioRequest request);
    
    /**
     * Cambia el estado de un negocio
     * Flujo válido: EN_REVISION → FINALIZADO o CANCELADO
     * @param id ID del negocio
     * @param estado Nuevo estado (EN_REVISION, FINALIZADO, CANCELADO)
     * @return Negocio con estado actualizado
     */
    NegocioResponse cambiarEstado(Long id, String estado);
    
    /**
     * Obtiene negocios filtrados por estado específico
     * @param estado Estado a filtrar (EN_REVISION, FINALIZADO, CANCELADO)
     * @return Lista de negocios con el estado especificado
     */
    List<NegocioResponse> findByEstado(String estado);
} 