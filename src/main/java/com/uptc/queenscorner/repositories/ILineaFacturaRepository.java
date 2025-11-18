package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.LineaFacturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILineaFacturaRepository extends JpaRepository<LineaFacturaEntity, Long> {
    List<LineaFacturaEntity> findByFacturaId(Long facturaId);
}
