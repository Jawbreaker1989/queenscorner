package com.uptc.queenscorner.services.async;

import com.uptc.queenscorner.models.entities.FacturaEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
public class PdfAsyncService {

    @Async
    public CompletableFuture<String> generarFacturaPdfAsync(FacturaEntity factura) {
        try {
            Thread.sleep(3000);
            String pdfPath = "/facturas/" + factura.getCodigo() + ".pdf";
            return CompletableFuture.completedFuture(pdfPath);
        } catch (InterruptedException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<String> generarCotizacionPdfAsync(String codigoCotizacion) {
        try {
            Thread.sleep(2000);
            String pdfPath = "/cotizaciones/" + codigoCotizacion + ".pdf";
            return CompletableFuture.completedFuture(pdfPath);
        } catch (InterruptedException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}