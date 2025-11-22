package com.uptc.queenscorner.services;

import com.uptc.queenscorner.models.dtos.requests.CotizacionRequest;
import com.uptc.queenscorner.models.dtos.responses.CotizacionResponse;
import java.util.List;

/**
 * Servicio de cotizaciones (presupuestos)
 * Define operaciones para crear, actualizar y gestionar cotizaciones
 * Las cotizaciones son la base para crear negocios y facturas
 * Implementación: CotizacionServiceImpl
 */
public interface ICotizacionService {
    
    /**
     * Obtiene todas las cotizaciones del sistema
     * @return Lista completa de cotizaciones
     */
    List<CotizacionResponse> findAll();
    
    /**
     * Busca una cotización específica por ID
     * @param id Identificador de la cotización
     * @return Datos de la cotización encontrada
     */
    CotizacionResponse findById(Long id);
    
    /**
     * Busca una cotización por su código único
     * Formato del código: COT-YYYYMMDD-XXXXX
     * @param codigo Código único de la cotización
     * @return Datos de la cotización encontrada
     */
    CotizacionResponse findByCodigo(String codigo);
    
    /**
     * Crea una nueva cotización en el sistema
     * Genera automáticamente código y número secuencial
     * Calcula subtotal, IVA y total basado en items
     * @param request Datos de la cotización (cliente, items, descripción, etc)
     * @return Cotización creada con código generado
     */
    CotizacionResponse create(CotizacionRequest request);
    
    /**
     * Actualiza los datos de una cotización existente
     * Solo se pueden actualizar cotizaciones en estado BORRADOR
     * @param id ID de la cotización a actualizar
     * @param request Nuevos datos de la cotización
     * @return Cotización actualizada
     */
    CotizacionResponse update(Long id, CotizacionRequest request);
    
    /**
     * Cambia el estado de una cotización
     * Flujo válido: BORRADOR → ENVIADA → APROBADA o RECHAZADA
     * @param id ID de la cotización
     * @param estado Nuevo estado (BORRADOR, ENVIADA, APROBADA, RECHAZADA)
     * @return Cotización con estado actualizado
     */
    CotizacionResponse cambiarEstado(Long id, String estado);
    
    /**
     * Elimina una cotización
     * Solo permite eliminar cotizaciones en estado BORRADOR
     * @param id ID de la cotización a eliminar
     */
    void delete(Long id);
} 