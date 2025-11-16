package com.uptc.queenscorner.services.async;

import com.uptc.queenscorner.models.entities.OrdenTrabajoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificacionAsyncService {

    @Autowired
    private EmailAsyncService emailAsyncService;

    @Async
    public CompletableFuture<Boolean> notificarOrdenLista(OrdenTrabajoEntity orden) {
        try {
            String emailCliente = orden.getNegocio().getCotizacion().getCliente().getEmail();
            
            if (emailCliente != null) {
                emailAsyncService.notificarOrdenLista(orden, emailCliente);
            }

            String telefono = orden.getNegocio().getCotizacion().getCliente().getTelefono();
            if (telefono != null) {
                System.out.println("📱 SMS enviado: Orden " + orden.getCodigo() + " lista al " + telefono);
            }

            Thread.sleep(1500);
            return CompletableFuture.completedFuture(true);
        } catch (InterruptedException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}