package com.uptc.queenscorner.models.dtos.responses;

import java.time.LocalDateTime;

/**
 * DTO genérico para enviar respuestas de la API al cliente.
 * 
 * Todas las respuestas del servidor usan este formato estandarizado que incluye:
 * - success: indica si la operación fue exitosa
 * - message: mensaje descriptivo para el usuario
 * - data: los datos específicos de la respuesta (de tipo genérico T)
 * - timestamp: cuándo se generó la respuesta
 * - status: código HTTP de estado
 * 
 * Ejemplo:
 * {
 *   "success": true,
 *   "message": "Cliente creado exitosamente",
 *   "data": { "id": 1, "nombre": "Empresa XYZ" },
 *   "timestamp": "2024-11-21T10:30:00",
 *   "status": 200
 * }
 */
public class ApiResponse<T> {
    /** true si la operación fue exitosa, false en caso de error */
    private boolean success;
    /** Mensaje descriptivo (éxito o error) para mostrar al usuario */
    private String message;
    /** Datos genéricos de la respuesta (cambia según el endpoint) */
    private T data;
    /** Fecha y hora de generación de la respuesta */
    private LocalDateTime timestamp;
    /** Código de estado HTTP (200, 400, 404, 500, etc.) */
    private int status;

    /**
     * Constructor por defecto que inicializa el timestamp
     */
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
} 