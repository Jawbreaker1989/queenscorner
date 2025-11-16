package com.uptc.queenscorner.services.impl;

import com.uptc.queenscorner.models.dtos.requests.FacturaRequest;
import com.uptc.queenscorner.models.dtos.responses.FacturaResponse;
import com.uptc.queenscorner.models.entities.FacturaEntity;
import com.uptc.queenscorner.models.entities.NegocioEntity;
import com.uptc.queenscorner.models.mappers.FacturaMapper;
import com.uptc.queenscorner.repositories.IFacturaRepository;
import com.uptc.queenscorner.repositories.INegocioRepository;
import com.uptc.queenscorner.services.IFacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaServiceImpl implements IFacturaService {

    @Autowired
    private IFacturaRepository facturaRepository;

    @Autowired
    private INegocioRepository negocioRepository;

    @Autowired
    private FacturaMapper facturaMapper;

    @Override
    @Cacheable(value = "facturas", key = "'all'")
    public List<FacturaResponse> findAll() {
        return facturaRepository.findAll().stream()
                .map(facturaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "facturas", key = "#id")
    public FacturaResponse findById(Long id) {
        FacturaEntity factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        return facturaMapper.toResponse(factura);
    }

    @Override
    public FacturaResponse findByCodigo(String codigo) {
        FacturaEntity factura = facturaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        return facturaMapper.toResponse(factura);
    }

    @Override
    @CacheEvict(value = "facturas", allEntries = true)
    public FacturaResponse create(FacturaRequest request) {
        NegocioEntity negocio = negocioRepository.findById(request.getNegocioId())
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        FacturaEntity factura = new FacturaEntity();
        factura.setNegocio(negocio);
        factura.setCodigo(generarCodigoFactura());
        factura.setSubtotal(negocio.getCotizacion().getSubtotal());
        factura.setImpuestos(negocio.getCotizacion().getImpuestos());
        factura.setTotal(negocio.getCotizacion().getTotal());
        facturaMapper.updateEntityFromRequest(request, factura);

        FacturaEntity saved = facturaRepository.save(factura);
        return facturaMapper.toResponse(saved);
    }

    private String generarCodigoFactura() {
        return "FAC-" + System.currentTimeMillis();
    }

    @Override
    @CacheEvict(value = "facturas", allEntries = true)
    public FacturaResponse cambiarEstado(Long id, String estado) {
        FacturaEntity factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        factura.setEstado(FacturaEntity.EstadoFactura.valueOf(estado));
        
        if (estado.equals("ENVIADA")) {
            factura.setFechaEnvio(java.time.LocalDateTime.now());
        }

        FacturaEntity updated = facturaRepository.save(factura);
        return facturaMapper.toResponse(updated);
    }

    @Override
    public List<FacturaResponse> findByEstado(String estado) {
        return facturaRepository.findByEstado(FacturaEntity.EstadoFactura.valueOf(estado)).stream()
                .map(facturaMapper::toResponse)
                .collect(Collectors.toList());
    }
}