package com.uptc.queenscorner.exceptions;

import com.uptc.queenscorner.models.dtos.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para toda la aplicación
 * Intercepta excepciones y retorna respuestas HTTP consistentes
 * Centraliza el manejo de errores para mantener uniformidad en la API
 * Todas las excepciones se retornan con estructura ApiResponse estándar
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de formato JSON inválido (400)
     * Se lanza cuando el cliente envía JSON malformado
     * @param ex Excepción de lectura de mensaje HTTP
     * @return Respuesta con error descriptivo
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("Error de formato en el JSON: " + ex.getMostSpecificCause().getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Maneja excepciones cuando no se encuentra un recurso (404)
     * Se lanza cuando busca cliente, cotización, etc. que no existe
     * @param ex Excepción de recurso no encontrado
     * @return Respuesta con error 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Maneja excepciones de violación de reglas de negocio (400)
     * Ejemplos: cotización no aprobada, estado inválido, datos inconsistentes
     * @param ex Excepción de lógica de negocio
     * @return Respuesta con error descriptivo
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Maneja cualquier RuntimeException no capturada por otros manejadores (500)
     * Es el manejador por defecto para errores inesperados
     * @param ex RuntimeException no esperada
     * @return Respuesta con error interno del servidor
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("Error interno del servidor: " + ex.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
} 