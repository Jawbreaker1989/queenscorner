package com.uptc.queenscorner;

import com.uptc.queenscorner.utils.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Clase principal de la aplicación Spring Boot
 * Inicia el servidor de la aplicación QueensCorner
 * 
 * Anotaciones:
 * - @SpringBootApplication: Habilita autoconfiguración de Spring
 * - @EnableCaching: Activa el sistema de caché (Caffeine)
 * - @EnableAsync: Activa procesamiento asincrónico de tareas
 * 
 * La aplicación gestiona: Clientes, Cotizaciones, Negocios y Facturas
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class QueenscornerApplication {
    
    /**
     * Método principal que inicia la aplicación
     * Crea directorios necesarios antes de iniciar Spring
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        FileUtils.inicializarDirectorios();
        
        SpringApplication.run(QueenscornerApplication.class, args);
    }
}