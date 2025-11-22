package com.uptc.queenscorner.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

/**
 * Configuración del sistema de caché de la aplicación
 * Utiliza Caffeine para caché en memoria
 * 
 * Cachés configurados:
 * - clientes: Almacena listados y búsquedas de clientes
 * - cotizaciones: Almacena listados de cotizaciones
 * - negocios: Almacena listados de negocios
 * - facturas: Almacena listados de facturas
 * - facturas_negocio: Almacena facturas por negocio
 * 
 * Política:
 * - Tamaño máximo: 500 elementos
 * - Expiración: 10 minutos desde escritura
 * - Capacidad inicial: 100
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Crea el gestor de caché con Caffeine
     * Configura cachés reutilizables en toda la aplicación
     * @return CacheManager con Caffeine
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
            "clientes", "cotizaciones", "negocios", "facturas", "facturas_negocio"
        );
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    /**
     * Configura las políticas de caché (expiración, tamaño máximo, etc)
     * @return Configuración de Caffeine para todas las cachés
     */
    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats();
    }
} 