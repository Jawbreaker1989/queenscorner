package com.uptc.queenscorner.services;

import com.uptc.queenscorner.models.dtos.requests.PagoRequest;
import com.uptc.queenscorner.models.dtos.responses.PagoResponse;
import java.util.List;

public interface IPagoService {
    List<PagoResponse> findAll();
    PagoResponse findById(Long id);
    List<PagoResponse> findByNegocioId(Long negocioId);
    PagoResponse create(PagoRequest request);
    void delete(Long id);
}