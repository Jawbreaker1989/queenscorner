package com.uptc.queenscorner.services.async;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.uptc.queenscorner.models.entities.CotizacionEntity;
import com.uptc.queenscorner.models.entities.FacturaEntity;
import com.uptc.queenscorner.models.entities.ItemCotizacionEntity;
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
    public CompletableFuture<String> generarFacturaPdfAsync(FacturaEntity factura) {
        try {
            Thread.sleep(2000); // Simula procesamiento
            
            String directorioFacturas = FileUtils.getRutaFacturas();
            FileUtils.crearDirectorioSiNoExiste(directorioFacturas);
            
            String nombreArchivo = "factura-" + factura.getNumeroFactura() + ".pdf";
            String rutaCompleta = directorioFacturas + File.separator + nombreArchivo;
            
            // Generar PDF con contenido completo incluyendo items
            generarPdfFacturaCompleto(factura, rutaCompleta);
            
            System.out.println("🧾 PDF FACTURA generado: " + rutaCompleta);
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
                        .setBold().setFontSize(16));
            document.add(new Paragraph("=".repeat(70)));
            
            // Información básica
            document.add(new Paragraph("CLIENTE: " + cotizacion.getCliente().getNombre()).setBold());
            document.add(new Paragraph("EMAIL: " + cotizacion.getCliente().getEmail()));
            document.add(new Paragraph("TELÉFONO: " + (cotizacion.getCliente().getTelefono() != null ? 
                cotizacion.getCliente().getTelefono() : "N/A")));
            document.add(new Paragraph("DIRECCIÓN: " + (cotizacion.getCliente().getDireccion() != null ? 
                cotizacion.getCliente().getDireccion() : "N/A")));
            
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("FECHA CREACIÓN: " + cotizacion.getFechaCreacion().format(formatoFecha)));
            if (cotizacion.getFechaValidez() != null) {
                document.add(new Paragraph("VÁLIDA HASTA: " + cotizacion.getFechaValidez().format(formatoFecha)));
            }
            if (cotizacion.getDescripcion() != null && !cotizacion.getDescripcion().isEmpty()) {
                document.add(new Paragraph("DESCRIPCIÓN: " + cotizacion.getDescripcion()));
            }
            if (cotizacion.getObservaciones() != null && !cotizacion.getObservaciones().isEmpty()) {
                document.add(new Paragraph("OBSERVACIONES: " + cotizacion.getObservaciones()));
            }
            
            // Items de la cotización
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("DETALLE DE PRODUCTOS/SERVICIOS:").setBold());
            document.add(new Paragraph("=".repeat(70)));
            document.add(new Paragraph(String.format("%-5s %-25s %-10s %-12s %-15s", 
                "Nº", "DESCRIPCIÓN", "CANTIDAD", "PRECIO UNI.", "SUBTOTAL")));
            document.add(new Paragraph("-".repeat(70)));
            
            // Mostrar items de la cotización
            List<ItemCotizacionEntity> items = cotizacion.getItems();
            
            if (items != null && !items.isEmpty()) {
                int contador = 1;
                for (ItemCotizacionEntity item : items) {
                    if (item != null) {
                        BigDecimal subtotal = item.getPrecioUnitario() != null ? 
                            item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad() != null ? item.getCantidad() : 0)) :
                            BigDecimal.ZERO;
                        
                        document.add(new Paragraph(String.format("%-5d %-25s %-10d $%-11s $%s",
                            contador,
                            truncarTexto(item.getDescripcion() != null ? item.getDescripcion() : "N/A", 25),
                            item.getCantidad() != null ? item.getCantidad() : 0,
                            formatearMonto(item.getPrecioUnitario() != null ? item.getPrecioUnitario() : BigDecimal.ZERO),
                            formatearMonto(subtotal)
                        )));
                        contador++;
                    }
                }
            } else {
                // Si no hay items, mostrar la descripción general
                document.add(new Paragraph(String.format("%-5d %-25s %-10d $%-11s $%s",
                    1,
                    truncarTexto(cotizacion.getDescripcion() != null ? cotizacion.getDescripcion() : "Servicios varios", 25),
                    1,
                    "0.00",
                    "0.00"
                )));
                document.add(new Paragraph("⚠️ Nota: Items no cargados en esta cotización"));
            }
            
            document.add(new Paragraph("=".repeat(70)));
            
            // Resumen financiero
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("RESUMEN FINANCIERO:").setBold());
            document.add(new Paragraph(String.format("%-45s %s", "Subtotal:", "$" + formatearMonto(cotizacion.getSubtotal()))));
            document.add(new Paragraph(String.format("%-45s %s", "Impuestos (19%):", "$" + formatearMonto(cotizacion.getImpuestos()))));
            document.add(new Paragraph(String.format("%-45s %s", "TOTAL A PAGAR:", "$" + formatearMonto(cotizacion.getTotal()))).setBold());
            
            // Condiciones
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("CONDICIONES Y TÉRMINOS:").setBold());
            document.add(new Paragraph("• Precios válidos por 30 días"));
            document.add(new Paragraph("• Tiempo de entrega: 15 días hábiles"));
            document.add(new Paragraph("• Garantía: 1 año por defectos de fabricación"));
            document.add(new Paragraph("• Pago: 50% anticipo, 50% contra entrega"));
            document.add(new Paragraph("• Estado: " + cotizacion.getEstado()));
            
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Autorizado por:     ___________________     Fecha: ___________________"));
            document.add(new Paragraph("Cliente:            ___________________     Fecha: ___________________"));
            
            System.out.println("✓ PDF COTIZACIÓN generado exitosamente con " + 
                (items != null ? items.size() : 0) + " items");
            
        } catch (Exception e) {
            System.err.println("✗ Error generando PDF de cotización: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF de cotización", e);
        }
    }

    private void generarPdfFacturaCompleto(FacturaEntity factura, String rutaArchivo) {
        try (PdfWriter writer = new PdfWriter(rutaArchivo);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {
            
            // Encabezado de la factura
            document.add(new Paragraph("🧾 FACTURA #" + factura.getNumeroFactura())
                        .setBold().setFontSize(18));
            document.add(new Paragraph("=".repeat(70)));
            
            // Información del emisor
            document.add(new Paragraph("EMISOR: Queen's Corner Gallery").setBold());
            document.add(new Paragraph("NIT: 900.123.456-7"));
            document.add(new Paragraph("DIRECCIÓN: Av. Principal #123, Ciudad"));
            
            // Información del cliente
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("CLIENTE: " + factura.getNegocio().getCotizacion().getCliente().getNombre()).setBold());
            document.add(new Paragraph("EMAIL: " + factura.getNegocio().getCotizacion().getCliente().getEmail()));
            document.add(new Paragraph("TELÉFONO: " + (factura.getNegocio().getCotizacion().getCliente().getTelefono() != null ? 
                factura.getNegocio().getCotizacion().getCliente().getTelefono() : "N/A")));
            document.add(new Paragraph("DIRECCIÓN: " + (factura.getNegocio().getCotizacion().getCliente().getDireccion() != null ? 
                factura.getNegocio().getCotizacion().getCliente().getDireccion() : "N/A")));
            
            // Información de fechas
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("FECHA EMISIÓN: " + factura.getFechaEmision().format(formatoFecha)));
            document.add(new Paragraph("FECHA VENCIMIENTO: " + factura.getFechaVencimiento().format(formatoFecha)));
            
            // Detalles de la factura
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("REFERENCIA: Cotización #" + factura.getCotizacion().getCodigo()));
            document.add(new Paragraph("NEGOCIO: " + factura.getNegocio().getCodigo()));
            
            // Detalle de items
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("DETALLE DE FACTURACIÓN:").setBold());
            document.add(new Paragraph("=".repeat(70)));
            document.add(new Paragraph(String.format("%-5s %-30s %-10s %-12s %-15s", 
                "Nº", "DESCRIPCIÓN", "CANTIDAD", "PRECIO UNI.", "SUBTOTAL")));
            document.add(new Paragraph("-".repeat(70)));
            
            // Obtener items de la cotización
            List<ItemCotizacionEntity> items = factura.getNegocio().getCotizacion().getItems();
            if (items != null && !items.isEmpty()) {
                int contador = 1;
                for (ItemCotizacionEntity item : items) {
                    if (item != null) {
                        BigDecimal subtotal = item.getPrecioUnitario() != null ? 
                            item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad() != null ? item.getCantidad() : 0)) :
                            BigDecimal.ZERO;
                        
                        document.add(new Paragraph(String.format("%-5d %-30s %-10d $%-11s $%s",
                            contador,
                            truncarTexto(item.getDescripcion() != null ? item.getDescripcion() : "N/A", 30),
                            item.getCantidad() != null ? item.getCantidad() : 0,
                            formatearMonto(item.getPrecioUnitario() != null ? item.getPrecioUnitario() : BigDecimal.ZERO),
                            formatearMonto(subtotal)
                        )));
                        contador++;
                    }
                }
            }
            
            document.add(new Paragraph("=".repeat(70)));
            
            // Resumen financiero
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("RESUMEN FINANCIERO:").setBold());
            document.add(new Paragraph(String.format("%-45s %s", "Subtotal:", "$" + formatearMonto(factura.getSubtotalItems()))));
            document.add(new Paragraph(String.format("%-45s %s", "Anticipo:", "$" + formatearMonto(factura.getAnticipo()))));
            document.add(new Paragraph(String.format("%-45s %s", "Base Gravable:", "$" + formatearMonto(factura.getBaseGravable()))));
            document.add(new Paragraph(String.format("%-45s %s", "IVA (19%):", "$" + formatearMonto(factura.getIva19()))));
            document.add(new Paragraph(String.format("%-45s %s", "TOTAL A PAGAR:", "$" + formatearMonto(factura.getTotalAPagar()))).setBold());
            
            // Información de pago
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("CONDICIONES DE PAGO:").setBold());
            if (factura.getCondicionesPago() != null && !factura.getCondicionesPago().isEmpty()) {
                document.add(new Paragraph(factura.getCondicionesPago()));
            } else {
                document.add(new Paragraph("• Plazo: 14 días desde la emisión"));
                document.add(new Paragraph("• Medio de Pago: " + factura.getMedioPago().getDescripcion()));
            }
            
            // Estado de la factura
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("ESTADO: " + factura.getEstado()).setBold());
            
            if (factura.getNotas() != null && !factura.getNotas().isEmpty()) {
                document.add(new Paragraph("\nNOTAS:"));
                document.add(new Paragraph(factura.getNotas()));
            }
            
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("_".repeat(70)));
            document.add(new Paragraph("Autorizado por:     ___________________     Fecha: ___________________"));
            document.add(new Paragraph("Cliente:            ___________________     Fecha: ___________________"));
            
            System.out.println("✓ PDF FACTURA generado exitosamente con " + 
                (items != null ? items.size() : 0) + " items");
            
        } catch (Exception e) {
            System.err.println("✗ Error generando PDF de factura: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF de factura", e);
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