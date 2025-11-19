package com.uptc.queenscorner.services.impl;

import com.uptc.queenscorner.exceptions.BusinessException;
import com.uptc.queenscorner.models.dtos.requests.CrearFacturaRequest;
import com.uptc.queenscorner.models.dtos.responses.FacturaResponse;
import com.uptc.queenscorner.models.entities.*;
import com.uptc.queenscorner.repositories.*;
import com.uptc.queenscorner.services.IFacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FacturaServiceImpl implements IFacturaService {

    @Autowired
    private IFacturaRepository facturaRepository;

    @Autowired
    private ILineaFacturaRepository lineaRepository;

    @Autowired
    private INegocioRepository negocioRepository;

    @Autowired
    private ICotizacionRepository cotizacionRepository;

    @Autowired
    private IItemCotizacionRepository itemRepository;

    @Autowired
    private FacturaPdfGeneratorService pdfGeneratorService;

    @Override
    @CacheEvict(value = "facturas", allEntries = true)
    public FacturaResponse crearFactura(CrearFacturaRequest request, String usuario) {
        NegocioEntity negocio = negocioRepository.findById(request.getNegocioId())
                .orElseThrow(() -> new BusinessException("Negocio no encontrado"));

        CotizacionEntity cotizacion = cotizacionRepository.findById(request.getCotizacionId())
                .orElseThrow(() -> new BusinessException("Cotización no encontrada"));

        FacturaEntity factura = new FacturaEntity();
        factura.setNegocio(negocio);
        factura.setCotizacion(cotizacion);
        factura.setUsuarioCreacion(usuario);
        factura.setFechaVencimiento(request.getFechaVencimiento() != null 
            ? request.getFechaVencimiento() 
            : LocalDate.now().plusDays(30));
        factura.setAnticipo(negocio.getAnticipo());
        factura.setNotas(request.getNotas());
        factura.setCondicionesPago(request.getCondicionesPago());

        try {
            factura.setMedioPago(FacturaEntity.MedioPago.valueOf(request.getMedioPago().toUpperCase()));
            factura.setReferenciaPago(request.getReferenciaPago());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Medio de pago inválido");
        }

        FacturaEntity facturGuardada = facturaRepository.save(factura);

        BigDecimal subtotal = BigDecimal.ZERO;
        int numeroLinea = 1;

        if (request.getLineas() != null && !request.getLineas().isEmpty()) {
            for (CrearFacturaRequest.LineaRequest lineaReq : request.getLineas()) {
                LineaFacturaEntity linea = new LineaFacturaEntity();
                linea.setFactura(facturGuardada);
                linea.setNumeroLinea(numeroLinea++);
                linea.setDescripcion(lineaReq.getDescripcion());
                linea.setCantidad(lineaReq.getCantidad());
                linea.setValorUnitario(lineaReq.getValorUnitario());

                if (lineaReq.getItemCotizacionId() != null) {
                    ItemCotizacionEntity item = itemRepository.findById(lineaReq.getItemCotizacionId())
                            .orElse(null);
                    linea.setItemCotizacion(item);
                }

                lineaRepository.save(linea);
                subtotal = subtotal.add(lineaReq.getValorUnitario().multiply(BigDecimal.valueOf(lineaReq.getCantidad())));
            }
        }

        facturGuardada.setSubtotalItems(subtotal);
        facturGuardada = facturaRepository.save(facturGuardada);

        return mapToResponse(facturGuardada);
    }

    @Override
    @CacheEvict(value = "facturas", allEntries = true)
    public FacturaResponse emitirFactura(Long id, String usuario) {
        FacturaEntity factura = facturaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Factura no encontrada"));

        if (!factura.puedeEmitirse()) {
            throw new BusinessException("Factura no puede ser emitida en este estado");
        }

        long consecutivo = facturaRepository.count();
        factura.generarNumeroFactura(consecutivo);
        factura.cambiarEstado(FacturaEntity.EstadoFactura.EMITIDA, usuario);

        FacturaEntity actualizada = facturaRepository.save(factura);

        generarPdfAsync(actualizada.getId());

        return mapToResponse(actualizada);
    }

    @Async
    public void generarPdfAsync(Long facturaId) {
        try {
            FacturaEntity factura = facturaRepository.findById(facturaId)
                    .orElseThrow(() -> new BusinessException("Factura no encontrada"));

            String rutaPdf = pdfGeneratorService.generarPdfFactura(factura);

            factura.setRutaPdf(rutaPdf);
            factura.setPdfGenerado(true);
            facturaRepository.save(factura);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Cacheable(value = "facturas", key = "#id")
    public FacturaResponse obtenerFactura(Long id) {
        FacturaEntity factura = facturaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Factura no encontrada"));
        return mapToResponse(factura);
    }

    @Override
    @Cacheable("facturas")
    public List<FacturaResponse> listarFacturas() {
        return facturaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "facturas", key = "#negocioId")
    public List<FacturaResponse> listarPorNegocio(Long negocioId) {
        return facturaRepository.findByNegocioId(negocioId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "facturas", allEntries = true)
    public FacturaResponse cambiarEstado(Long id, String nuevoEstado, String usuario) {
        FacturaEntity factura = facturaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Factura no encontrada"));

        try {
            FacturaEntity.EstadoFactura estado = FacturaEntity.EstadoFactura.valueOf(nuevoEstado.toUpperCase());
            factura.cambiarEstado(estado, usuario);
            FacturaEntity actualizada = facturaRepository.save(factura);
            return mapToResponse(actualizada);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Estado inválido");
        }
    }

    @Override
    @CacheEvict(value = "facturas", allEntries = true)
    public void anularFactura(Long id, String usuario) {
        FacturaEntity factura = facturaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Factura no encontrada"));

        if (factura.getEstado() == FacturaEntity.EstadoFactura.PAGADA) {
            throw new BusinessException("No se puede anular factura pagada");
        }

        factura.setEstado(FacturaEntity.EstadoFactura.ANULADA);
        facturaRepository.save(factura);
    }

    private FacturaResponse mapToResponse(FacturaEntity factura) {
        FacturaResponse response = new FacturaResponse();
        response.setId(factura.getId());
        response.setNumeroFactura(factura.getNumeroFactura());
        response.setFechaEmision(factura.getFechaEmision());
        response.setFechaVencimiento(factura.getFechaVencimiento());

        response.setNombreNegocio(factura.getNegocio().getCodigo());
        ClienteEntity cliente = factura.getNegocio().getCotizacion().getCliente();
        response.setNombreCliente(cliente != null ? cliente.getNombre() : "N/A");
        response.setRutCliente(cliente != null ? cliente.getDocumento() : "N/A");
        response.setEmailCliente(cliente != null ? cliente.getEmail() : "N/A");
        response.setTelefonoCliente(cliente != null ? cliente.getTelefono() : "N/A");

        response.setAnticipo(factura.getAnticipo());
        response.setSubtotalItems(factura.getSubtotalItems());
        response.setBaseGravable(factura.calcularBaseGravable());
        response.setIva19(factura.calcularIva());
        response.setTotalAPagar(factura.calcularTotal());

        response.setMedioPago(factura.getMedioPago().getDescripcion());
        response.setReferenciaPago(factura.getReferenciaPago());

        response.setEstado(factura.getEstado().toString());
        response.setPdfGenerado(factura.getPdfGenerado());
        response.setRutaPdf(factura.getRutaPdf());

        response.setNotas(factura.getNotas());
        response.setCondicionesPago(factura.getCondicionesPago());

        response.setLineas(factura.getLineas().stream()
                .map(linea -> {
                    FacturaResponse.LineaResponse lineaResp = new FacturaResponse.LineaResponse();
                    lineaResp.setId(linea.getId());
                    lineaResp.setNumeroLinea(linea.getNumeroLinea());
                    lineaResp.setDescripcion(linea.getDescripcion());
                    lineaResp.setCantidad(linea.getCantidad());
                    lineaResp.setValorUnitario(linea.getValorUnitario());
                    lineaResp.setValorLinea(linea.getValorLinea());
                    return lineaResp;
                })
                .collect(Collectors.toList()));

        return response;
    }
}
