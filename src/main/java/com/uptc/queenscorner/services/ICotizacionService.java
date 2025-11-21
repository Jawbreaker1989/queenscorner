package com.uptc.queenscorner.services;

import com.uptc.queenscorner.models.dtos.requests.CotizacionRequest;
import com.uptc.queenscorner.models.dtos.responses.CotizacionResponse;
import java.util.List;

public interface ICotizacionService {
    List<CotizacionResponse> findAll();
    CotizacionResponse findById(Long id);
    CotizacionResponse findByCodigo(String codigo);
    CotizacionResponse create(CotizacionRequest request);
    CotizacionResponse update(Long id, CotizacionRequest request);
    CotizacionResponse cambiarEstado(Long id, String estado);
    void delete(Long id);
} 