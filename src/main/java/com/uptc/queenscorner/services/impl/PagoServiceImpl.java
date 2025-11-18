package com.uptc.queenscorner.services.impl;

import com.uptc.queenscorner.models.dtos.requests.PagoRequest;
import com.uptc.queenscorner.models.dtos.responses.PagoResponse;
import com.uptc.queenscorner.models.entities.NegocioEntity;
import com.uptc.queenscorner.models.entities.PagoEntity;
import com.uptc.queenscorner.models.mappers.PagoMapper;
import com.uptc.queenscorner.repositories.INegocioRepository;
import com.uptc.queenscorner.repositories.IPagoRepository;
import com.uptc.queenscorner.services.IPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoServiceImpl implements IPagoService {

    @Autowired
    private IPagoRepository pagoRepository;

    @Autowired
    private INegocioRepository negocioRepository;

    @Autowired
    private PagoMapper pagoMapper;

    @Override
    public List<PagoResponse> findAll() {
        return pagoRepository.findAll().stream()
                .map(pagoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PagoResponse findById(Long id) {
        PagoEntity pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return pagoMapper.toResponse(pago);
    }

    @Override
    public List<PagoResponse> findByNegocioId(Long negocioId) {
        return pagoRepository.findByNegocioId(negocioId).stream()
                .map(pagoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PagoResponse create(PagoRequest request) {
        NegocioEntity negocio = negocioRepository.findById(request.getNegocioId())
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        PagoEntity pago = pagoMapper.toEntity(request);
        pago.setNegocio(negocio);

        PagoEntity saved = pagoRepository.save(pago);
        actualizarSaldoNegocio(negocio);
        return pagoMapper.toResponse(saved);
    }

    private void actualizarSaldoNegocio(NegocioEntity negocio) {
        BigDecimal totalPagado = pagoRepository.findByNegocioId(negocio.getId()).stream()
                .map(PagoEntity::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        negocio.setAnticipo(totalPagado);
        negocioRepository.save(negocio);
    }

    @Override
    public void delete(Long id) {
        PagoEntity pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        
        NegocioEntity negocio = pago.getNegocio();
        pagoRepository.delete(pago);
        actualizarSaldoNegocio(negocio);
    }
}