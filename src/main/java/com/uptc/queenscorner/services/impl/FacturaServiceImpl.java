package com.uptc.queenscorner.services.impl;

import com.uptc.queenscorner.models.dtos.requests.AgregarLineaRequest;
import com.uptc.queenscorner.models.dtos.requests.CrearFacturaRequest;
import com.uptc.queenscorner.models.dtos.responses.FacturaResponse;
import com.uptc.queenscorner.models.mappers.FacturaMapper;
import com.uptc.queenscorner.models.entities.*;
import com.uptc.queenscorner.repositories.*;
import com.uptc.queenscorner.services.IFacturaService;
import com.uptc.queenscorner.services.async.PdfAsyncService;
import com.uptc.queenscorner.services.validation.FacturaValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de facturas (invoices).
 * 
 * Responsabilidades:
 * - Crear facturas a partir de negocios
 * - Gestionar líneas de factura (agregar/remover)
 * - Cambiar estado de factura
 * - Generar PDFs de forma asincrónica
 * - Validar transiciones y estados
 * 
 * Ciclo de Vida de Factura:
 * - EN_REVISION: Recién creada, editable, líneas modificables
 * - ENVIADA: Emitida, líneas inmutables, PDF generado
 * 
 * Estrategia de Validación:
 * - Usa FacturaValidationService para centralizar reglas
 * - Validaciones: negocio finalizado, factura editable, líneas válidas
 * 
 * Transacción Global: @Transactional en clase
 * - Todas las operaciones se envuelven en transacción
 * - Rollback automático si hay excepciones
 * - readOnly=true para operaciones de lectura
 * 
 * Asincronía:
 * - Generación de PDF: se delega a PdfAsyncService
 * - No bloquea la respuesta de enviarFactura()
 */
@Service
@Transactional
public class FacturaServiceImpl implements IFacturaService {

    @Autowired
    private IFacturaRepository facturaRepository;

    @Autowired
    private INegocioRepository negocioRepository;

    @Autowired
    private FacturaMapper facturaMapper;

    @Autowired
    private PdfAsyncService pdfAsyncService;

    @Autowired
    private FacturaValidationService validationService;

    /**
     * Crea una nueva factura para un negocio.
     * 
     * Precondiciones:
     * - Negocio debe existir
     * - Negocio debe estar FINALIZADO (validado por validationService)
     * - Cotización debe tener total válido
     * 
     * Flujo:
     * 1. Busca negocio por ID
     * 2. Valida que negocio pueda facturarse
     * 3. Crea entidad Factura con estado EN_REVISION
     * 4. Copia anticipo del negocio
     * 5. Agrega líneas desde request
     * 6. Calcula totales (subtotal, IVA, total)
     * 7. Persiste factura completa
     * 8. Invalida caché
     * 
     * Líneas de Factura:
     * - Se crean con numeroLinea secuencial
     * - Cada línea tiene descripción, cantidad, valorUnitario
     * - Subtotal de línea = cantidad * valorUnitario
     * 
     * @param request DTO con negocioId, líneas, observaciones
     * @param usuario Usuario que crea (para auditoría)
     * @return DTO con factura creada incluyendo número generado
     * @throws RuntimeException si negocio no existe o no puede facturarse
     */
    @Override
    @CacheEvict(value = "facturas", allEntries = true)
    public FacturaResponse crearFactura(CrearFacturaRequest request, String usuario) {
        NegocioEntity negocio = negocioRepository.findById(request.getNegocioId())
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        // VALIDACIÓN CENTRALIZADA: Solo negocios FINALIZADOS pueden tener factura
        validationService.validarNegocioParaFactura(negocio);

        // Obtener cotización del negocio
        CotizacionEntity cotizacion = negocio.getCotizacion();

        FacturaEntity factura = new FacturaEntity();
        factura.setNegocio(negocio);
        factura.setCotizacion(cotizacion);
        factura.setUsuarioCreacion(usuario);
        factura.setEstado("ENVIADA");
        factura.setNumeroFactura("FAC-" + System.currentTimeMillis());
        
        // Copiar anticipo del negocio
        if (negocio.getAnticipo() != null) {
            factura.setAnticipo(negocio.getAnticipo());
        }

        FacturaEntity guardada = facturaRepository.save(factura);

        if (request.getLineas() != null && !request.getLineas().isEmpty()) {
            int numeroLinea = 1;
            for (AgregarLineaRequest lineaReq : request.getLineas()) {
                LineaFacturaEntity linea = new LineaFacturaEntity();
                linea.setFactura(guardada);
                linea.setNumeroLinea(numeroLinea++);
                linea.setDescripcion(lineaReq.getDescripcion());
                linea.setCantidad(lineaReq.getCantidad().intValue());
                linea.setValorUnitario(lineaReq.getValorUnitario());
                guardada.getLineas().add(linea);
            }
        }

        guardada.calcularTotales();
        FacturaEntity actualizada = facturaRepository.save(guardada);
        
        return facturaMapper.toResponse(actualizada);
    }

