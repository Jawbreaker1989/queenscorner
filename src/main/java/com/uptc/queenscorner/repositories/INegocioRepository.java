package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.NegocioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface INegocioRepository extends JpaRepository<NegocioEntity, Long> {
    Optional<NegocioEntity> findByCodigo(String codigo);
    Optional<NegocioEntity> findByCotizacionId(Long cotizacionId);
    List<NegocioEntity> findByEstado(NegocioEntity.EstadoNegocio estado);
    boolean existsByCodigo(String codigo);
}