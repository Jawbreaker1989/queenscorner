package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.ItemCotizacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository (Acceso a Datos) para la entidad ItemCotizacionEntity.
 * 
 * Proporciona métodos para acceder y manipular items de cotización en la base de datos.
 * 
 * Métodos personalizados:
 * - findByCotizacionId(): Obtiene todos los items de una cotización
 * - deleteByCotizacionId(): Elimina todos los items de una cotización (operación en cascada)
 */
@Repository
public interface IItemCotizacionRepository extends JpaRepository<ItemCotizacionEntity, Long> {
    
    /**
     * Obtiene todos los items que pertenecen a una cotización específica.
     * @param cotizacionId ID de la cotización
     * @return Lista de items de la cotización
     */
    List<ItemCotizacionEntity> findByCotizacionId(Long cotizacionId);
    
    /**
     * Elimina todos los items asociados a una cotización.
     * Operación útil para limpiar datos cuando se elimina una cotización.
     * @param cotizacionId ID de la cotización cuyos items se eliminarán
     */
    void deleteByCotizacionId(Long cotizacionId);
} 