    /**
     * Agrega una nueva línea a una factura existente.
     * 
     * Precondiciones:
     * - Factura debe existir
     * - Factura debe estar EN_REVISION (no modificable si está ENVIADA)
     * - Validado por FacturaValidationService
     * 
     * Flujo:
     * 1. Busca factura por ID
     * 2. Valida que factura sea editable
     * 3. Determina numero de línea (size + 1)
     * 4. Crea nueva LineaFacturaEntity
     * 5. Mapea datos del request
     * 6. Agrega a factura.lineas
     * 7. Recalcula totales
     * 8. Persiste y retorna DTO
     * 
     * Recálculo de Totales:
     * - Suma todos los subtotales de líneas
     * - Calcula IVA sobre subtotal
     * - Total = subtotal + IVA
     * - Recalcula saldoPendiente = total - anticipo
     * 
     * @param facturaId ID de la factura
     * @param request DTO con descripción, cantidad, valorUnitario
     * @return DTO con factura actualizada (incluyendo línea nueva)
     * @throws RuntimeException si factura no existe o no está en EN_REVISION
     */
    @Override
    @CacheEvict(value = "facturas", allEntries = true)
    public FacturaResponse agregarLinea(Long facturaId, AgregarLineaRequest request) {
        FacturaEntity factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        validationService.validarFacturaParaAgregarLinea(factura);

        int numeroLinea = factura.getLineas().size() + 1;
        LineaFacturaEntity linea = new LineaFacturaEntity();
        linea.setFactura(factura);
        linea.setNumeroLinea(numeroLinea);
        linea.setDescripcion(request.getDescripcion());
        linea.setCantidad(request.getCantidad().intValue());
        linea.setValorUnitario(request.getValorUnitario());
        
        factura.getLineas().add(linea);
        factura.calcularTotales();
        
        FacturaEntity actualizada = facturaRepository.save(factura);
        return facturaMapper.toResponse(actualizada);
    }

    /**
     * Remueve una línea de una factura existente.
     * 
     * Precondiciones:
     * - Factura debe existir
     * - Factura debe estar EN_REVISION
     * - Línea debe existir en la factura
     * 
     * Flujo:
     * 1. Busca factura por ID
     * 2. Valida que factura sea editable
     * 3. Remueve línea por ID
     * 4. Recalcula totales
     * 5. Persiste y retorna DTO
     * 
     * @param facturaId ID de la factura
     * @param lineaId ID de la línea a remover
     * @return DTO con factura actualizada (línea removida, totales recalculados)
     * @throws RuntimeException si factura no existe o no está en EN_REVISION
     */
    @Override
    @CacheEvict(value = "facturas", allEntries = true)
    public FacturaResponse removerLinea(Long facturaId, Long lineaId) {
        FacturaEntity factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        validationService.validarFacturaParaAgregarLinea(factura);

        factura.getLineas().removeIf(l -> l.getId().equals(lineaId));
        factura.calcularTotales();
        
        FacturaEntity actualizada = facturaRepository.save(factura);
        return facturaMapper.toResponse(actualizada);
    }

