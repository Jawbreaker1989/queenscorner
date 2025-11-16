package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.OrdenTrabajoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IOrdenTrabajoRepository extends JpaRepository<OrdenTrabajoEntity, Long> {
    Optional<OrdenTrabajoEntity> findByCodigo(String codigo);
    List<OrdenTrabajoEntity> findByEstado(OrdenTrabajoEntity.EstadoOrden estado);
    List<OrdenTrabajoEntity> findByPrioridad(OrdenTrabajoEntity.PrioridadOrden prioridad);
    List<OrdenTrabajoEntity> findByNegocioId(Long negocioId);
    boolean existsByCodigo(String codigo);
}