package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IPagoRepository extends JpaRepository<PagoEntity, Long> {
    List<PagoEntity> findByNegocioId(Long negocioId);
    List<PagoEntity> findByNegocioIdOrderByFechaPagoDesc(Long negocioId);
}