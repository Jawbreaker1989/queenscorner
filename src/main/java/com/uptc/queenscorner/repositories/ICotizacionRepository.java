package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.CotizacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository (Acceso a Datos) para la entidad CotizacionEntity.
 * 
 * Proporciona métodos para acceder y manipular cotizaciones en la base de datos.
 * 
 * Métodos personalizados:
 * - findByCodigo(): Busca cotización por su código único
 * - findByEstado(): Obtiene todas las cotizaciones con un estado específico
 * - findByClienteId(): Obtiene todas las cotizaciones de un cliente
 * - existsByCodigo(): Verifica si un código de cotización ya existe
 */
@Repository
public interface ICotizacionRepository extends JpaRepository<CotizacionEntity, Long> {
    
    /**
     * Busca una cotización por su código único (COT-YYYYMMDD-XXXXX).
     * @param codigo Código único de la cotización
     * @return Optional con la cotización si existe
     */
    Optional<CotizacionEntity> findByCodigo(String codigo);
    
    /**
     * Obtiene todas las cotizaciones con un estado específico.
     * Útil para reportes o filtros (ej: BORRADOR, ENVIADA, APROBADA, RECHAZADA).
     * @param estado Estado de la cotización a filtrar
     * @return Lista de cotizaciones con ese estado
     */
    List<CotizacionEntity> findByEstado(CotizacionEntity.EstadoCotizacion estado);
    
    /**
     * Obtiene todas las cotizaciones de un cliente específico.
     * @param clienteId ID del cliente
     * @return Lista de cotizaciones del cliente
     */
    List<CotizacionEntity> findByClienteId(Long clienteId);
    
    /**
     * Verifica si ya existe una cotización con un código específico.
     * Útil para validar unicidad antes de generar un nuevo código.
     * @param codigo Código a verificar
     * @return true si el código existe, false en caso contrario
     */
    boolean existsByCodigo(String codigo);
} 