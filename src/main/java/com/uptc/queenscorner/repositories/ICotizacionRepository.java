package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.CotizacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ICotizacionRepository extends JpaRepository<CotizacionEntity, Long> {
    Optional<CotizacionEntity> findByCodigo(String codigo);
    List<CotizacionEntity> findByEstado(CotizacionEntity.EstadoCotizacion estado);
    List<CotizacionEntity> findByClienteId(Long clienteId);
    boolean existsByCodigo(String codigo);
}