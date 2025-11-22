package com.uptc.queenscorner.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utilidad para gestionar rutas y creación de directorios para almacenamiento de PDFs
 * Actualmente soporta:
 * - Cotizaciones (generación async)
 * - Facturas (generación async)
 */
public class FileUtils {

    // Directorio base para almacenamiento de PDFs
    private static final String BASE_DIR = "D:" + File.separator + "queenscorner" + File.separator + "queenscornerarchives";

    /**
     * Crea un directorio si no existe
     */
    public static void crearDirectorioSiNoExiste(String rutaDirectorio) {
        try {
            Path path = Paths.get(rutaDirectorio);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al crear directorio: " + rutaDirectorio, e);
        }
    }

    /**
     * Obtiene la ruta completa para PDFs de cotizaciones
     */
    public static String getRutaCotizaciones() {
        return BASE_DIR + File.separator + "cotizaciones";
    }

    /**
     * Obtiene la ruta completa para PDFs de facturas
     */
    public static String getRutaFacturas() {
        return BASE_DIR + File.separator + "facturas";
    }

    /**
     * Genera nombre de archivo estándar para cotización
     */
    public static String generarNombreCotizacion(String codigo) {
        return "COT-" + codigo + ".pdf";
    }

    /**
     * Genera nombre de archivo estándar para factura
     */
    public static String generarNombreFactura(String codigo) {
        return "FAC-" + codigo + ".pdf";
    }

    /**
     * Verifica si un archivo PDF existe
     */
    public static boolean archivoExiste(String rutaCompleta) {
        return Files.exists(Paths.get(rutaCompleta));
    }

    /**
     * Obtiene la ruta completa para un PDF específico
     */
    public static String obtenerRutaCompleta(String tipoDocumento, String nombreArchivo) {
        String rutaBase;
        
        switch (tipoDocumento.toUpperCase()) {
            case "COTIZACION":
                rutaBase = getRutaCotizaciones();
                break;
            case "FACTURA":
                rutaBase = getRutaFacturas();
                break;
            default:
                throw new IllegalArgumentException("Tipo de documento no soportado: " + tipoDocumento);
        }
        
        return rutaBase + File.separator + nombreArchivo;
    }

    /**
     * Inicializa todos los directorios necesarios
     */
    public static void inicializarDirectorios() {
        crearDirectorioSiNoExiste(BASE_DIR);
        crearDirectorioSiNoExiste(getRutaCotizaciones());
        crearDirectorioSiNoExiste(getRutaFacturas());
        
        System.out.println("📁 Directorios inicializados:");
        System.out.println("   📄 Cotizaciones: " + getRutaCotizaciones());
        System.out.println("   🧾 Facturas: " + getRutaFacturas());
    }
}  