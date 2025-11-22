package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.LineaFacturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository (Acceso a Datos) para la entidad LineaFacturaEntity.
 * 
 * Proporciona métodos para acceder y manipular líneas de factura en la base de datos.
 * 
 * Métodos personalizados:
 * - findByFacturaId(): Obtiene todas las líneas de una factura específica
 */
@Repository
public interface ILineaFacturaRepository extends JpaRepository<LineaFacturaEntity, Long> {
    
    /**
     * Obtiene todas las líneas de detalle que pertenecen a una factura específica.
     * @param facturaId ID de la factura
     * @return Lista de líneas de la factura
     */
    List<LineaFacturaEntity> findByFacturaId(Long facturaId);
} 
