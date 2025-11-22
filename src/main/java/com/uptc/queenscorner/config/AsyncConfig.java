package com.uptc.queenscorner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import java.util.concurrent.Executor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuración del procesamiento asincrónico
 * Define un pool de threads para tareas de larga duración
 * 
 * Utilizado para:
 * - Generación de PDFs en background
 * - Envío de notificaciones
 * - Procesamiento de reportes
 * 
 * Pool de threads:
 * - Threads principales: 5
 * - Máximo de threads: 10
 * - Cola de espera: 100 tareas
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Crea el ejecutor de tareas asincrónicas
     * Define cuántos threads pueden procesar tareas simultáneamente
     * @return Executor con ThreadPool configurado
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("queens-async-");
        executor.initialize();
        return executor;
    }
} 