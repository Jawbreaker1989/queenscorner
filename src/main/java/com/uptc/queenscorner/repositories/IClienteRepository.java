package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IClienteRepository extends JpaRepository<ClienteEntity, Long> {
    List<ClienteEntity> findByActivoTrue();
    Optional<ClienteEntity> findByIdAndActivoTrue(Long id);
    boolean existsByEmail(String email);
    List<ClienteEntity> findByNombreContainingIgnoreCase(String nombre);
}