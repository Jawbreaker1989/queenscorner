package com.uptc.queenscorner.services.async;

import com.uptc.queenscorner.models.entities.CotizacionEntity;
import com.uptc.queenscorner.models.entities.FacturaEntity;
import com.uptc.queenscorner.repositories.IFacturaRepository;
import com.uptc.queenscorner.utils.FileUtils;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@Service
public class PdfAsyncService {
    
    @Autowired
    private IFacturaRepository facturaRepository;
    
    private static final DateTimeFormatter FECHA_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    /**
     * Genera PDF de cotización en hilo separado (asincrónico)
     * Se ejecuta solo una vez cuando se aprueba
     */
    public void generarPdfCotizacion(CotizacionEntity cotizacion) {
        new Thread(() -> {
            try {
                System.out.println("⏳ Iniciando generación de PDF para cotización: " + cotizacion.getCodigo());
                Thread.sleep(2000);
                
                FileUtils.inicializarDirectorios();
                
                String nombreArchivo = cotizacion.getCodigo() + ".pdf";
                String rutaCompleta = Paths.get(FileUtils.getRutaCotizaciones(), nombreArchivo).toString();
                
                try (PdfWriter writer = new PdfWriter(rutaCompleta)) {
                    PdfDocument pdfDoc = new PdfDocument(writer);
                    Document doc = new Document(pdfDoc);
                    
                    // LOGO
                    try {
                        String logoPath = "src/main/resources/imagenes/logo.png";
                        Image logo = new Image(com.itextpdf.io.image.ImageDataFactory.create(logoPath));
                        logo.setWidth(80);
                        logo.setHeight(80);
                        doc.add(logo);
                    } catch (Exception e) {
                        System.out.println("⚠️ Logo no encontrado, continuando sin él");
                    }
                    
                    // ENCABEZADO EMPRESA
                    doc.add(new Paragraph("QUEEN'S CORNER GALLERY"));
                    doc.add(new Paragraph("Carrera 10 # 16 - 95, Sogamoso"));
                    doc.add(new Paragraph("NIT: 56789056-6"));
                    doc.add(new Paragraph("\"Diseño y confort a su servicio\""));
                    doc.add(new Paragraph(""));
                    doc.add(new Paragraph("═══════════════════════════════════════════════════════"));
                    doc.add(new Paragraph("COTIZACIÓN"));
                    doc.add(new Paragraph("═══════════════════════════════════════════════════════"));
                    doc.add(new Paragraph(""));
                    
                    // INFORMACIÓN COTIZACIÓN
                    doc.add(new Paragraph("Código: " + cotizacion.getCodigo()));
                    doc.add(new Paragraph("Número: " + cotizacion.getNumeroCotizacion()));
                    doc.add(new Paragraph("Fecha: " + cotizacion.getFechaCreacion().format(FECHA_FORMATO)));
                    doc.add(new Paragraph("Estado: " + cotizacion.getEstado()));
                    doc.add(new Paragraph("Válida hasta: " + cotizacion.getFechaValidez()));
                    doc.add(new Paragraph(""));
                    
                    // CLIENTE
                    doc.add(new Paragraph("CLIENTE:"));
                    doc.add(new Paragraph("Nombre: " + cotizacion.getCliente().getNombre()));
                    doc.add(new Paragraph("Documento: " + cotizacion.getCliente().getDocumento()));
                    doc.add(new Paragraph("Email: " + cotizacion.getCliente().getEmail()));
                    doc.add(new Paragraph("Teléfono: " + cotizacion.getCliente().getTelefono()));
                    doc.add(new Paragraph("Dirección: " + cotizacion.getCliente().getDireccion()));
                    doc.add(new Paragraph(""));
                    
                    // DESCRIPCIÓN
                    doc.add(new Paragraph("DESCRIPCIÓN:"));
                    doc.add(new Paragraph(cotizacion.getDescripcion()));
                    doc.add(new Paragraph(""));
                    
                    // ITEMS
                    doc.add(new Paragraph("ITEMS:"));
                    if (cotizacion.getItems() != null && !cotizacion.getItems().isEmpty()) {
                        int num = 1;
                        for (var item : cotizacion.getItems()) {
                            doc.add(new Paragraph(num + ". " + item.getDescripcion()));
                            doc.add(new Paragraph("   Cantidad: " + item.getCantidad() + " | Valor: $" + item.getPrecioUnitario() + " | Subtotal: $" + item.getSubtotal()));
                            num++;
                        }
                    }
                    doc.add(new Paragraph(""));
                    
                    // TOTALES
                    doc.add(new Paragraph("───────────────────────────────────────────────────────"));
                    doc.add(new Paragraph("Subtotal: $" + cotizacion.getSubtotal()));
                    doc.add(new Paragraph("Impuestos (19%): $" + cotizacion.getImpuestos()));
                    doc.add(new Paragraph("TOTAL: $" + cotizacion.getTotal()));
                    doc.add(new Paragraph("───────────────────────────────────────────────────────"));
                    doc.add(new Paragraph(""));
                    
                    if (cotizacion.getObservaciones() != null && !cotizacion.getObservaciones().isEmpty()) {
                        doc.add(new Paragraph("OBSERVACIONES:"));
                        doc.add(new Paragraph(cotizacion.getObservaciones()));
                    }
                    
                    doc.close();
                }
                
                System.out.println("✅ PDF generado: " + rutaCompleta);
                
            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    public void generarPdfFacturaAsync(FacturaEntity factura) {
        new Thread(() -> {
            try {
                System.out.println("⏳ Iniciando generación de PDF para factura: " + factura.getNumeroFactura());
                Thread.sleep(2000);
                
                FileUtils.inicializarDirectorios();
                
                String nombreArchivo = factura.getNumeroFactura() + ".pdf";
                String rutaCompleta = Paths.get(FileUtils.getRutaFacturas(), nombreArchivo).toString();
                
                try (PdfWriter writer = new PdfWriter(rutaCompleta)) {
                    PdfDocument pdfDoc = new PdfDocument(writer);
                    Document doc = new Document(pdfDoc);
                    
                    // LOGO
                    try {
                        String logoPath = "src/main/resources/imagenes/logo.png";
                        Image logo = new Image(com.itextpdf.io.image.ImageDataFactory.create(logoPath));
                        logo.setWidth(80);
                        logo.setHeight(80);
                        doc.add(logo);
                    } catch (Exception e) {
                        System.out.println("⚠️ Logo no encontrado, continuando sin él");
                    }
                    
                    // ENCABEZADO EMPRESA
                    doc.add(new Paragraph("QUEEN'S CORNER GALLERY"));
                    doc.add(new Paragraph("Carrera 10 # 16 - 95, Sogamoso"));
                    doc.add(new Paragraph("NIT: 56789056-6"));
                    doc.add(new Paragraph("\"Diseño y confort a su servicio\""));
                    doc.add(new Paragraph(""));
                    doc.add(new Paragraph("═══════════════════════════════════════════════════════"));
                    doc.add(new Paragraph("FACTURA DE VENTA"));
                    doc.add(new Paragraph("═══════════════════════════════════════════════════════"));
                    doc.add(new Paragraph(""));
                    
                    // INFORMACIÓN FACTURA
                    doc.add(new Paragraph("Número: " + factura.getNumeroFactura()));
                    doc.add(new Paragraph("Fecha Emisión: " + factura.getFechaCreacion().format(FECHA_FORMATO)));
                    if (factura.getFechaEnvio() != null) {
                        doc.add(new Paragraph("Fecha Envío: " + factura.getFechaEnvio().format(FECHA_FORMATO)));
                    }
                    doc.add(new Paragraph("Estado: " + factura.getEstado()));
                    doc.add(new Paragraph(""));
                    
                    // CLIENTE
                    if (factura.getNegocio() != null && factura.getNegocio().getCotizacion() != null && 
                        factura.getNegocio().getCotizacion().getCliente() != null) {
                        doc.add(new Paragraph("CLIENTE:"));
                        doc.add(new Paragraph("Nombre: " + factura.getNegocio().getCotizacion().getCliente().getNombre()));
                        doc.add(new Paragraph("Documento: " + factura.getNegocio().getCotizacion().getCliente().getDocumento()));
                        doc.add(new Paragraph("Email: " + factura.getNegocio().getCotizacion().getCliente().getEmail()));
                        doc.add(new Paragraph("Teléfono: " + factura.getNegocio().getCotizacion().getCliente().getTelefono()));
                        doc.add(new Paragraph("Dirección: " + factura.getNegocio().getCotizacion().getCliente().getDireccion()));
                        doc.add(new Paragraph(""));
                    }
                    
                    // NEGOCIO
                    if (factura.getNegocio() != null) {
                        doc.add(new Paragraph("NEGOCIO:"));
                        doc.add(new Paragraph("Código: " + factura.getNegocio().getCodigo()));
                        doc.add(new Paragraph("Descripción: " + factura.getNegocio().getDescripcion()));
                        doc.add(new Paragraph(""));
                    }
                    
                    // DETALLE DE LÍNEAS
                    doc.add(new Paragraph("DETALLE DE LÍNEAS:"));
                    if (factura.getLineas() != null && !factura.getLineas().isEmpty()) {
                        for (var linea : factura.getLineas()) {
                            doc.add(new Paragraph(linea.getNumeroLinea() + ". " + linea.getDescripcion()));
                            doc.add(new Paragraph("   Cantidad: " + linea.getCantidad() + " | Valor: $" + linea.getValorUnitario() + " | Subtotal: $" + linea.getTotal()));
                        }
                    }
                    doc.add(new Paragraph(""));
                    
                    // TOTALES
                    doc.add(new Paragraph("───────────────────────────────────────────────────────"));
                    doc.add(new Paragraph("Subtotal: $" + factura.getSubtotal()));
                    if (factura.getAnticipo() != null && factura.getAnticipo().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        doc.add(new Paragraph("Anticipo: $" + factura.getAnticipo()));
                    }
                    doc.add(new Paragraph("IVA (19%): $" + factura.getIva()));
                    doc.add(new Paragraph("TOTAL A PAGAR: $" + factura.getTotal()));
                    doc.add(new Paragraph("───────────────────────────────────────────────────────"));
                    doc.add(new Paragraph(""));
                    
                    if (factura.getObservaciones() != null && !factura.getObservaciones().isEmpty()) {
                        doc.add(new Paragraph("OBSERVACIONES:"));
                        doc.add(new Paragraph(factura.getObservaciones()));
                        doc.add(new Paragraph(""));
                    }
                    
                    doc.add(new Paragraph("Creado por: " + factura.getUsuarioCreacion()));
                    if (factura.getUsuarioEnvio() != null) {
                        doc.add(new Paragraph("Enviado por: " + factura.getUsuarioEnvio()));
                    }
                    
                    doc.close();
                }
                
                factura.setPathPdf(rutaCompleta);
                facturaRepository.save(factura);
                
                System.out.println("✅ PDF generado: " + rutaCompleta);
                
            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    public void generarPdfFacturaCompleto(FacturaEntity factura) {
        generarPdfFacturaAsync(factura);
    }
}
  