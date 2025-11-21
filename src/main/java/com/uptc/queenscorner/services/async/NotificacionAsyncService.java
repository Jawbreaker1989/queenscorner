package com.uptc.queenscorner.services.async;

import com.uptc.queenscorner.models.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

/**
 * Servicio que procesa eventos asincronos de cotizaciones, negocios y facturas
 * Responsabilidades:
 * - Enviar notificaciones de cotizaciones al cliente
 * - Generar PDFs de cotizaciones aprobadas
 * - Generar PDFs de facturas creadas
 */
@Service
public class NotificacionAsyncService {

    @Autowired
    private PdfAsyncService pdfAsyncService;

    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Simula el envío de cotización al cliente por SMS/WhatsApp
     * Registra un log con: nombre, teléfono y hora de envío
     */
    @Async
    public CompletableFuture<Boolean> enviarCotizacionAlCliente(CotizacionEntity cotizacion) {
        try {
            // Validar datos del cliente
            String nombreCliente = cotizacion.getCliente().getNombre();
            String telefono = cotizacion.getCliente().getTelefono();
            String codigo = cotizacion.getCodigo();
            
            if (telefono == null || telefono.trim().isEmpty()) {
                System.err.println("⚠️ No se puede enviar cotización " + codigo + 
                    ": Cliente sin número de contacto");
                return CompletableFuture.completedFuture(false);
            }
            
            // Registrar envío
            LocalDateTime horaEnvio = LocalDateTime.now();
            System.out.println("\n" + "=".repeat(70));
            System.out.println("📱 LOG DE ENVÍO DE COTIZACIÓN");
            System.out.println("=".repeat(70));
            System.out.println("Código Cotización: " + codigo);
            System.out.println("Cliente: " + nombreCliente);
            System.out.println("Teléfono: " + telefono);
            System.out.println("Hora de Envío: " + horaEnvio.format(formatoFecha));
            System.out.println("Método: SMS/WhatsApp");
            System.out.println("Estado: ✓ ENVIADO");
            System.out.println("Monto Total: $" + formatearMonto(cotizacion.getTotal()));
            System.out.println("=".repeat(70) + "\n");
            
            return CompletableFuture.completedFuture(true);
            
        } catch (Exception e) {
            System.err.println("❌ Error enviando cotización: " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<Boolean> notificarFacturaCreada(FacturaEntity factura) {
        try {
            System.out.println("✅ Factura " + factura.getNumeroFactura() + " creada exitosamente");
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            System.err.println("❌ Error notificando factura: " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Procesa una cotización APROBADA generando PDF
     */
    @Async
    public CompletableFuture<Boolean> procesarCotizacionAprobada(CotizacionEntity cotizacion) {
        try {
            System.out.println("🔄 Procesando cotización APROBADA: " + cotizacion.getCodigo());
            
            pdfAsyncService.generarPdfCotizacion(cotizacion);
            
            System.out.println("✅ Cotización " + cotizacion.getCodigo() + " procesada exitosamente");
            return CompletableFuture.completedFuture(true);
            
        } catch (Exception e) {
            System.err.println("❌ Error procesando cotización: " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<Boolean> procesarFacturaCreada(FacturaEntity factura) {
        try {
            System.out.println("🔄 Procesando factura: " + factura.getNumeroFactura());
            
            pdfAsyncService.generarPdfFacturaAsync(factura);
            
            System.out.println("✅ Factura " + factura.getNumeroFactura() + " procesada exitosamente");
            
            return CompletableFuture.completedFuture(true);
            
        } catch (Exception e) {
            System.err.println("❌ Error procesando factura: " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    // =================== MÉTODOS AUXILIARES ===================

    private String formatearMonto(java.math.BigDecimal monto) {
        if (monto == null) return "0";
        return String.format("%,.0f", monto.doubleValue());
    }
}  