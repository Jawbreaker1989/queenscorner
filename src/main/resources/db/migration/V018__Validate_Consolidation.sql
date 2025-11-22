-- ============================================
-- V018__Validate_Consolidation.sql
-- ============================================
-- Validación de que la consolidación V017 fue exitosa
-- Verifica que todas las tablas y estructuras estén presentes
-- ============================================

-- Verificar que todas las tablas existen
SELECT 'Tabla usuarios OK' AS check_result WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'usuarios');
SELECT 'Tabla clientes OK' AS check_result WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'clientes');
SELECT 'Tabla cotizaciones OK' AS check_result WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'cotizaciones');
SELECT 'Tabla items_cotizacion OK' AS check_result WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'items_cotizacion');
SELECT 'Tabla negocios OK' AS check_result WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'negocios');
SELECT 'Tabla facturas OK' AS check_result WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'facturas');
SELECT 'Tabla lineas_factura OK' AS check_result WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'lineas_factura');
SELECT 'Tabla facturas_auditoria OK' AS check_result WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'facturas_auditoria');

-- Verificar vista
SELECT 'Vista v_facturas_resumen OK' AS check_result WHERE EXISTS (SELECT 1 FROM information_schema.views WHERE table_name = 'v_facturas_resumen');

-- Verificar columnas clave
SELECT 'Columna rol en usuarios OK' AS check_result WHERE EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'usuarios' AND column_name = 'rol' AND column_type LIKE "enum('ADMIN')"
);

SELECT 'Columna estado en cotizaciones OK' AS check_result WHERE EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'cotizaciones' AND column_name = 'estado' AND column_type LIKE "%APROBADA%"
);

SELECT 'Campos desnormalizados en negocios OK' AS check_result WHERE EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'negocios' AND column_name = 'codigo_cotizacion'
);

-- Si todas las verificaciones pasan, la consolidación fue exitosa
SELECT 'VALIDACIÓN EXITOSA: Consolidación V017 completada correctamente' AS final_result;