-- Agregar campos desnormalizados de cotización a tabla negocios
ALTER TABLE negocios 
ADD COLUMN IF NOT EXISTS codigo_cotizacion VARCHAR(50),
ADD COLUMN IF NOT EXISTS estado_cotizacion VARCHAR(20),
ADD COLUMN IF NOT EXISTS fecha_cotizacion DATETIME,
ADD COLUMN IF NOT EXISTS fecha_validez_cotizacion DATE,
ADD COLUMN IF NOT EXISTS descripcion_cotizacion TEXT,
ADD COLUMN IF NOT EXISTS subtotal_cotizacion DECIMAL(15, 2),
ADD COLUMN IF NOT EXISTS impuestos_cotizacion DECIMAL(15, 2),
ADD COLUMN IF NOT EXISTS total_cotizacion DECIMAL(15, 2),
ADD COLUMN IF NOT EXISTS observaciones_cotizacion TEXT,
ADD COLUMN IF NOT EXISTS fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP;

-- Crear índices para mejor performance en consultas
CREATE INDEX IF NOT EXISTS idx_negocios_estado_cotizacion ON negocios(estado_cotizacion);
CREATE INDEX IF NOT EXISTS idx_negocios_codigo_cotizacion ON negocios(codigo_cotizacion);
CREATE INDEX IF NOT EXISTS idx_negocios_fecha_actualizacion ON negocios(fecha_actualizacion);
