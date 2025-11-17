package com.uptc.queenscorner.services.impl;

import com.uptc.queenscorner.models.dtos.requests.OrdenTrabajoRequest;
import com.uptc.queenscorner.models.dtos.responses.OrdenTrabajoResponse;
import com.uptc.queenscorner.models.entities.NegocioEntity;
import com.uptc.queenscorner.models.entities.OrdenTrabajoEntity;
import com.uptc.queenscorner.models.mappers.OrdenTrabajoMapper;
import com.uptc.queenscorner.repositories.INegocioRepository;
import com.uptc.queenscorner.repositories.IOrdenTrabajoRepository;
import com.uptc.queenscorner.services.IOrdenTrabajoService;
import com.uptc.queenscorner.services.async.NotificacionAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenTrabajoServiceImpl implements IOrdenTrabajoService {

    @Autowired
    private IOrdenTrabajoRepository ordenTrabajoRepository;

    @Autowired
    private INegocioRepository negocioRepository;

    @Autowired
    private OrdenTrabajoMapper ordenTrabajoMapper;

    @Autowired
    private NotificacionAsyncService notificacionAsyncService;

    @Override
    @Cacheable(value = "ordenesTrabajo", key = "'all'")
    public List<OrdenTrabajoResponse> findAll() {
        return ordenTrabajoRepository.findAll().stream()
                .map(ordenTrabajoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "ordenesTrabajo", key = "#id")
    public OrdenTrabajoResponse findById(Long id) {
        OrdenTrabajoEntity orden = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada"));
        return ordenTrabajoMapper.toResponse(orden);
    }

    @Override
    public OrdenTrabajoResponse findByCodigo(String codigo) {
        OrdenTrabajoEntity orden = ordenTrabajoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada"));
        return ordenTrabajoMapper.toResponse(orden);
    }

    @Override
    @CacheEvict(value = "ordenesTrabajo", allEntries = true)
    public OrdenTrabajoResponse create(OrdenTrabajoRequest request) {
        NegocioEntity negocio = negocioRepository.findById(request.getNegocioId())
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        // APLICAR DEFAULTS INTELIGENTES
        aplicarDefaultsInteligentesOrden(request, negocio);

        OrdenTrabajoEntity orden = ordenTrabajoMapper.toEntity(request);
        orden.setNegocio(negocio);
        orden.setCodigo(generarCodigoOrdenTrabajo());

        OrdenTrabajoEntity saved = ordenTrabajoRepository.save(orden);
        return ordenTrabajoMapper.toResponse(saved);
    }

    private void aplicarDefaultsInteligentesOrden(OrdenTrabajoRequest request, NegocioEntity negocio) {
        // Si no hay descripción, usar info del negocio
        if (request.getDescripcion() == null || request.getDescripcion().trim().isEmpty()) {
            request.setDescripcion("Orden para: " + negocio.getCotizacion().getDescripcion());
        }

        // Si no hay prioridad, poner MEDIA
        if (request.getPrioridad() == null || request.getPrioridad().trim().isEmpty()) {
            request.setPrioridad("MEDIA");
        }

        // Si no hay fechas, calcular automáticamente
        if (request.getFechaInicioEstimada() == null) {
            request.setFechaInicioEstimada(java.time.LocalDate.now().plusDays(1));
        }

        if (request.getFechaFinEstimada() == null) {
            request.setFechaFinEstimada(request.getFechaInicioEstimada().plusDays(7));
        }

        // Si no hay observaciones, crear básicas
        if (request.getObservaciones() == null || request.getObservaciones().trim().isEmpty()) {
            request.setObservaciones("Orden de trabajo programada automáticamente");
        }
    }

    private String generarCodigoOrdenTrabajo() {
        return "OT-" + System.currentTimeMillis();
    }

    @Override
    @CacheEvict(value = "ordenesTrabajo", allEntries = true)
    public OrdenTrabajoResponse update(Long id, OrdenTrabajoRequest request) {
        OrdenTrabajoEntity orden = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada"));

        ordenTrabajoMapper.updateEntityFromRequest(request, orden);
        OrdenTrabajoEntity updated = ordenTrabajoRepository.save(orden);
        return ordenTrabajoMapper.toResponse(updated);
    }

    @Override
    @CacheEvict(value = "ordenesTrabajo", allEntries = true)
    public OrdenTrabajoResponse cambiarEstado(Long id, String estado) {
        OrdenTrabajoEntity orden = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada"));

        orden.setEstado(OrdenTrabajoEntity.EstadoOrden.valueOf(estado));
        
        // Si la orden cambia a estado FINALIZADA, generar PDF de notificación
        if ("FINALIZADA".equals(estado)) {
            notificacionAsyncService.notificarOrdenLista(orden);
        }
        
        OrdenTrabajoEntity updated = ordenTrabajoRepository.save(orden);
        return ordenTrabajoMapper.toResponse(updated);
    }

    @Override
    public List<OrdenTrabajoResponse> findByEstado(String estado) {
        return ordenTrabajoRepository.findByEstado(OrdenTrabajoEntity.EstadoOrden.valueOf(estado)).stream()
                .map(ordenTrabajoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdenTrabajoResponse> findByPrioridad(String prioridad) {
        return ordenTrabajoRepository.findByPrioridad(OrdenTrabajoEntity.PrioridadOrden.valueOf(prioridad)).stream()
                .map(ordenTrabajoMapper::toResponse)
                .collect(Collectors.toList());
    }
}