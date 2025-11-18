package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.FacturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IFacturaRepository extends JpaRepository<FacturaEntity, Long> {
    
    Optional<FacturaEntity> findByNumeroFactura(String numeroFactura);
    
    List<FacturaEntity> findByNegocioId(Long negocioId);
    
    List<FacturaEntity> findByEstado(FacturaEntity.EstadoFactura estado);
    
    List<FacturaEntity> findByPdfGeneradoFalse();
    
    @Query("SELECT f FROM FacturaEntity f WHERE f.fechaEmision BETWEEN :inicio AND :fin")
    List<FacturaEntity> findByFechaEmisionBetween(
        @Param("inicio") LocalDateTime inicio,
        @Param("fin") LocalDateTime fin
    );
    
    long countByEstado(FacturaEntity.EstadoFactura estado);
}
