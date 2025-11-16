package com.uptc.queenscorner.services;

import com.uptc.queenscorner.models.dtos.requests.FacturaRequest;
import com.uptc.queenscorner.models.dtos.responses.FacturaResponse;
import java.util.List;

public interface IFacturaService {
    List<FacturaResponse> findAll();
    FacturaResponse findById(Long id);
    FacturaResponse findByCodigo(String codigo);
    FacturaResponse create(FacturaRequest request);
    FacturaResponse cambiarEstado(Long id, String estado);
    List<FacturaResponse> findByEstado(String estado);
}