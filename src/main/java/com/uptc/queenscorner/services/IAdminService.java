package com.uptc.queenscorner.services;

import com.uptc.queenscorner.models.dtos.responses.SystemStats;

/**
 * Servicio de administración del sistema
 * Proporciona operaciones de mantenimiento y limpieza de datos
 * Implementación: AdminServiceImpl
 */
public interface IAdminService {

    /**
     * Limpia completamente todos los datos del sistema
     * Elimina todos los registros de todas las tablas manteniendo la estructura
     * Útil para demos y testing
     */
    void cleanAllData();

    /**
     * Obtiene estadísticas del sistema
     * @return Información sobre cantidad de registros en cada tabla
     */
    SystemStats getSystemStats();
}