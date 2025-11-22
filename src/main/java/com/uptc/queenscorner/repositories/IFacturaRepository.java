package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.FacturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository (Acceso a Datos) para la entidad FacturaEntity.
 * 
 * Proporciona métodos para acceder y manipular facturas en la base de datos.
 * 
 * Métodos personalizados:
 * - findByNumeroFactura(): Busca factura por su número único
 * - findByNegocioId(): Obtiene todas las facturas de un negocio
 */
@Repository
public interface IFacturaRepository extends JpaRepository<FacturaEntity, Long> {
    
    /**
     * Busca una factura por su número único (FAC-AAAA-CCCCCC).
     * @param numeroFactura Número único de la factura
     * @return Optional con la factura si existe
     */
    Optional<FacturaEntity> findByNumeroFactura(String numeroFactura);
    
    /**
     * Obtiene todas las facturas generadas a partir de un negocio específico.
     * Un negocio puede generar múltiples facturas (parciales o totales).
     * @param negocioId ID del negocio
     * @return Lista de facturas del negocio
     */
    List<FacturaEntity> findByNegocioId(Long negocioId);
} 

