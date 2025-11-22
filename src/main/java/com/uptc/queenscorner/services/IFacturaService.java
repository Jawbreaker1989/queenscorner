package com.uptc.queenscorner.services;

import com.uptc.queenscorner.models.dtos.requests.AgregarLineaRequest;
import com.uptc.queenscorner.models.dtos.requests.CrearFacturaRequest;
import com.uptc.queenscorner.models.dtos.responses.FacturaResponse;

import java.util.List;

/**
 * Servicio de facturas (invoices)
 * Gestiona la creación, emisión y seguimiento de facturas
 * Las facturas se crean a partir de negocios finalizados
 * Incluye gestión de líneas de factura y cálculo de totales
 * Implementación: FacturaServiceImpl
 */
public interface IFacturaService {
    
    /**
     * Crea una nueva factura para un negocio
     * Valida que el negocio esté en estado válido
     * Calcula automáticamente subtotal, IVA y total
     * @param request Datos de la factura (negocio, líneas, observaciones)
     * @param usuario Usuario que crea la factura (para auditoría)
     * @return Factura creada con número generado
     */
    FacturaResponse crearFactura(CrearFacturaRequest request, String usuario);
    
    /**
     * Agrega una nueva línea a una factura existente
     * Solo permite agregar líneas si la factura está en estado EN_REVISION
     * Recalcula automáticamente los totales de la factura
     * @param facturaId ID de la factura a modificar
     * @param request Datos de la línea (descripción, cantidad, valor)
     * @return Factura con la línea agregada y totales recalculados
     */
    FacturaResponse agregarLinea(Long facturaId, AgregarLineaRequest request);
    
    /**
     * Remueve una línea de una factura
     * Solo permite remover si la factura está en estado EN_REVISION
     * Recalcula automáticamente los totales
     * @param facturaId ID de la factura
     * @param lineaId ID de la línea a remover
     * @return Factura sin la línea y totales recalculados
     */
    FacturaResponse removerLinea(Long facturaId, Long lineaId);
    
    /**
     * Envía/emite una factura
     * Cambia estado de EN_REVISION a ENVIADA
     * Valida que tenga al menos una línea
     * Genera PDF de forma asincrónica
     * @param facturaId ID de la factura a enviar
     * @param usuario Usuario que envía (para auditoría)
     * @return Factura con estado actualizado a ENVIADA
     */
    FacturaResponse enviarFactura(Long facturaId, String usuario);
    
    /**
     * Obtiene una factura completa con todas sus líneas y datos
     * @param id ID de la factura
     * @return Datos completos de la factura
     */
    FacturaResponse obtenerFactura(Long id);
    
    /**
     * Obtiene todas las facturas del sistema
     * @return Lista de todas las facturas
     */
    List<FacturaResponse> listarFacturas();
    
    /**
     * Obtiene todas las facturas asociadas a un negocio específico
     * @param negocioId ID del negocio
     * @return Lista de facturas del negocio
     */
    List<FacturaResponse> listarPorNegocio(Long negocioId);
    
    /**
     * Obtiene un resumen de la factura con cálculos de totales y saldo
     * @param facturaId ID de la factura
     * @return Factura con resumen de totales
     */
    FacturaResponse obtenerResumen(Long facturaId);
} 
