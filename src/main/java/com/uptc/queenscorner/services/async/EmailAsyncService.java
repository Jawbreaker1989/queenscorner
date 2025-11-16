package com.uptc.queenscorner.services.async;

import com.uptc.queenscorner.models.entities.CotizacionEntity;
import com.uptc.queenscorner.models.entities.OrdenTrabajoEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailAsyncService {

    @Async
    public CompletableFuture<Boolean> enviarCotizacionPorEmail(CotizacionEntity cotizacion, String emailCliente) {
        try {
            Thread.sleep(2500);
            System.out.println("📧 Email enviado: Cotización " + cotizacion.getCodigo() + " a " + emailCliente);
            return CompletableFuture.completedFuture(true);
        } catch (InterruptedException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<Boolean> notificarOrdenLista(OrdenTrabajoEntity orden, String emailCliente) {
        try {
            Thread.sleep(2000);
            System.out.println("📧 Notificación enviada: Orden " + orden.getCodigo() + " lista a " + emailCliente);
            return CompletableFuture.completedFuture(true);
        } catch (InterruptedException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}