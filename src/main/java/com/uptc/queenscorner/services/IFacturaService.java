package com.uptc.queenscorner.services;

import com.uptc.queenscorner.dtos.AgregarLineaRequest;
import com.uptc.queenscorner.dtos.CrearFacturaRequest;
import com.uptc.queenscorner.dtos.FacturaResponse;

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
