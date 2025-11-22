package com.uptc.queenscorner.services.impl;

import com.uptc.queenscorner.models.dtos.responses.SystemStats;
import com.uptc.queenscorner.repositories.*;
import com.uptc.queenscorner.services.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de administración
 * Maneja operaciones de mantenimiento del sistema
 */
@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private ICotizacionRepository cotizacionRepository;

    @Autowired
    private INegocioRepository negocioRepository;

    @Autowired
    private IFacturaRepository facturaRepository;

    @Autowired
    private IItemCotizacionRepository itemCotizacionRepository;

    @Autowired
    private ILineaFacturaRepository lineaFacturaRepository;

    @Override
    @Transactional
    public void cleanAllData() {
        // Limpiar en orden inverso a las dependencias para evitar errores de FK

        // Limpiar entidades dependientes primero
        lineaFacturaRepository.deleteAll();
        itemCotizacionRepository.deleteAll();

        // Limpiar entidades principales
        facturaRepository.deleteAll();
        cotizacionRepository.deleteAll();
        negocioRepository.deleteAll();
        clienteRepository.deleteAll();

        // Nota: No limpiamos usuarios para mantener al menos un admin
        // Nota: facturas_auditoria se limpia automáticamente por FK cascade
    }

    @Override
    public SystemStats getSystemStats() {
        return new SystemStats(
            clienteRepository.count(),
            cotizacionRepository.count(),
            negocioRepository.count(),
            facturaRepository.count(),
            1 // Siempre hay al menos 1 usuario (admin)
        );
    }
}