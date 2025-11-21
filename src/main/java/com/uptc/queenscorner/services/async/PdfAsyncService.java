package com.uptc.queenscorner.services.async;

import com.uptc.queenscorner.models.entities.CotizacionEntity;
import com.uptc.queenscorner.models.entities.FacturaEntity;
import com.uptc.queenscorner.repositories.IFacturaRepository;
import com.uptc.queenscorner.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@Service
public class PdfAsyncService {
    
    @Autowired
    private IFacturaRepository facturaRepository;
    
    private static final DateTimeFormatter FECHA_FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Async
    public void generarPdfCotizacion(CotizacionEntity cotizacion) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.err.println("Error en generarPdfCotizacion: " + e.getMessage());
        }
    }
    
    @Async
    public void generarPdfFacturaAsync(FacturaEntity factura) {
        try {
            Thread.sleep(1000);
            generarPdfFacturaCompleto(factura);
        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
        }
    }
    
    private void generarPdfFacturaCompleto(FacturaEntity factura) {
        try {
            FileUtils.inicializarDirectorios();
            
            String nombreArchivo = factura.getNumeroFactura() + ".txt";
            String rutaCompleta = Paths.get(FileUtils.getRutaFacturas(), nombreArchivo).toString();
            
            StringBuilder contenido = new StringBuilder();
            contenido.append("=".repeat(80)).append("\n");
            contenido.append("FACTURA DE VENTA\n");
            contenido.append("=".repeat(80)).append("\n\n");
            
            contenido.append("NUMERO: ").append(factura.getNumeroFactura()).append("\n");
            contenido.append("FECHA: ").append(factura.getFechaCreacion().format(FECHA_FORMATO)).append("\n");
            contenido.append("ESTADO: ").append(factura.getEstado()).append("\n\n");
            
            contenido.append("-".repeat(80)).append("\n");
            contenido.append("CLIENTE Y NEGOCIO\n");
            contenido.append("-".repeat(80)).append("\n");
            contenido.append("Negocio: ").append(factura.getNegocio().getCodigo()).append("\n");
            contenido.append("Descripcion: ").append(factura.getNegocio().getDescripcion()).append("\n\n");
            
            contenido.append("-".repeat(80)).append("\n");
            contenido.append("DETALLE DE LINEAS\n");
            contenido.append("-".repeat(80)).append("\n");
            contenido.append(String.format("%-5s | %-40s | %10s | %12s | %12s\n", 
                "LINEA", "DESCRIPCION", "CANTIDAD", "VALOR UNI", "TOTAL"));
            contenido.append("-".repeat(80)).append("\n");
            
            factura.getLineas().forEach(linea -> 
                contenido.append(String.format("%-5d | %-40s | %10.2f | %12.2f | %12.2f\n",
                    linea.getNumeroLinea(),
                    linea.getDescripcion().substring(0, Math.min(40, linea.getDescripcion().length())),
                    linea.getCantidad(),
                    linea.getValorUnitario(),
                    linea.getTotal()))
            );
            
            contenido.append("-".repeat(80)).append("\n");
            contenido.append(String.format("%-63s SUBTOTAL:  %12.2f\n", "", factura.getSubtotal()));
            contenido.append(String.format("%-63s IVA (19%): %12.2f\n", "", factura.getIva()));
            contenido.append("=".repeat(80)).append("\n");
            contenido.append(String.format("%-63s TOTAL:     %12.2f\n", "", factura.getTotal()));
            contenido.append("=".repeat(80)).append("\n\n");
            
            if (factura.getObservaciones() != null && !factura.getObservaciones().isEmpty()) {
                contenido.append("OBSERVACIONES:\n");
                contenido.append(factura.getObservaciones()).append("\n\n");
            }
            
            contenido.append("Usuario creacion: ").append(factura.getUsuarioCreacion()).append("\n");
            if (factura.getUsuarioEnvio() != null) {
                contenido.append("Usuario envio: ").append(factura.getUsuarioEnvio()).append("\n");
            }
            
            try (FileWriter writer = new FileWriter(rutaCompleta)) {
                writer.write(contenido.toString());
            }
            
            factura.setPathPdf(rutaCompleta);
            facturaRepository.save(factura);
            
        } catch (Exception e) {
            System.err.println("Error generando PDF de factura: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
