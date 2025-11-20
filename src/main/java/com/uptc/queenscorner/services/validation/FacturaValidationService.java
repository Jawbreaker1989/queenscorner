package com.uptc.queenscorner.services.validation;

import com.uptc.queenscorner.models.entities.NegocioEntity;
import com.uptc.queenscorner.models.entities.FacturaEntity;
import org.springframework.stereotype.Service;

/**
 * Servicio de validación centralizado para facturas.
 * Garantiza consistencia en las validaciones en todo el sistema.
 */
@Service
public class FacturaValidationService {

    /**
     * Valida que un negocio pueda generar una factura
     * @param negocio Negocio a validar
     * @throws RuntimeException si el negocio no cumple los requisitos
     */
    public void validarNegocioParaFactura(NegocioEntity negocio) {
        if (negocio == null) {
            throw new RuntimeException("Negocio no encontrado");
        }

        if (negocio.getEstado() != NegocioEntity.EstadoNegocio.FINALIZADO) {
            throw new RuntimeException("El negocio debe estar en estado FINALIZADO para crear una factura. " +
                    "Estado actual: " + negocio.getEstado());
        }

        if (negocio.getCotizacion() == null) {
            throw new RuntimeException("El negocio no tiene cotización asociada. Es necesaria una cotización para crear la factura.");
        }
    }

    /**
     * Valida que una factura pueda ser enviada
     * @param factura Factura a validar
     * @throws RuntimeException si la factura no cumple los requisitos
     */
    public void validarFacturaParaEnvio(FacturaEntity factura) {
        if (factura == null) {
            throw new RuntimeException("Factura no encontrada");
        }

        if (factura.getEstado() != FacturaEntity.EstadoFactura.EN_REVISION) {
            throw new RuntimeException("Solo facturas en estado EN_REVISION pueden ser enviadas. Estado actual: " + factura.getEstado());
        }

        if (factura.getLineas() == null || factura.getLineas().isEmpty()) {
            throw new RuntimeException("La factura debe tener al menos una línea para ser enviada");
        }

        if (factura.getNegocio() == null) {
            throw new RuntimeException("La factura no tiene negocio asociado");
        }
    }

    /**
     * Valida que una factura pueda recibir líneas adicionales
     * @param factura Factura a validar
     * @throws RuntimeException si la factura no puede recibir líneas
     */
    public void validarFacturaParaAgregarLinea(FacturaEntity factura) {
        if (factura == null) {
            throw new RuntimeException("Factura no encontrada");
        }

        if (factura.getEstado() != FacturaEntity.EstadoFactura.EN_REVISION) {
            throw new RuntimeException("No se pueden agregar líneas a factura que no está en EN_REVISION. Estado actual: " + factura.getEstado());
        }
    }

    /**
     * Valida que una factura tenga datos mínimos válidos
     * @param factura Factura a validar
     * @throws RuntimeException si la factura no tiene datos válidos
     */
    public void validarDatosMinimosFactura(FacturaEntity factura) {
        if (factura.getNumeroFactura() == null || factura.getNumeroFactura().trim().isEmpty()) {
            throw new RuntimeException("La factura debe tener un número de factura");
        }

        if (factura.getNegocio() == null) {
            throw new RuntimeException("La factura debe estar asociada a un negocio");
        }

        if (factura.getEstado() == null) {
            throw new RuntimeException("La factura debe tener un estado definido");
        }

        if (factura.getTotal() == null || factura.getTotal().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new RuntimeException("La factura debe tener un total válido");
        }
    }
}
