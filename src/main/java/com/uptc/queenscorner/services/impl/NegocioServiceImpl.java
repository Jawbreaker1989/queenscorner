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

        NegocioEntity negocio = new NegocioEntity();
        negocio.setCotizacion(cotizacion);
        negocio.setCodigo(generarCodigoNegocio());
        
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