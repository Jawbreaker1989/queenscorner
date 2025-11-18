-- =============================================
-- MIGRACION: Actualizar tabla NEGOCIOS (versión 2)
-- Verificar y agregar columnas faltantes
-- =============================================

USE queens_corner_prod_v2;

-- PASO 1: Verificar estructura actual
SELECT 'Estructura actual de negocios:' as paso;
DESCRIBE negocios;

-- PASO 2: Agregar columnas faltantes si no existen
ALTER TABLE negocios
ADD COLUMN IF NOT EXISTS fecha_inicio DATE NULL AFTER fecha_creacion,
ADD COLUMN IF NOT EXISTS fecha_fin_estimada DATE NULL AFTER fecha_inicio,
ADD COLUMN IF NOT EXISTS presupuesto_asignado DECIMAL(15, 2) DEFAULT 0.00 AFTER estado,
ADD COLUMN IF NOT EXISTS presupuesto_utilizado DECIMAL(15, 2) DEFAULT 0.00 AFTER presupuesto_asignado,
ADD COLUMN IF NOT EXISTS responsable VARCHAR(255) NULL AFTER presupuesto_utilizado;

-- PASO 3: Migrar datos de columnas antiguas a nuevas (si existen)
-- Si existen las columnas antiguas, copiar sus valores
UPDATE negocios 
SET 
    presupuesto_asignado = COALESCE(total_negocio, presupuesto_asignado, 0),
    presupuesto_utilizado = COALESCE(anticipo, presupuesto_utilizado, 0),
    fecha_fin_estimada = COALESCE(fecha_entrega_estimada, fecha_fin_estimada),
    fecha_inicio = COALESCE(fecha_inicio, DATE(fecha_creacion))
WHERE presupuesto_asignado = 0 OR presupuesto_asignado IS NULL;

-- PASO 4: Cambiar el ENUM de estados si es necesario
-- Primero cambiar a un tipo ENUM que incluya todos los estados posibles
ALTER TABLE negocios 
MODIFY estado ENUM('EN_REVISION', 'CANCELADO', 'FINALIZADO', 'BORRADOR', 'ENVIADA', 'APROBADA', 'RECHAZADA') DEFAULT 'EN_REVISION';

-- PASO 5: Convertir estados antiguos a nuevos (FINALIZADO y CANCELADO se mantienen)
UPDATE negocios 
SET estado = 'EN_REVISION' 
WHERE estado NOT IN ('CANCELADO', 'FINALIZADO');

-- PASO 6: Cambiar de nuevo el ENUM al valor final (sin estados extra)
ALTER TABLE negocios 
MODIFY estado ENUM('EN_REVISION', 'CANCELADO', 'FINALIZADO') DEFAULT 'EN_REVISION';

-- PASO 7: Remover columnas antiguas si existen
-- Usar estructura condicional para evitar errores
SET @sql = CONCAT(
    'ALTER TABLE negocios ',
    CASE WHEN (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='negocios' AND COLUMN_NAME='total_negocio' AND TABLE_SCHEMA=DATABASE()) > 0 
        THEN 'DROP COLUMN total_negocio,' ELSE '' END,
    CASE WHEN (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='negocios' AND COLUMN_NAME='anticipo' AND TABLE_SCHEMA=DATABASE()) > 0 
        THEN 'DROP COLUMN anticipo,' ELSE '' END,
    CASE WHEN (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='negocios' AND COLUMN_NAME='saldo_pendiente' AND TABLE_SCHEMA=DATABASE()) > 0 
        THEN 'DROP COLUMN saldo_pendiente,' ELSE '' END,
    CASE WHEN (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='negocios' AND COLUMN_NAME='fecha_entrega_estimada' AND TABLE_SCHEMA=DATABASE()) > 0 
        THEN 'DROP COLUMN fecha_entrega_estimada' ELSE '' END
);

-- Remover la última coma si existe
SET @sql = TRIM(TRAILING ',' FROM @sql);

-- Ejecutar solo si hay algo para borrar
IF @sql != 'ALTER TABLE negocios' THEN
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END IF;

-- PASO 8: Mostrar estructura final
SELECT 'Estructura final de negocios:' as paso;
DESCRIBE negocios;

-- PASO 9: Verificar datos
SELECT 'Datos en tabla negocios:' as paso;
SELECT id, codigo, estado, presupuesto_asignado, presupuesto_utilizado, responsable, fecha_inicio, fecha_fin_estimada FROM negocios LIMIT 5;

SELECT 'Migracion de tabla NEGOCIOS completada exitosamente' as mensaje;
