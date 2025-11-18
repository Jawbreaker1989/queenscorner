-- =============================================
-- TEST DATA: Crear negocio desde cotización APROBADA
-- =============================================

USE queens_corner_prod_v2;

-- Verificar que hay cotizaciones aprobadas
SELECT 'Cotizaciones APROBADAS:' as paso;
SELECT id, codigo, estado FROM cotizaciones WHERE estado='APROBADA' LIMIT 5;

-- Intentar crear un negocio desde la cotización ID=1 (si existe y está APROBADA)
-- Primero verificar si ya existe
SELECT 'Negocio existente para cotización 1:' as paso;
SELECT id, codigo, estado FROM negocios WHERE cotizacion_id=1;

-- Si no existe, insertar uno manualmente para testing
-- (En producción esto debería hacerse por la API)
INSERT INTO negocios (cotizacion_id, codigo, fecha_creacion, fecha_inicio, fecha_fin_estimada, estado, presupuesto_asignado, presupuesto_utilizado, responsable, descripcion, observaciones)
SELECT 
    c.id,
    CONCAT('NEG-', UNIX_TIMESTAMP()*1000),
    NOW(),
    CURDATE(),
    DATE_ADD(CURDATE(), INTERVAL 30 DAY),
    'EN_REVISION',
    c.total,
    0,
    'Default Manager',
    CONCAT('Negocio para: ', c.descripcion),
    'Negocio generado desde cotización aprobada'
FROM cotizaciones c
WHERE c.id = 1 
  AND c.estado = 'APROBADA'
  AND NOT EXISTS (SELECT 1 FROM negocios n WHERE n.cotizacion_id = c.id);

-- Verificar que se creó
SELECT 'Negocio creado:' as paso;
SELECT id, codigo, estado, presupuesto_asignado FROM negocios ORDER BY id DESC LIMIT 3;

SELECT 'Script completado exitosamente' as resultado;
