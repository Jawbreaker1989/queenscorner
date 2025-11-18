-- =============================================
-- MIGRACION: Actualizar tabla NEGOCIOS
-- Cambiar de (FINALIZADO, CANCELADO) a (EN_REVISION, CANCELADO, FINALIZADO)
-- Agregar nuevos campos: fechaInicio, fechaFinEstimada, presupuestoAsignado, presupuestoUtilizado, responsable
-- Remover campos antiguos: totalNegocio, anticipo, saldoPendiente, fechaEntregaEstimada
-- =============================================

USE queens_corner_prod_v2;

-- PASO 1: Agregar nuevas columnas
ALTER TABLE negocios
ADD COLUMN fecha_inicio DATE NULL AFTER fecha_creacion,
ADD COLUMN fecha_fin_estimada DATE NULL AFTER fecha_inicio,
ADD COLUMN presupuesto_asignado DECIMAL(15, 2) DEFAULT 0.00 AFTER estado,
ADD COLUMN presupuesto_utilizado DECIMAL(15, 2) DEFAULT 0.00 AFTER presupuesto_asignado,
ADD COLUMN responsable VARCHAR(255) NULL AFTER presupuesto_utilizado,
ADD COLUMN descripcion TEXT NULL AFTER responsable;

-- PASO 2: Migrar datos de columnas antiguas a nuevas
UPDATE negocios 
SET 
    presupuesto_asignado = COALESCE(total_negocio, 0),
    presupuesto_utilizado = COALESCE(anticipo, 0),
    descripcion = observaciones,
    fecha_fin_estimada = fecha_entrega_estimada,
    fecha_inicio = DATE(fecha_creacion);

-- PASO 3: Cambiar el ENUM de estados
ALTER TABLE negocios 
MODIFY estado ENUM('EN_REVISION', 'CANCELADO', 'FINALIZADO') DEFAULT 'EN_REVISION';

-- PASO 4: Actualizar valores existentes del estado a EN_REVISION como default
UPDATE negocios 
SET estado = 'EN_REVISION' 
WHERE estado NOT IN ('CANCELADO', 'FINALIZADO');

-- PASO 5: Remover columnas antiguas
ALTER TABLE negocios
DROP COLUMN total_negocio,
DROP COLUMN anticipo,
DROP COLUMN saldo_pendiente,
DROP COLUMN fecha_entrega_estimada;

-- PASO 6: Actualizar la vista de negocios
DROP VIEW IF EXISTS vista_negocios_dashboard;

CREATE VIEW vista_negocios_dashboard AS
SELECT 
    n.id,
    n.codigo,
    n.estado,
    n.presupuesto_asignado,
    n.presupuesto_utilizado,
    n.fecha_fin_estimada,
    n.responsable,
    c.codigo as cotizacion_codigo,
    cl.nombre as cliente_nombre
FROM negocios n
JOIN cotizaciones c ON n.cotizacion_id = c.id
JOIN clientes cl ON c.cliente_id = cl.id;

-- PASO 7: Actualizar la vista de flujo completo
DROP VIEW IF EXISTS vista_flujo_completo;

CREATE VIEW vista_flujo_completo AS
SELECT 
    cl.nombre as cliente_nombre,
    c.codigo as cotizacion_codigo,
    c.estado as cotizacion_estado,
    c.total as cotizacion_total,
    n.codigo as negocio_codigo,
    n.estado as negocio_estado,
    n.presupuesto_asignado,
    n.responsable,
    ot.codigo as orden_codigo,
    ot.estado as orden_estado,
    f.codigo as factura_codigo,
    f.estado as factura_estado,
    f.total as factura_total
FROM negocios n
JOIN cotizaciones c ON n.cotizacion_id = c.id
JOIN clientes cl ON c.cliente_id = cl.id
LEFT JOIN ordenes_trabajo ot ON ot.negocio_id = n.id
LEFT JOIN facturas f ON f.negocio_id = n.id;

-- PASO 8: Verificar cambios
DESCRIBE negocios;
SELECT 'Migracion de tabla NEGOCIOS completada exitosamente' as mensaje;
