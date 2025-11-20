-- Drop existing audit table
DROP TABLE IF EXISTS facturas_auditoria CASCADE;

-- Drop existing foreign key constraints and tables
DROP TABLE IF EXISTS lineas_factura CASCADE;
DROP TABLE IF EXISTS facturas CASCADE;

-- Create facturas table with complete structure matching FacturaEntity
CREATE TABLE facturas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    -- Identificación
    numero_factura VARCHAR(50) UNIQUE NOT NULL,
    codigo VARCHAR(255) NOT NULL,
    
    -- Fechas
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_emision TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_vencimiento DATE NOT NULL,
    fecha_envio TIMESTAMP,
    
    -- Referencias
    negocio_id BIGINT NOT NULL,
    cotizacion_id BIGINT,
    
    -- Valores
    subtotal_items DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    anticipo DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    subtotal DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    iva DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    iva_19 DECIMAL(15, 2),
    total_a_pagar DECIMAL(15, 2),
    total DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    
    -- Estado y Medio de Pago
    estado VARCHAR(50) NOT NULL DEFAULT 'EN_REVISION' CHECK (estado IN ('EN_REVISION', 'ENVIADA', 'PAGADA', 'ANULADA')),
    medio_pago VARCHAR(50) NOT NULL DEFAULT 'EFECTIVO',
    
    -- Información adicional
    observaciones TEXT,
    referencia_pago VARCHAR(255),
    notas TEXT,
    condiciones_pago VARCHAR(255),
    
    -- Auditoría
    usuario_creacion VARCHAR(100),
    usuario_envio VARCHAR(100),
    usuario_pago VARCHAR(100),
    fecha_pago DATETIME,
    
    -- PDF
    path_pdf VARCHAR(500),
    
    -- Índices
    CONSTRAINT fk_facturas_negocio FOREIGN KEY (negocio_id) REFERENCES negocios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_facturas_cotizacion FOREIGN KEY (cotizacion_id) REFERENCES cotizaciones(id) ON DELETE SET NULL,
    
    INDEX idx_facturas_negocio_id (negocio_id),
    INDEX idx_facturas_cotizacion_id (cotizacion_id),
    INDEX idx_facturas_numero (numero_factura),
    INDEX idx_facturas_estado (estado),
    INDEX idx_facturas_fecha_creacion (fecha_creacion)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create lineas_factura table with correct structure
CREATE TABLE lineas_factura (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    factura_id BIGINT NOT NULL,
    numero_linea INTEGER NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    cantidad INTEGER NOT NULL DEFAULT 1,
    valor_unitario DECIMAL(15, 2) NOT NULL,
    
    INDEX idx_lineas_factura_factura_id (factura_id),
    CONSTRAINT fk_lineas_factura_factura FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE,
    CONSTRAINT uc_factura_numero_linea UNIQUE (factura_id, numero_linea)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create simplified audit table
CREATE TABLE facturas_auditoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    factura_id BIGINT NOT NULL,
    estado_anterior VARCHAR(50),
    estado_nuevo VARCHAR(50) NOT NULL,
    usuario VARCHAR(100),
    fecha_cambio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_auditoria_factura FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE,
    INDEX idx_auditoria_factura_id (factura_id),
    INDEX idx_auditoria_fecha (fecha_cambio)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create summary view
CREATE OR REPLACE VIEW v_facturas_resumen AS
SELECT 
    f.id,
    f.numero_factura,
    f.fecha_creacion,
    f.fecha_envio,
    n.id as negocio_id,
    n.codigo as codigo_negocio,
    f.subtotal,
    f.iva,
    f.total,
    f.estado,
    COUNT(lf.id) as cantidad_lineas,
    f.usuario_creacion,
    f.usuario_envio
FROM facturas f
LEFT JOIN negocios n ON f.negocio_id = n.id
LEFT JOIN lineas_factura lf ON f.id = lf.factura_id
GROUP BY f.id;

