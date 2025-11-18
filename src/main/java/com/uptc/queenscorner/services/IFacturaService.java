package com.uptc.queenscorner.services;

import com.uptc.queenscorner.models.dtos.requests.CrearFacturaRequest;
import com.uptc.queenscorner.models.dtos.responses.FacturaResponse;

import java.util.List;

public interface IFacturaService {
    FacturaResponse crearFactura(CrearFacturaRequest request, String usuario);
    FacturaResponse emitirFactura(Long id, String usuario);
    FacturaResponse obtenerFactura(Long id);
    List<FacturaResponse> listarFacturas();
    List<FacturaResponse> listarPorNegocio(Long negocioId);
    FacturaResponse cambiarEstado(Long id, String nuevoEstado, String usuario);
    void anularFactura(Long id, String usuario);
}
