package com.uptc.queenscorner.repositories;

import com.uptc.queenscorner.models.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository (Acceso a Datos) para la entidad ClienteEntity.
 * 
 * Proporciona métodos para acceder y manipular clientes en la base de datos.
 * Spring Data JPA genera automáticamente las implementaciones de estos métodos.
 * 
 * Métodos personalizados (además de los heredados de JpaRepository):
 * - findByActivoTrue(): Obtiene todos los clientes activos
 * - findByIdAndActivoTrue(): Busca un cliente por ID que esté activo
 * - existsByEmail(): Verifica si un email ya existe
 * - findByNombreContainingIgnoreCase(): Búsqueda de clientes por nombre (case-insensitive)
 */
@Repository
public interface IClienteRepository extends JpaRepository<ClienteEntity, Long> {
    
    /**
     * Encuentra todos los clientes activos (no eliminados lógicamente).
     * @return Lista de clientes con activo = true
     */
    List<ClienteEntity> findByActivoTrue();
    
    /**
     * Busca un cliente específico por su ID, verificando que esté activo.
     * @param id Identificador del cliente
     * @return Optional con el cliente si existe y está activo
     */
    Optional<ClienteEntity> findByIdAndActivoTrue(Long id);
    
    /**
     * Verifica si ya existe un cliente con un email específico.
     * Útil para validar unicidad antes de crear o actualizar.
     * @param email Email a verificar
     * @return true si el email existe, false en caso contrario
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca clientes cuyo nombre contenga el texto proporcionado (sin distinguir mayúsculas).
     * Útil para búsquedas de autocompletado o filtros.
     * @param nombre Texto a buscar en el nombre
     * @return Lista de clientes coincidentes
     */
    List<ClienteEntity> findByNombreContainingIgnoreCase(String nombre);
} 