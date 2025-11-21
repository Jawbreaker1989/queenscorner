package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.ItemCotizacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IItemCotizacionRepository extends JpaRepository<ItemCotizacionEntity, Long> {
    List<ItemCotizacionEntity> findByCotizacionId(Long cotizacionId);
    void deleteByCotizacionId(Long cotizacionId);
} 