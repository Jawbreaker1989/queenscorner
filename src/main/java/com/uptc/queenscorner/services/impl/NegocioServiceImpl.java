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
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Override
    @Cacheable(value = "negocios", key = "'all'")
    public List<NegocioResponse> findAll() {
        return negocioRepository.findAll().stream()
                .map(negocioMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "negocios", key = "#id")
    public NegocioResponse findById(Long id) {
        NegocioEntity negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
        return negocioMapper.toResponse(negocio);
    }

    @Override
    public NegocioResponse findByCodigo(String codigo) {
        NegocioEntity negocio = negocioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
        return negocioMapper.toResponse(negocio);
    }

    @Override
    @CacheEvict(value = "negocios", allEntries = true)
    public NegocioResponse create(NegocioRequest request) {
        CotizacionEntity cotizacion = cotizacionRepository.findById(request.getCotizacionId())
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));

        // APLICAR DEFAULTS INTELIGENTES
        aplicarDefaultsInteligentesNegocio(request, cotizacion);

        NegocioEntity negocio = new NegocioEntity();
        negocio.setCotizacion(cotizacion);
        negocio.setCodigo(generarCodigoNegocio());
        
        // Asignar presupuesto desde la cotización
        negocio.setPresupuestoAsignado(cotizacion.getTotal());
        
        // POBLAR DATOS DESNORMALIZADOS DE COTIZACIÓN
        negocioMapper.populateDesnormalizedFields(negocio, cotizacion);
        
        // Actualizar con datos del request
        negocioMapper.updateEntityFromRequest(request, negocio);
        
        // Registrar timestamp de actualización
        negocio.setFechaActualizacion(LocalDateTime.now());

        NegocioEntity saved = negocioRepository.save(negocio);
        return negocioMapper.toResponse(saved);
    }

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

        request.setCotizacionId(cotizacionId);
        return create(request);
    }

    private void aplicarDefaultsInteligentesNegocio(NegocioRequest request, CotizacionEntity cotizacion) {
        // Si no hay descripción, usar la de la cotización
        if (request.getDescripcion() == null || request.getDescripcion().trim().isEmpty()) {
            request.setDescripcion("Negocio para: " + cotizacion.getDescripcion());
        }

        // Si no hay observaciones, crear básicas
        if (request.getObservaciones() == null || request.getObservaciones().trim().isEmpty()) {
            request.setObservaciones("Negocio iniciado correctamente");
        }

        // Si no hay fecha inicio, usar hoy
        if (request.getFechaInicio() == null) {
            request.setFechaInicio(LocalDate.now());
        }

        // Si no hay fecha fin estimada, poner 30 días
        if (request.getFechaFinEstimada() == null) {
            request.setFechaFinEstimada(LocalDate.now().plusDays(30));
        }

        // Si no hay presupuesto asignado, usar el total de la cotización
        if (request.getPresupuestoAsignado() == null) {
            request.setPresupuestoAsignado(cotizacion.getTotal());
        }
    }

    private String generarCodigoNegocio() {
        return "NEG-" + System.currentTimeMillis();
    }

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

    @Override
    @CacheEvict(value = "negocios", allEntries = true)
    public NegocioResponse cambiarEstado(Long id, String estado) {
        NegocioEntity negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        negocio.setEstado(NegocioEntity.EstadoNegocio.valueOf(estado));
        negocio.setFechaActualizacion(LocalDateTime.now());
        NegocioEntity updated = negocioRepository.save(negocio);
        return negocioMapper.toResponse(updated);
    }

    @Override
    public List<NegocioResponse> findByEstado(String estado) {
        return negocioRepository.findByEstado(NegocioEntity.EstadoNegocio.valueOf(estado)).stream()
                .map(negocioMapper::toResponse)
                .collect(Collectors.toList());
    }
}