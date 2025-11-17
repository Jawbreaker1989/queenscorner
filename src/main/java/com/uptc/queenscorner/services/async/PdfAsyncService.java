package com.uptc.queenscorner.services.async;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.uptc.queenscorner.models.entities.*;
import com.uptc.queenscorner.utils.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.File;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class PdfAsyncService {

    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Async
    public CompletableFuture<String> generarPdfCotizacion(CotizacionEntity cotizacion) {
        try {
            Thread.sleep(1000); // Simula procesamiento
            
            String directorioCotizaciones = FileUtils.getRutaCotizaciones();
            FileUtils.crearDirectorioSiNoExiste(directorioCotizaciones);
            
            String nombreArchivo = "cotizacion-" + cotizacion.getCodigo() + ".pdf";
            String rutaCompleta = directorioCotizaciones + File.separator + nombreArchivo;
            
            // Generar PDF con contenido completo incluyendo items
            generarPdfCotizacionCompleto(cotizacion, rutaCompleta);
            
            System.out.println("📄 PDF COTIZACIÓN generado: " + rutaCompleta);
            return CompletableFuture.completedFuture(rutaCompleta);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<String> generarPdfOrdenTrabajo(OrdenTrabajoEntity orden) {
        try {
            Thread.sleep(1500); // Simula procesamiento
            
            String directorioOrdenes = FileUtils.getRutaNotificaciones();
            FileUtils.crearDirectorioSiNoExiste(directorioOrdenes);
            
            String nombreArchivo = "orden-trabajo-" + orden.getCodigo() + ".pdf";
            String rutaCompleta = directorioOrdenes + File.separator + nombreArchivo;
            
            // Generar PDF con especificaciones de producción
            generarPdfOrdenTrabajoCompleto(orden, rutaCompleta);
            
            System.out.println("📋 PDF ORDEN DE TRABAJO generado: " + rutaCompleta);
            return CompletableFuture.completedFuture(rutaCompleta);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<String> generarFacturaPdfAsync(FacturaEntity factura) {
        try {
            Thread.sleep(2000); // Simula procesamiento
            
            String directorioFacturas = FileUtils.getRutaFacturas();
            FileUtils.crearDirectorioSiNoExiste(directorioFacturas);
            
            String nombreArchivo = "factura-" + factura.getCodigo() + ".pdf";
            String rutaCompleta = directorioFacturas + File.separator + nombreArchivo;
            
            // Generar PDF con contenido completo incluyendo items
            generarPdfFacturaCompleto(factura, rutaCompleta);
            
            System.out.println("🧾 PDF FACTURA generado: " + rutaCompleta);
            return CompletableFuture.completedFuture(rutaCompleta);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<String> generarPdfComprobantePago(PagoEntity pago) {
        try {
            Thread.sleep(800); // Simula procesamiento
            
            String directorioComprobantes = FileUtils.getRutaComprobantes();
            FileUtils.crearDirectorioSiNoExiste(directorioComprobantes);
            
            String nombreArchivo = "comprobante-pago-" + pago.getId() + ".pdf";
            String rutaCompleta = directorioComprobantes + File.separator + nombreArchivo;
            
            // Generar PDF con detalle completo del pago
            generarPdfComprobantePagoCompleto(pago, rutaCompleta);
            
            System.out.println("✅ PDF COMPROBANTE DE PAGO generado: " + rutaCompleta);
            return CompletableFuture.completedFuture(rutaCompleta);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    // =================== MÉTODOS PRIVADOS PARA GENERACIÓN DE PDFs ===================

    private void generarPdfCotizacionCompleto(CotizacionEntity cotizacion, String rutaArchivo) {
        try (PdfWriter writer = new PdfWriter(rutaArchivo);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {
            
            // Encabezado de la cotización
            document.add(new Paragraph("📄 COTIZACIÓN #" + cotizacion.getCodigo())
                        .setBold().setFontSize(18));
            document.add(new Paragraph("=".repeat(50)));
            document.add(new Paragraph("CLIENTE: " + cotizacion.getCliente().getNombre()));
            document.add(new Paragraph("FECHA: " + cotizacion.getFechaCreacion().format(formatoFecha)));
            
            if (cotizacion.getFechaValidez() != null) {
                document.add(new Paragraph("VÁLIDA HASTA: " + cotizacion.getFechaValidez().format(formatoFecha)));
            }
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("DETALLE DE PRODUCTOS/SERVICIOS:").setBold());
            document.add(new Paragraph("-".repeat(40)));
            
            // Mostrar items de la cotización
            List<ItemCotizacionEntity> items = cotizacion.getItems();
            if (items != null && !items.isEmpty()) {
                for (ItemCotizacionEntity item : items) {
                    document.add(new Paragraph("• " + item.getDescripcion() +
                        " - " + item.getCantidad() + " unidades × $" + formatearMonto(item.getPrecioUnitario()) +
                        " = $" + formatearMonto(item.getSubtotal())));
                }
            } else {
                document.add(new Paragraph("• " + (cotizacion.getDescripcion() != null ? 
                    cotizacion.getDescripcion() : "Servicios varios")));
            }
            
            // Resumen financiero
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("RESUMEN FINANCIERO:").setBold());
            document.add(new Paragraph("-".repeat(20)));
            document.add(new Paragraph("Subtotal: $" + formatearMonto(cotizacion.getSubtotal())));
            document.add(new Paragraph("Impuestos (19%): $" + formatearMonto(cotizacion.getImpuestos())));
            document.add(new Paragraph("TOTAL: $" + formatearMonto(cotizacion.getTotal())).setBold());
            
            // Condiciones
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("CONDICIONES:").setBold());
            document.add(new Paragraph("- Precios válidos por 30 días"));
            document.add(new Paragraph("- Tiempo de entrega: 15 días hábiles"));
            document.add(new Paragraph("- Garantía: 1 año por defectos de fabricación"));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("FIRMA CLIENTE: ___________________"));
            
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de cotización", e);
        }
    }

    private void generarPdfOrdenTrabajoCompleto(OrdenTrabajoEntity orden, String rutaArchivo) {
        try (PdfWriter writer = new PdfWriter(rutaArchivo);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {
            
            // Encabezado de la orden
            document.add(new Paragraph("📋 ORDEN DE TRABAJO #" + orden.getCodigo())
                        .setBold().setFontSize(18));
            document.add(new Paragraph("=".repeat(50)));
            document.add(new Paragraph("CLIENTE: " + orden.getNegocio().getCotizacion().getCliente().getNombre()));
            document.add(new Paragraph("NEGOCIO: #" + orden.getNegocio().getCodigo()));
            document.add(new Paragraph("FECHA CREACIÓN: " + orden.getFechaCreacion().format(formatoFecha)));
            document.add(new Paragraph("PRIORIDAD: " + orden.getPrioridad()));
            document.add(new Paragraph("ESTADO: " + orden.getEstado()));
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("ESPECIFICACIONES DE PRODUCCIÓN:").setBold());
            document.add(new Paragraph("-".repeat(35)));
            document.add(new Paragraph("PRODUCTOS A FABRICAR/ENTREGAR:"));
            
            // Mostrar items de la cotización asociada
            List<ItemCotizacionEntity> items = orden.getNegocio().getCotizacion().getItems();
            if (items != null && !items.isEmpty()) {
                int contador = 1;
                for (ItemCotizacionEntity item : items) {
                    document.add(new Paragraph(contador + ". " + item.getDescripcion().toUpperCase() +
                        " (" + item.getCantidad() + " unidades)").setBold());
                    document.add(new Paragraph("   - Precio unitario: $" + formatearMonto(item.getPrecioUnitario())));
                    document.add(new Paragraph("   - Subtotal: $" + formatearMonto(item.getSubtotal())));
                    document.add(new Paragraph(""));
                    contador++;
                }
            } else {
                document.add(new Paragraph("1. " + (orden.getDescripcion() != null ? 
                    orden.getDescripcion() : "Trabajo general")));
            }
            
            // Plazos estimados
            document.add(new Paragraph("PLAZOS ESTIMADOS:").setBold());
            document.add(new Paragraph("-".repeat(18)));
            if (orden.getFechaInicioEstimada() != null) {
                document.add(new Paragraph("Inicio: " + orden.getFechaInicioEstimada().format(formatoFecha)));
            }
            if (orden.getFechaFinEstimada() != null) {
                document.add(new Paragraph("Finalización: " + orden.getFechaFinEstimada().format(formatoFecha)));
            }
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("RESPONSABLE TALLER: ___________________"));
            
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de orden de trabajo", e);
        }
    }

    private void generarPdfFacturaCompleto(FacturaEntity factura, String rutaArchivo) {
        try (PdfWriter writer = new PdfWriter(rutaArchivo);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {
            
            // Encabezado de la factura
            document.add(new Paragraph("🧾 FACTURA #" + factura.getCodigo())
                        .setBold().setFontSize(18));
            document.add(new Paragraph("=".repeat(40)));
            document.add(new Paragraph("EMISOR: Queen's Corner Gallery"));
            document.add(new Paragraph("NIT: 900.123.456-7"));
            document.add(new Paragraph("DIRECCIÓN: Av. Principal #123, Ciudad"));
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("CLIENTE: " + factura.getNegocio().getCotizacion().getCliente().getNombre()));
            document.add(new Paragraph("EMAIL: " + factura.getNegocio().getCotizacion().getCliente().getEmail()));
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("FECHA EMISIÓN: " + factura.getFechaEmision().format(formatoFecha)));
            document.add(new Paragraph("FECHA VENCIMIENTO: " + factura.getFechaEmision().plusDays(14).format(formatoFecha)));
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("DETALLE DE FACTURACIÓN:").setBold());
            document.add(new Paragraph("-".repeat(25)));
            document.add(new Paragraph("CANT.  DESCRIPCIÓN            PRECIO UNIT.   SUBTOTAL"));
            document.add(new Paragraph("-".repeat(60)));
            
            // Obtener items de la cotización
            List<ItemCotizacionEntity> items = factura.getNegocio().getCotizacion().getItems();
            if (items != null && !items.isEmpty()) {
                for (ItemCotizacionEntity item : items) {
                    document.add(new Paragraph(String.format("%-6d %-20s $%-12s $%s",
                        item.getCantidad(),
                        truncarTexto(item.getDescripcion(), 20),
                        formatearMonto(item.getPrecioUnitario()),
                        formatearMonto(item.getSubtotal())
                    )));
                }
            }
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("RESUMEN FINANCIERO:").setBold());
            document.add(new Paragraph("-".repeat(20)));
            document.add(new Paragraph("Subtotal: $" + formatearMonto(factura.getSubtotal())));
            document.add(new Paragraph("IVA (19%): $" + formatearMonto(factura.getImpuestos())));
            document.add(new Paragraph("TOTAL A PAGAR: $" + formatearMonto(factura.getTotal())).setBold());
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("INFORMACIÓN DE PAGO:").setBold());
            document.add(new Paragraph("-".repeat(20)));
            document.add(new Paragraph("Banco: Banco Nacional"));
            document.add(new Paragraph("Cuenta: 123-456789-01"));
            document.add(new Paragraph("Tipo: Cuenta Corriente"));
            document.add(new Paragraph("Referencia: " + factura.getCodigo()));
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("ESTADO: " + factura.getEstado()).setBold());
            
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de factura", e);
        }
    }

    private void generarPdfComprobantePagoCompleto(PagoEntity pago, String rutaArchivo) {
        try (PdfWriter writer = new PdfWriter(rutaArchivo);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {
            
            // Encabezado del comprobante
            document.add(new Paragraph("✅ COMPROBANTE DE PAGO #" + pago.getId())
                        .setBold().setFontSize(18));
            document.add(new Paragraph("=".repeat(50)));
            document.add(new Paragraph("CLIENTE: " + 
                pago.getNegocio().getCotizacion().getCliente().getNombre()));
            document.add(new Paragraph("NEGOCIO: #" + pago.getNegocio().getCodigo()));
            document.add(new Paragraph("FECHA PAGO: " + pago.getFechaPago().format(formatoFecha)));
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("DETALLE DE TRANSACCIÓN:").setBold());
            document.add(new Paragraph("-".repeat(25)));
            document.add(new Paragraph("MÉTODO DE PAGO: " + pago.getMetodoPago()));
            document.add(new Paragraph("REFERENCIA: " + (pago.getReferencia() != null ? pago.getReferencia() : "N/A")));
            document.add(new Paragraph("MONTO: $" + formatearMonto(pago.getMonto())));
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("DETALLE PRODUCTOS/SERVICIOS PAGADOS:").setBold());
            document.add(new Paragraph("-".repeat(35)));
            
            // Mostrar items de la cotización del negocio
            List<ItemCotizacionEntity> items = pago.getNegocio().getCotizacion().getItems();
            if (items != null && !items.isEmpty()) {
                for (ItemCotizacionEntity item : items) {
                    document.add(new Paragraph("• " + item.getDescripcion() +
                        " (" + item.getCantidad() + ") = $" + formatearMonto(item.getSubtotal())));
                }
            }
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("ESTADO: PAGO CONFIRMADO").setBold());
            document.add(new Paragraph("NEGOCIO: FINALIZADO").setBold());
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("GRACIAS POR SU PAGO"));
            document.add(new Paragraph("Queen's Corner Gallery"));
            
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de comprobante de pago", e);
        }
    }

    // =================== MÉTODOS AUXILIARES ===================

    private String formatearMonto(BigDecimal monto) {
        if (monto == null) return "0";
        return String.format("%,.0f", monto.doubleValue());
    }

    private String truncarTexto(String texto, int maxLongitud) {
        if (texto == null) return "";
        return texto.length() > maxLongitud ? texto.substring(0, maxLongitud - 3) + "..." : texto;
    }
}