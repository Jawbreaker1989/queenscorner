-- Agregar campos desnormalizados de cotización a tabla negocios
ALTER TABLE negocios 
ADD COLUMN codigo_cotizacion VARCHAR(50),
ADD COLUMN estado_cotizacion VARCHAR(20),
ADD COLUMN fecha_cotizacion DATETIME,
ADD COLUMN fecha_validez_cotizacion DATE,
ADD COLUMN descripcion_cotizacion TEXT,
ADD COLUMN subtotal_cotizacion DECIMAL(15, 2),
ADD COLUMN impuestos_cotizacion DECIMAL(15, 2),
ADD COLUMN total_cotizacion DECIMAL(15, 2),
ADD COLUMN observaciones_cotizacion TEXT,
ADD COLUMN fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP;

-- Crear índices para mejor performance en consultas
CREATE INDEX IF NOT EXISTS idx_negocios_estado_cotizacion ON negocios(estado_cotizacion);
CREATE INDEX IF NOT EXISTS idx_negocios_codigo_cotizacion ON negocios(codigo_cotizacion);
CREATE INDEX IF NOT EXISTS idx_negocios_fecha_actualizacion ON negocios(fecha_actualizacion);
