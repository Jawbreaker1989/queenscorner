package com.uptc.queenscorner.services.impl;

import com.uptc.queenscorner.dtos.*;
import com.uptc.queenscorner.mappers.FacturaMapper;
import com.uptc.queenscorner.models.entities.*;
import com.uptc.queenscorner.repositories.*;
import com.uptc.queenscorner.services.IFacturaService;
import com.uptc.queenscorner.services.async.PdfAsyncService;
import com.uptc.queenscorner.services.validation.FacturaValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FacturaServiceImpl implements IFacturaService {

    @Autowired
    private IFacturaRepository facturaRepository;

    @Autowired
    private INegocioRepository negocioRepository;

    @Autowired
    private FacturaMapper facturaMapper;

    @Autowired
    private PdfAsyncService pdfAsyncService;

    @Autowired
    private FacturaValidationService validationService;

    @Override
    @CacheEvict(value = "facturas", allEntries = true)
    public FacturaResponse crearFactura(CrearFacturaRequest request, String usuario) {
        NegocioEntity negocio = negocioRepository.findById(request.getNegocioId())
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        // VALIDACIÓN CENTRALIZADA: Solo negocios FINALIZADOS pueden tener factura
        validationService.validarNegocioParaFactura(negocio);

        // Obtener cotización del negocio
        CotizacionEntity cotizacion = negocio.getCotizacion();

        FacturaEntity factura = new FacturaEntity();
        factura.setNegocio(negocio);
        factura.setCotizacion(cotizacion);
        factura.setUsuarioCreacion(usuario);
        factura.setEstado("ENVIADA");
        factura.setNumeroFactura("FAC-" + System.currentTimeMillis());
        
        // Copiar anticipo del negocio
        if (negocio.getAnticipo() != null) {
            factura.setAnticipo(negocio.getAnticipo());
        }

        FacturaEntity guardada = facturaRepository.save(factura);

        if (request.getLineas() != null && !request.getLineas().isEmpty()) {
            int numeroLinea = 1;
            for (AgregarLineaRequest lineaReq : request.getLineas()) {
                LineaFacturaEntity linea = new LineaFacturaEntity();
                linea.setFactura(guardada);
                linea.setNumeroLinea(numeroLinea++);
                linea.setDescripcion(lineaReq.getDescripcion());
                linea.setCantidad(lineaReq.getCantidad().intValue());
                linea.setValorUnitario(lineaReq.getValorUnitario());
                guardada.getLineas().add(linea);
            }
        }

        guardada.calcularTotales();
        FacturaEntity actualizada = facturaRepository.save(guardada);
        
        return facturaMapper.toResponse(actualizada);
    }

    @Override
    @CacheEvict(value = "facturas", allEntries = true)
    public FacturaResponse agregarLinea(Long facturaId, AgregarLineaRequest request) {
        FacturaEntity factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        validationService.validarFacturaParaAgregarLinea(factura);

        int numeroLinea = factura.getLineas().size() + 1;
        LineaFacturaEntity linea = new LineaFacturaEntity();
        linea.setFactura(factura);
        linea.setNumeroLinea(numeroLinea);
        linea.setDescripcion(request.getDescripcion());
        linea.setCantidad(request.getCantidad().intValue());
        linea.setValorUnitario(request.getValorUnitario());
        
        factura.getLineas().add(linea);
        factura.calcularTotales();
        
        FacturaEntity actualizada = facturaRepository.save(factura);
        return facturaMapper.toResponse(actualizada);
    }

    @Override
    @CacheEvict(value = "facturas", allEntries = true)
    public FacturaResponse removerLinea(Long facturaId, Long lineaId) {
        FacturaEntity factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        validationService.validarFacturaParaAgregarLinea(factura);

        factura.getLineas().removeIf(l -> l.getId().equals(lineaId));
        factura.calcularTotales();
        
        FacturaEntity actualizada = facturaRepository.save(factura);
        return facturaMapper.toResponse(actualizada);
    }

    @Override
    @CacheEvict(value = "facturas", allEntries = true)
    public FacturaResponse enviarFactura(Long facturaId, String usuario) {
        FacturaEntity factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        validationService.validarFacturaParaEnvio(factura);

        factura.cambiarAEnviada(usuario);
        FacturaEntity actualizada = facturaRepository.save(factura);
        
        pdfAsyncService.generarPdfFacturaAsync(actualizada);
        
        return facturaMapper.toResponse(actualizada);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "factura", key = "#id")
    public FacturaResponse obtenerFactura(Long id) {
        FacturaEntity factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        return facturaMapper.toResponse(factura);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "facturas")
    public List<FacturaResponse> listarFacturas() {
        return facturaRepository.findAll().stream()
                .map(facturaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaResponse> listarPorNegocio(Long negocioId) {
        return facturaRepository.findByNegocioId(negocioId).stream()
                .map(facturaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaResponse obtenerResumen(Long facturaId) {
        return obtenerFactura(facturaId);
    }
}
