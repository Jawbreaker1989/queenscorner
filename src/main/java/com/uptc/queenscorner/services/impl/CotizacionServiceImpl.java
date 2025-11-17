package com.uptc.queenscorner.services.impl;

import com.uptc.queenscorner.models.dtos.requests.CotizacionRequest;
import com.uptc.queenscorner.models.dtos.responses.CotizacionResponse;
import com.uptc.queenscorner.models.entities.ClienteEntity;
import com.uptc.queenscorner.models.entities.CotizacionEntity;
import com.uptc.queenscorner.models.entities.ItemCotizacionEntity;
import com.uptc.queenscorner.models.mappers.CotizacionMapper;
import com.uptc.queenscorner.models.mappers.ItemCotizacionMapper;
import com.uptc.queenscorner.repositories.IClienteRepository;
import com.uptc.queenscorner.repositories.ICotizacionRepository;
import com.uptc.queenscorner.repositories.IItemCotizacionRepository;
import com.uptc.queenscorner.services.ICotizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CotizacionServiceImpl implements ICotizacionService {

    @Autowired
    private ICotizacionRepository cotizacionRepository;

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private IItemCotizacionRepository itemCotizacionRepository;

    @Autowired
    private CotizacionMapper cotizacionMapper;

    @Autowired
    private ItemCotizacionMapper itemCotizacionMapper;

    @Override
    @Cacheable(value = "cotizaciones", key = "'all'")
    public List<CotizacionResponse> findAll() {
        return cotizacionRepository.findAll().stream()
                .map(cotizacionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "cotizaciones", key = "#id")
    public CotizacionResponse findById(Long id) {
        CotizacionEntity cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));
        return cotizacionMapper.toResponse(cotizacion);
    }

    @Override
    public CotizacionResponse findByCodigo(String codigo) {
        CotizacionEntity cotizacion = cotizacionRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));
        return cotizacionMapper.toResponse(cotizacion);
    }

    @Override
    @CacheEvict(value = "cotizaciones", allEntries = true)
    public CotizacionResponse create(CotizacionRequest request) {
        ClienteEntity cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // APLICAR DEFAULTS INTELIGENTES
        aplicarDefaultsInteligentes(request);

        CotizacionEntity cotizacion = new CotizacionEntity();
        cotizacion.setCliente(cliente);
        cotizacion.setCodigo(generarCodigoCotizacion());
        cotizacionMapper.updateEntityFromRequest(request, cotizacion);

        calcularTotales(cotizacion, request);
        CotizacionEntity saved = cotizacionRepository.save(cotizacion);

        guardarItemsCotizacion(request, saved);
        return cotizacionMapper.toResponse(saved);
    }

    private void aplicarDefaultsInteligentes(CotizacionRequest request) {
        // Si no hay descripción, crear una básica
        if (request.getDescripcion() == null || request.getDescripcion().trim().isEmpty()) {
            request.setDescripcion("Servicio personalizado");
        }

        // Si no hay fecha validez, poner 30 días
        if (request.getFechaValidez() == null) {
            request.setFechaValidez(java.time.LocalDate.now().plusDays(30));
        }

        // Si no hay items, crear uno básico
        if (request.getItems() == null || request.getItems().isEmpty()) {
            var itemDefault = new com.uptc.queenscorner.models.dtos.requests.ItemCotizacionRequest();
            itemDefault.setDescripcion(request.getDescripcion());
            itemDefault.setCantidad(1);
            itemDefault.setPrecioUnitario(new BigDecimal("100000"));
            request.setItems(java.util.Arrays.asList(itemDefault));
        }
    }

    private void calcularTotales(CotizacionEntity cotizacion, CotizacionRequest request) {
        BigDecimal subtotal = request.getItems().stream()
                .map(item -> item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal impuestos = subtotal.multiply(BigDecimal.valueOf(0.19));
        BigDecimal total = subtotal.add(impuestos);

        cotizacion.setSubtotal(subtotal);
        cotizacion.setImpuestos(impuestos);
        cotizacion.setTotal(total);
    }

    private void guardarItemsCotizacion(CotizacionRequest request, CotizacionEntity cotizacion) {
        List<ItemCotizacionEntity> items = request.getItems().stream()
                .map(itemRequest -> {
                    ItemCotizacionEntity item = itemCotizacionMapper.toEntity(itemRequest);
                    item.setCotizacion(cotizacion);
                    return item;
                })
                .collect(Collectors.toList());

        itemCotizacionRepository.saveAll(items);
    }

    private String generarCodigoCotizacion() {
        return "COT-" + System.currentTimeMillis();
    }

    @Override
    @CacheEvict(value = "cotizaciones", allEntries = true)
    public CotizacionResponse update(Long id, CotizacionRequest request) {
        CotizacionEntity cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));

        cotizacionMapper.updateEntityFromRequest(request, cotizacion);
        CotizacionEntity updated = cotizacionRepository.save(cotizacion);
        return cotizacionMapper.toResponse(updated);
    }

    @Override
    @CacheEvict(value = "cotizaciones", allEntries = true)
    public CotizacionResponse cambiarEstado(Long id, String estado) {
        CotizacionEntity cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));

        // VALIDAR estados permitidos: ENVIADA, APROBADA, RECHAZADA
        if (!estado.equals("ENVIADA") && !estado.equals("APROBADA") && !estado.equals("RECHAZADA")) {
            throw new RuntimeException("Estado inválido. Solo se permite ENVIADA, APROBADA o RECHAZADA");
        }

        cotizacion.setEstado(CotizacionEntity.EstadoCotizacion.valueOf(estado));
        CotizacionEntity updated = cotizacionRepository.save(cotizacion);
        return cotizacionMapper.toResponse(updated);
    }

    @Override
    @CacheEvict(value = "cotizaciones", allEntries = true)
    public void delete(Long id) {
        CotizacionEntity cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));
        cotizacionRepository.delete(cotizacion);
    }
}