    /**
     * Envía/emite una factura.
     * 
     * Transición: EN_REVISION → ENVIADA
     * 
     * Precondiciones:
     * - Factura debe existir
     * - Factura debe estar EN_REVISION
     * - Factura debe tener al menos 1 línea (validado)
     * 
     * Flujo:
     * 1. Busca factura por ID
     * 2. Valida que pueda enviarse
     * 3. Cambia estado a ENVIADA
     * 4. Registra usuario y fecha de envío
     * 5. Persiste
     * 6. Dispara PdfAsyncService para generar PDF en background
     * 7. Retorna DTO inmediatamente (no espera PDF)
     * 
     * Generación de PDF:
     * - Asincrónica: no bloquea respuesta
     * - Una vez completada, actualiza factura.pathPdf
     * - Cliente puede consultar pathPdf en queries posteriores
     * 
     * @param facturaId ID de la factura a enviar
     * @param usuario Usuario que envía (para auditoría)
     * @return DTO con factura en estado ENVIADA
     * @throws RuntimeException si factura no existe, no está en EN_REVISION, o no tiene líneas
     */
    @Override
    @CacheEvict(value = "facturas", allEntries = true)
    public FacturaResponse enviarFactura(Long facturaId, String usuario) {
        FacturaEntity factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        validationService.validarFacturaParaEnvio(factura);

        factura.cambiarAEnviada(usuario);
        FacturaEntity actualizada = facturaRepository.save(factura);
        
        pdfAsyncService.generarPdfFacturaAsync(actualizada);
        
        return facturaMapper.toResponse(actualizada);
    }

    /**
     * Obtiene una factura completa por ID.
     * 
     * Incluye:
     * - Todos los datos de la factura
     * - Todas las líneas de detalle
     * - Información del negocio asociado
     * - Información del cliente (via negocio)
     * 
     * Transacción: readOnly=true (solo lectura, sin modificaciones)
     * 
     * @param id ID de la factura
     * @return DTO con datos completos de la factura
     * @throws RuntimeException si factura no existe
     */
    @Override
    @Transactional(readOnly = true)
    public FacturaResponse obtenerFactura(Long id) {
        FacturaEntity factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        return facturaMapper.toResponse(factura);
    }

    /**
     * Obtiene todas las facturas del sistema.
     * 
     * Retorna facturas en TODOS los estados (EN_REVISION, ENVIADA).
     * 
     * Caché:
     * - Clave: 'facturas' (genérica para todas las listas)
     * - Se invalida en cualquier modificación
     * 
     * Transacción: readOnly=true
     * 
     * @return Lista de DTOs de todas las facturas
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "facturas")
    public List<FacturaResponse> listarFacturas() {
        return facturaRepository.findAll().stream()
                .map(facturaMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las facturas asociadas a un negocio específico.
     * 
     * Uso: Cuando se consultan las facturas de un proyecto específico.
     * 
     * @param negocioId ID del negocio
     * @return Lista de facturas del negocio
     */
    @Override
    @Transactional(readOnly = true)
    public List<FacturaResponse> listarPorNegocio(Long negocioId) {
        return facturaRepository.findByNegocioId(negocioId).stream()
                .map(facturaMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un resumen de la factura con cálculos de totales y saldo.
     * 
     * Nota: Actualmente idéntico a obtenerFactura().
     * Se mantiene separado para permitir diferenciación futura (ej: formato de resumen).
     * 
     * @param facturaId ID de la factura
     * @return Factura con resumen de totales
     */
    @Override
    @Transactional(readOnly = true)
    public FacturaResponse obtenerResumen(Long facturaId) {
        return obtenerFactura(facturaId);
    }
}
