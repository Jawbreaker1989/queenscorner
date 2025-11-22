package com.uptc.queenscorner.exceptions;

/**
 * Excepción lanzada cuando se viola una regla de negocio (400)
 * Se utiliza para validaciones como:
 * - Estado inválido para la operación
 * - Cotización no aprobada al crear negocio
 * - Factura sin líneas al intentar enviar
 * - Datos inconsistentes
 */
public class BusinessException extends RuntimeException {
    
    /**
     * Constructor con mensaje descriptivo
     * @param message Descripción de la regla de negocio violada
     */
    public BusinessException(String message) {
        super(message);
    }
} 