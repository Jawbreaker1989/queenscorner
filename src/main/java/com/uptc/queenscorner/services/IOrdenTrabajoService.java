package com.uptc.queenscorner.services;

import com.uptc.queenscorner.models.dtos.requests.OrdenTrabajoRequest;
import com.uptc.queenscorner.models.dtos.responses.OrdenTrabajoResponse;
import java.util.List;

public interface IOrdenTrabajoService {
    List<OrdenTrabajoResponse> findAll();
    OrdenTrabajoResponse findById(Long id);
    OrdenTrabajoResponse findByCodigo(String codigo);
    OrdenTrabajoResponse create(OrdenTrabajoRequest request);
    OrdenTrabajoResponse update(Long id, OrdenTrabajoRequest request);
    OrdenTrabajoResponse cambiarEstado(Long id, String estado);
    List<OrdenTrabajoResponse> findByEstado(String estado);
    List<OrdenTrabajoResponse> findByPrioridad(String prioridad);
}