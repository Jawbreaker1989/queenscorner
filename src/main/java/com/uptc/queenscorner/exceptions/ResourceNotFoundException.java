package com.uptc.queenscorner.exceptions;

/**
 * Excepción lanzada cuando no se encuentra un recurso (404)
 * Se utiliza cuando busca un cliente, cotización, negocio o factura inexistente
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Constructor con mensaje descriptivo
     * @param message Descripción de qué recurso no fue encontrado
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
} 