package com.uptc.queenscorner.services;

import com.uptc.queenscorner.models.dtos.requests.NegocioRequest;
import com.uptc.queenscorner.models.dtos.responses.NegocioResponse;
import java.util.List;

public interface INegocioService {
    List<NegocioResponse> findAll();
    NegocioResponse findById(Long id);
    NegocioResponse findByCodigo(String codigo);
    NegocioResponse create(NegocioRequest request);
    NegocioResponse crearDesdeAprobada(Long cotizacionId, NegocioRequest request);
    NegocioResponse update(Long id, NegocioRequest request);
    NegocioResponse cambiarEstado(Long id, String estado);
    List<NegocioResponse> findByEstado(String estado);
} 