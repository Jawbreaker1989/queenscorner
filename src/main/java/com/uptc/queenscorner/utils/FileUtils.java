package com.uptc.queenscorner.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    // Directorio base para almacenamiento de PDFs organizados por tipo de documento
    private static final String BASE_DIR = "D:" + File.separator + "queenscorner" + File.separator + "queenscornerarchives";

    /**
     * Crea un directorio si no existe - Esencial para el flujo async de PDFs
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
     * Obtiene la ruta completa para PDFs de notificaciones
     */
    public static String getRutaNotificaciones() {
        return BASE_DIR + File.separator + "notificaciones";
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
            case "NOTIFICACION":
                rutaBase = getRutaNotificaciones();
                break;
            default:
                rutaBase = BASE_DIR;
        }
        
        return rutaBase + File.separator + nombreArchivo;
    }

    /**
     * Inicializa todos los directorios necesarios para el flujo completo
     */
    public static void inicializarDirectorios() {
        crearDirectorioSiNoExiste(BASE_DIR);
        crearDirectorioSiNoExiste(getRutaCotizaciones());
        crearDirectorioSiNoExiste(getRutaFacturas());
        crearDirectorioSiNoExiste(getRutaNotificaciones());
        
        System.out.println("📁 Directorios inicializados:");
        System.out.println("   📄 Cotizaciones: " + getRutaCotizaciones());
        System.out.println("   🧾 Facturas: " + getRutaFacturas());
        System.out.println("   📧 Notificaciones: " + getRutaNotificaciones());
    }
}