package com.uptc.queenscorner.services.impl;

import com.uptc.queenscorner.models.dtos.requests.ClienteRequest;
import com.uptc.queenscorner.models.dtos.responses.ClienteResponse;
import com.uptc.queenscorner.models.entities.ClienteEntity;
import com.uptc.queenscorner.models.mappers.ClienteMapper;
import com.uptc.queenscorner.repositories.IClienteRepository;
import com.uptc.queenscorner.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de clientes.
 * 
 * Responsabilidades:
 * - Gestionar operaciones CRUD para clientes
 * - Aplicar lógica de negocio (validaciones, cálculos)
 * - Manejar caché para optimizar consultas frecuentes
 * - Coordinar con repository y mapper
 * 
 * Estrategia de Caché:
 * - findAllActive(): Caché compartida bajo clave 'allActive' (cache invalidation: ALWAYS)
 * - findById(): Caché individual por ID (cache invalidation: ON CREATE/UPDATE/DELETE)
 * - create/update/delete: Invalidan TODO el cache de clientes al modificar
 * 
 * Patrón de Eliminación:
 * - Eliminación LÓGICA: setActivo(false) en lugar de eliminar registros
 * - Preserva historial y referencias de negocio
 */
@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    /**
     * Obtiene todos los clientes activos del sistema.
     * 
     * Flujo:
     * 1. Consulta BD con findByActivoTrue() (query optimizada)
     * 2. Mapea cada ClienteEntity a ClienteResponse
     * 3. Retorna lista de DTOs para el cliente
     * 
     * Caché:
     * - Clave: 'allActive' (estática para esta operación)
     * - Se invalida cuando se crea/actualiza/elimina cualquier cliente
     * 
     * @return Lista de DTOs de clientes activos, nunca null (puede ser vacía)
     */
    @Override
    @Cacheable(value = "clientes", key = "'allActive'")
    public List<ClienteResponse> findAllActive() {
        return clienteRepository.findByActivoTrue().stream()
                .map(clienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca un cliente por su ID.
     * 
     * Validaciones:
     * - El cliente debe existir en BD
     * - El cliente debe estar activo (no eliminado lógicamente)
     * 
     * Caché:
     * - Clave: ID del cliente (cada cliente cachea independientemente)
     * - Se invalida cuando se actualiza ese cliente específicamente
     * 
     * @param id Identificador único del cliente
     * @return DTO con datos del cliente
     * @throws RuntimeException si el cliente no existe o está inactivo
     */
    @Override
    @Cacheable(value = "clientes", key = "#id")
    public ClienteResponse findById(Long id) {
        ClienteEntity cliente = clienteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return clienteMapper.toResponse(cliente);
    }

    /**
     * Crea un nuevo cliente en el sistema.
     * 
     * Flujo:
     * 1. Mapea ClienteRequest → ClienteEntity
     * 2. Persiste en BD (ID generado automáticamente)
     * 3. Mapea ClienteEntity → ClienteResponse
     * 4. Invalida TODO el caché (porque afecta findAllActive())
     * 
     * Estado Inicial:
     * - Activo: true (por defecto en ClienteMapper)
     * - Otros campos: Usados desde request o defaults
     * 
     * @param request DTO con datos del cliente (nombre, documento, email, teléfono, dirección)
     * @return DTO del cliente creado incluyendo ID asignado
     */
    @Override
    @CacheEvict(value = "clientes", allEntries = true)
    public ClienteResponse create(ClienteRequest request) {
        ClienteEntity cliente = clienteMapper.toEntity(request);
        ClienteEntity saved = clienteRepository.save(cliente);
        return clienteMapper.toResponse(saved);
    }

    /**
     * Actualiza los datos de un cliente existente.
     * 
     * Validaciones:
     * - Cliente debe existir y estar activo
     * - Solo actualiza campos soportados en ClienteRequest
     * 
     * Flujo:
     * 1. Busca ClienteEntity por ID (debe estar activo)
     * 2. Mapea request → actualiza fields de la entidad
     * 3. Persiste cambios en BD
     * 4. Retorna ClienteResponse con datos actualizados
     * 5. Invalida TODO el caché
     * 
     * Patrón de Actualización:
     * - Usa mapper.updateEntityFromRequest() que hace null-checking
     * - Solo actualiza campos no-nulos del request
     * - Preserva campos no incluidos en request
     * 
     * @param id ID del cliente a actualizar
     * @param request DTO con nuevos datos (campos opcionales)
     * @return DTO del cliente actualizado
     * @throws RuntimeException si el cliente no existe o está inactivo
     */
    @Override
    @CacheEvict(value = "clientes", allEntries = true)
    public ClienteResponse update(Long id, ClienteRequest request) {
        ClienteEntity cliente = clienteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        clienteMapper.updateEntityFromRequest(request, cliente);
        ClienteEntity updated = clienteRepository.save(cliente);
        return clienteMapper.toResponse(updated);
    }

    /**
     * Elimina (desactiva) un cliente del sistema.
     * 
     * Estrategia: ELIMINACIÓN LÓGICA
     * - No elimina registros de BD
     * - Marca cliente como inactivo (activo = false)
     * - Preserva historial para auditoría
     * - Mantiene referencias de negocios/facturas intactas
     * 
     * Flujo:
     * 1. Busca ClienteEntity por ID (debe estar activo)
     * 2. Marca activo = false
     * 3. Persiste cambio en BD
     * 4. Invalida caché completa
     * 
     * Efecto:
     * - Cliente ya no aparece en findAllActive()
     * - findById(id) lanzará excepción (expects activo=true)
     * - Datos históricos preservados para reportes
     * 
     * @param id ID del cliente a desactivar
     * @throws RuntimeException si el cliente no existe o ya está inactivo
     */
    @Override
    @CacheEvict(value = "clientes", allEntries = true)
    public void delete(Long id) {
        ClienteEntity cliente = clienteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }
} 