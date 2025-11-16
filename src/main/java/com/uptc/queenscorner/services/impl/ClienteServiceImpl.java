package com.uptc.queenscorner.services.impl;

import com.uptc.queenscorner.models.dtos.requests.ClienteRequest;
import com.uptc.queenscorner.models.dtos.responses.ClienteResponse;
import com.uptc.queenscorner.models.entities.ClienteEntity;
import com.uptc.queenscorner.models.mappers.ClienteMapper;
import com.uptc.queenscorner.repositories.IClienteRepository;
import com.uptc.queenscorner.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    @Override
    @Cacheable(value = "clientes", key = "'allActive'")
    public List<ClienteResponse> findAllActive() {
        return clienteRepository.findByActivoTrue().stream()
                .map(clienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "clientes", key = "#id")
    public ClienteResponse findById(Long id) {
        ClienteEntity cliente = clienteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return clienteMapper.toResponse(cliente);
    }

    @Override
    @CacheEvict(value = "clientes", allEntries = true)
    public ClienteResponse create(ClienteRequest request) {
        ClienteEntity cliente = clienteMapper.toEntity(request);
        ClienteEntity saved = clienteRepository.save(cliente);
        return clienteMapper.toResponse(saved);
    }

    @Override
    @CacheEvict(value = "clientes", allEntries = true)
    public ClienteResponse update(Long id, ClienteRequest request) {
        ClienteEntity cliente = clienteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        clienteMapper.updateEntityFromRequest(request, cliente);
        ClienteEntity updated = clienteRepository.save(cliente);
        return clienteMapper.toResponse(updated);
    }

    @Override
    @CacheEvict(value = "clientes", allEntries = true)
    public void delete(Long id) {
        ClienteEntity cliente = clienteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }
}