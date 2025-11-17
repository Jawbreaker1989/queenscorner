package com.uptc.queenscorner.services.async;

import com.uptc.queenscorner.models.entities.*;
import com.uptc.queenscorner.repositories.IOrdenTrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

/**
 * Servicio que demuestra el flujo completo con los nuevos estados y PDFs mejorados
 * Implementa el flujo: COTIZACIÓN → NEGOCIO → ORDEN TRABAJO → FACTURA → PAGO
 */
@Service
public class NotificacionAsyncService {

    @Autowired
    private PdfAsyncService pdfAsyncService;

    @Autowired
    private IOrdenTrabajoRepository ordenTrabajoRepository;

    @Async
    public CompletableFuture<Boolean> notificarOrdenLista(OrdenTrabajoEntity orden) {
        try {
            // Generar PDF de notificación usando el nuevo método
            CompletableFuture<String> pdfFuture = pdfAsyncService.generarPdfOrdenTrabajo(orden);
            String rutaPdf = pdfFuture.get(); // Espera a que se complete
            
            // Guardar la ruta del PDF en la entidad
            orden.setRutaPdfNotificacion(rutaPdf);
            ordenTrabajoRepository.save(orden);
            
            System.out.println("✅ Notificación procesada para orden " + orden.getCodigo() + 
                             " - PDF guardado en: " + rutaPdf);
            
            Thread.sleep(500); // Simulación de procesamiento adicional
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Procesa una cotización APROBADA creando el negocio y generando notificaciones
     */
    @Async
    public CompletableFuture<Boolean> procesarCotizacionAprobada(CotizacionEntity cotizacion) {
        try {
            System.out.println("🔄 Procesando cotización APROBADA: " + cotizacion.getCodigo());
            
            // 1. Generar PDF de la cotización aprobada
            CompletableFuture<String> pdfCotizacion = pdfAsyncService.generarPdfCotizacion(cotizacion);
            
            // Esperar que la tarea async se complete
            pdfCotizacion.get();
            
            System.out.println("✅ Cotización " + cotizacion.getCodigo() + " procesada exitosamente");
            return CompletableFuture.completedFuture(true);
            
        } catch (Exception e) {
            System.err.println("❌ Error procesando cotización: " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Procesa una orden de trabajo FINALIZADA generando notificaciones
     */
    @Async
    public CompletableFuture<Boolean> procesarOrdenFinalizada(OrdenTrabajoEntity orden) {
        try {
            System.out.println("🔄 Procesando orden FINALIZADA: " + orden.getCodigo());
            
            // 1. Generar PDF de la orden de trabajo
            CompletableFuture<String> pdfOrden = pdfAsyncService.generarPdfOrdenTrabajo(orden);
            
            // Esperar que la tarea async se complete
            pdfOrden.get();
            
            System.out.println("✅ Orden " + orden.getCodigo() + " procesada exitosamente");
            return CompletableFuture.completedFuture(true);
            
        } catch (Exception e) {
            System.err.println("❌ Error procesando orden: " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Procesa una factura creada generando PDF y enviando notificaciones
     */
    @Async
    public CompletableFuture<Boolean> procesarFacturaCreada(FacturaEntity factura) {
        try {
            System.out.println("🔄 Procesando factura PENDIENTE: " + factura.getCodigo());
            
            // 1. Generar PDF de la factura con items incluidos
            CompletableFuture<String> pdfFactura = pdfAsyncService.generarFacturaPdfAsync(factura);
            
            // Esperar que el PDF se genere
            String rutaPdf = pdfFactura.get();
            
            System.out.println("✅ Factura " + factura.getCodigo() + " procesada exitosamente");
            System.out.println("📄 PDF generado: " + rutaPdf);
            
            return CompletableFuture.completedFuture(true);
            
        } catch (Exception e) {
            System.err.println("❌ Error procesando factura: " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Procesa un pago confirmado generando comprobante
     */
    @Async
    public CompletableFuture<Boolean> procesarPagoConfirmado(PagoEntity pago) {
        try {
            System.out.println("🔄 Procesando pago confirmado: " + pago.getId());
            
            // 1. Generar PDF del comprobante de pago
            CompletableFuture<String> pdfComprobante = pdfAsyncService.generarPdfComprobantePago(pago);
            
            // Esperar que el PDF se genere
            String rutaPdf = pdfComprobante.get();
            
            System.out.println("✅ Pago procesado exitosamente");
            System.out.println("📄 Comprobante generado: " + rutaPdf);
            
            return CompletableFuture.completedFuture(true);
            
        } catch (Exception e) {
            System.err.println("❌ Error procesando pago: " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Ejemplo de flujo completo: desde cotización hasta pago
     */
    @Async
    public CompletableFuture<String> ejemploFlujoCompleto() {
        try {
            StringBuilder resultado = new StringBuilder();
            
            resultado.append("🔄 FLUJO COMPLETO CORREGIDO - INCLUYENDO ITEMS\n");
            resultado.append("=".repeat(60)).append("\n\n");
            
            resultado.append("📄 PASO 1: COTIZACIÓN\n");
            resultado.append("• Estado inicial: BORRADOR\n");
            resultado.append("• Incluye items detallados (descripción, cantidad, precio)\n");
            resultado.append("• Estado final: APROBADA → Genera PDF + Email\n\n");
            
            resultado.append("🏢 PASO 2: NEGOCIO\n");
            resultado.append("• Estado inicial: FINALIZADO (El cliente ya procedió)\n");
            resultado.append("• Hereda items y totales de la cotización\n");
            resultado.append("• Estado final: FINALIZADO → Continúa flujo\n\n");
            
            resultado.append("🛠️ PASO 3: ORDEN TRABAJO\n");
            resultado.append("• Estado inicial: EN_PROCESO (En taller/producción)\n");
            resultado.append("• Usa items como guía de trabajo/producción\n");
            resultado.append("• Estado final: FINALIZADA → Genera PDF + Notificación\n\n");
            
            resultado.append("🧾 PASO 4: FACTURA\n");
            resultado.append("• Estado inicial: PENDIENTE (Generada, esperando pago)\n");
            resultado.append("• Incluye items en el PDF generado async\n");
            resultado.append("• Estado final: PAGADA → Proceso completado\n\n");
            
            resultado.append("💰 PASO 5: PAGO\n");
            resultado.append("• Registrado - referencia a items facturados\n");
            resultado.append("• Genera comprobante de pago con detalle de items\n");
            resultado.append("• Marca la factura como PAGADA\n\n");
            
            resultado.append("✅ FLUJO COMPLETADO CON ÉXITO\n");
            resultado.append("📋 Estados simplificados y más claros\n");
            resultado.append("📄 PDFs con contenido completo incluyendo items\n");
            resultado.append("🔄 Procesamiento asíncrono optimizado\n");
            
            System.out.println(resultado.toString());
            return CompletableFuture.completedFuture(resultado.toString());
            
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}