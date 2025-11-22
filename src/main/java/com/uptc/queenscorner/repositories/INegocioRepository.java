package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.CotizacionEntity;
import com.uptc.queenscorner.models.entities.NegocioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository (Acceso a Datos) para la entidad NegocioEntity.
 * 
 * Proporciona métodos para acceder y manipular negocios/proyectos en la base de datos.
 * 
 * Métodos personalizados:
 * - findByCodigo(): Busca negocio por su código único
 * - findByCotizacionId(): Obtiene el negocio creado a partir de una cotización
 * - findByCotizacion(): Busca negocio por entidad de cotización
 * - findByEstado(): Obtiene negocios por estado
 * - existsByCodigo(): Verifica si un código de negocio existe
 */
@Repository
public interface INegocioRepository extends JpaRepository<NegocioEntity, Long> {
    
    /**
     * Busca un negocio por su código único (NEG-YYYYMMDD-XXXXX).
     * @param codigo Código único del negocio
     * @return Optional con el negocio si existe
     */
    Optional<NegocioEntity> findByCodigo(String codigo);
    
    /**
     * Obtiene el negocio creado a partir de una cotización específica.
     * Relación OneToOne: una cotización aprobada genera un negocio.
     * @param cotizacionId ID de la cotización
     * @return Optional con el negocio si existe
     */
    Optional<NegocioEntity> findByCotizacionId(Long cotizacionId);
    
    /**
     * Busca un negocio por su entidad de cotización asociada.
     * Alternativa a findByCotizacionId usando la entidad completa.
     * @param cotizacion Entidad de cotización
     * @return Optional con el negocio si existe
     */
    Optional<NegocioEntity> findByCotizacion(CotizacionEntity cotizacion);
    
    /**
     * Obtiene todos los negocios con un estado específico.
     * Útil para reportes y análisis (ej: EN_REVISION, CANCELADO, FINALIZADO).
     * @param estado Estado del negocio a filtrar
     * @return Lista de negocios con ese estado
     */
    List<NegocioEntity> findByEstado(NegocioEntity.EstadoNegocio estado);
    
    /**
     * Verifica si ya existe un negocio con un código específico.
     * Útil para validar unicidad antes de generar un nuevo código.
     * @param codigo Código a verificar
     * @return true si el código existe, false en caso contrario
     */
    boolean existsByCodigo(String codigo);
} 