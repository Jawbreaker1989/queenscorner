package com.uptc.queenscorner.services.impl;

import com.uptc.queenscorner.models.dtos.requests.NegocioRequest;
import com.uptc.queenscorner.models.dtos.responses.NegocioResponse;
import com.uptc.queenscorner.models.entities.CotizacionEntity;
import com.uptc.queenscorner.models.entities.NegocioEntity;
import com.uptc.queenscorner.models.mappers.NegocioMapper;
import com.uptc.queenscorner.repositories.ICotizacionRepository;
import com.uptc.queenscorner.repositories.INegocioRepository;
import com.uptc.queenscorner.services.INegocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NegocioServiceImpl implements INegocioService {

    @Autowired
    private INegocioRepository negocioRepository;

    @Autowired
    private ICotizacionRepository cotizacionRepository;

    @Autowired
    private NegocioMapper negocioMapper;

    /**
     * Obtiene todos los negocios del sistema.
     * 
     * Caché:
     * - Clave: 'all' (estática para esta operación)
     * - Se invalida en cualquier modificación
     * 
     * @return Lista de DTOs de todos los negocios
     */
    @Override
    @Cacheable(value = "negocios", key = "'all'")
    public List<NegocioResponse> findAll() {
        return negocioRepository.findAll().stream()
                .map(negocioMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca un negocio por ID.
     * 
     * @param id Identificador del negocio
     * @return DTO con datos del negocio incluyendo información denormalizada
     * @throws RuntimeException si no existe
     */
    @Override
    @Cacheable(value = "negocios", key = "#id")
    public NegocioResponse findById(Long id) {
        NegocioEntity negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
        return negocioMapper.toResponse(negocio);
    }

    /**
     * Busca un negocio por su código único.
     * 
     * Código formato: NEG-<timestamp>
     * 
     * @param codigo Código único del negocio
     * @return DTO con datos del negocio
     * @throws RuntimeException si no existe
     */
    @Override
    public NegocioResponse findByCodigo(String codigo) {
        NegocioEntity negocio = negocioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
        return negocioMapper.toResponse(negocio);
    }

    /**
     * Crea un negocio manualmente.
     * 
     * Uso: Crear negocio sin cotización aprobada (creación directa).
     * Alternativa: crearDesdeAprobada() para flujo estándar.
     * 
     * Flujo:
     * 1. Busca cotización asociada
     * 2. Crea entidad Negocio con código generado
     * 3. Desnormaliza campos desde cotización
     * 4. Mapea datos adicionales del request
     * 5. Actualiza fechaActualizacion
     * 6. Persiste y retorna DTO
     * 
     * @param request DTO con datos del negocio (cotizacionId, descripción, anticipo, etc)
     * @return DTO del negocio creado
     * @throws RuntimeException si cotización no existe
     */
    @Override
    @CacheEvict(value = "negocios", allEntries = true)
    public NegocioResponse create(NegocioRequest request) {
        CotizacionEntity cotizacion = cotizacionRepository.findById(request.getCotizacionId())
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));

        NegocioEntity negocio = new NegocioEntity();
        negocio.setCotizacion(cotizacion);
        negocio.setCodigo(generarCodigoNegocio());
        
        // POBLAR DATOS DE COTIZACIÓN
        negocioMapper.populateDesnormalizedFields(negocio, cotizacion);
        
        // Actualizar con datos del request
        negocioMapper.updateEntityFromRequest(request, negocio);
        
        // Registrar timestamp de actualización
        negocio.setFechaActualizacion(LocalDateTime.now());

        NegocioEntity saved = negocioRepository.save(negocio);
        return negocioMapper.toResponse(saved);
    }

    /**
     * Crea un negocio a partir de una cotización APROBADA.
     * 
     * Este es el flujo principal del negocio:
     * Cotización APROBADA → Crear Negocio → Facturar
     * 
     * Validaciones:
     * - Cotización debe existir
     * - Cotización debe estar en estado APROBADA
     * - No debe existir un negocio previo para esta cotización (1-to-1)
     * - Total de cotización debe ser > 0
     * 
     * Flujo:
     * 1. Busca cotización
     * 2. Valida todas las condiciones anteriores
     * 3. Llama a create(request) con cotizacionId
     * 4. create() desnormaliza datos automáticamente
     * 
     * @param cotizacionId ID de la cotización aprobada
     * @param request DTO con datos adicionales del negocio
     * @return DTO del negocio creado con datos desnormalizados
     * @throws RuntimeException si cotización no existe, no está aprobada, o ya tiene negocio
     */
    @Override
    @CacheEvict(value = "negocios", allEntries = true)
    public NegocioResponse crearDesdeAprobada(Long cotizacionId, NegocioRequest request) {
        CotizacionEntity cotizacion = cotizacionRepository.findById(cotizacionId)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));

        // Validar que la cotización esté aprobada
        if (!"APROBADA".equals(cotizacion.getEstado().name())) {
            throw new RuntimeException("Solo se pueden crear negocios desde cotizaciones aprobadas");
        }

        // Validar que no exista un negocio para esta cotización
        if (negocioRepository.findByCotizacion(cotizacion).isPresent()) {
            throw new RuntimeException("Ya existe un negocio para esta cotización");
        }

        // Validar que la cotización tenga total válido
        if (cotizacion.getTotal() == null || cotizacion.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("La cotización no tiene un total válido");
        }

        request.setCotizacionId(cotizacionId);
        return create(request);
    }

    private String generarCodigoNegocio() {
        return "NEG-" + System.currentTimeMillis();
    }

    /**
     * Actualiza los datos de un negocio existente.
     * 
     * Flujo:
     * 1. Busca negocio por ID
     * 2. Mapea datos del request (campos opcionales)
     * 3. Actualiza fechaActualizacion con timestamp actual
     * 4. Persiste y retorna DTO actualizado
     * 
     * @param id ID del negocio a actualizar
     * @param request DTO con nuevos datos (campos opcionales)
     * @return DTO del negocio actualizado
     * @throws RuntimeException si negocio no existe
     */
    @Override
    @CacheEvict(value = "negocios", allEntries = true)
    public NegocioResponse update(Long id, NegocioRequest request) {
        NegocioEntity negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        negocioMapper.updateEntityFromRequest(request, negocio);
        negocio.setFechaActualizacion(LocalDateTime.now());
        NegocioEntity updated = negocioRepository.save(negocio);
        return negocioMapper.toResponse(updated);
    }

    /**
     * Cambia el estado de un negocio.
     * 
     * Estados válidos: EN_REVISION, FINALIZADO, CANCELADO
     * 
     * Transiciones permitidas:
     * - EN_REVISION → FINALIZADO (proyecto completado)
     * - EN_REVISION → CANCELADO (proyecto cancelado)
     * - FINALIZADO y CANCELADO: NO pueden cambiar (estados terminales)
     * 
     * Validaciones:
     * - Si está EN_REVISION: solo puede ir a FINALIZADO o CANCELADO
     * - Si está FINALIZADO: lanza excepción (usar nuevo negocio)
     * - Si está CANCELADO: lanza excepción (estado inmutable)
     * 
     * @param id ID del negocio
     * @param estado Nuevo estado (EN_REVISION, FINALIZADO, CANCELADO)
     * @return DTO con negocio actualizado
     * @throws RuntimeException si negocio no existe o transición no válida
     */
    @Override
    @CacheEvict(value = "negocios", allEntries = true)
    public NegocioResponse cambiarEstado(Long id, String estado) {
        NegocioEntity negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        NegocioEntity.EstadoNegocio nuevoEstado = NegocioEntity.EstadoNegocio.valueOf(estado);
        
        // VALIDACIÓN: Solo desde EN_REVISION se puede ir a FINALIZADO o CANCELADO
        if (negocio.getEstado() == NegocioEntity.EstadoNegocio.EN_REVISION) {
            if (nuevoEstado != NegocioEntity.EstadoNegocio.FINALIZADO && 
                nuevoEstado != NegocioEntity.EstadoNegocio.CANCELADO) {
                throw new RuntimeException("Desde EN_REVISION solo se puede cambiar a FINALIZADO o CANCELADO");
            }
        }
        
        // VALIDACIÓN: Un negocio FINALIZADO no puede volver a cambiar de estado
        if (negocio.getEstado() == NegocioEntity.EstadoNegocio.FINALIZADO) {
            throw new RuntimeException("Un negocio FINALIZADO no puede cambiar de estado. Es necesario crear un nuevo negocio.");
        }
        
        // VALIDACIÓN: Un negocio CANCELADO no puede volver a cambiar de estado
        if (negocio.getEstado() == NegocioEntity.EstadoNegocio.CANCELADO) {
            throw new RuntimeException("Un negocio CANCELADO no puede cambiar de estado.");
        }
        
        negocio.setEstado(nuevoEstado);
        negocio.setFechaActualizacion(java.time.LocalDateTime.now());
        NegocioEntity updated = negocioRepository.save(negocio);
        return negocioMapper.toResponse(updated);
    }

    /**
     * Obtiene negocios filtrados por estado específico.
     * 
     * Estados posibles: EN_REVISION, FINALIZADO, CANCELADO
     * 
     * Uso típico:
     * - findByEstado("EN_REVISION"): Negocios en ejecución
     * - findByEstado("FINALIZADO"): Negocios completados
     * - findByEstado("CANCELADO"): Negocios cancelados
     * 
     * @param estado Estado a filtrar
     * @return Lista de negocios con el estado especificado
     */
    @Override
    public List<NegocioResponse> findByEstado(String estado) {
        return negocioRepository.findByEstado(NegocioEntity.EstadoNegocio.valueOf(estado)).stream()
                .map(negocioMapper::toResponse)
                .collect(Collectors.toList());
    }
}