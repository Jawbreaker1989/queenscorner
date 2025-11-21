package com.uptc.queenscorner.services;

import com.uptc.queenscorner.models.dtos.requests.AgregarLineaRequest;
import com.uptc.queenscorner.models.dtos.requests.CrearFacturaRequest;
import com.uptc.queenscorner.models.dtos.responses.FacturaResponse;

import java.util.List;

public interface IFacturaService {
    FacturaResponse crearFactura(CrearFacturaRequest request, String usuario);
    FacturaResponse agregarLinea(Long facturaId, AgregarLineaRequest request);
    FacturaResponse removerLinea(Long facturaId, Long lineaId);
    FacturaResponse enviarFactura(Long facturaId, String usuario);
    FacturaResponse obtenerFactura(Long id);
    List<FacturaResponse> listarFacturas();
    List<FacturaResponse> listarPorNegocio(Long negocioId);
    FacturaResponse obtenerResumen(Long facturaId);
} 
