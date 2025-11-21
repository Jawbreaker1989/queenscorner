package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.FacturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IFacturaRepository extends JpaRepository<FacturaEntity, Long> {
    
    Optional<FacturaEntity> findByNumeroFactura(String numeroFactura);
    
    List<FacturaEntity> findByNegocioId(Long negocioId);
} 

