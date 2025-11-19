-- ==================== CREAR TABLA FACTURAS ====================

CREATE TABLE IF NOT EXISTS facturas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    -- Referencias
    negocio_id BIGINT NOT NULL,
    cotizacion_id BIGINT NOT NULL,
    
    -- Identificación
    numero_factura VARCHAR(50) NOT NULL UNIQUE,
    fecha_emision DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_vencimiento DATE NOT NULL,
    
    -- Valores
    anticipo DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    subtotal_items DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    base_gravable DECIMAL(15,2) GENERATED ALWAYS AS (subtotal_items - anticipo) STORED,
    iva_19 DECIMAL(15,2) GENERATED ALWAYS AS ((subtotal_items - anticipo) * 0.19) STORED,
    total_a_pagar DECIMAL(15,2) GENERATED ALWAYS AS ((subtotal_items - anticipo) + ((subtotal_items - anticipo) * 0.19)) STORED,
    
    -- Medio de pago
    medio_pago ENUM('TRANSFERENCIA', 'EFECTIVO', 'CHEQUE', 'TARJETA', 'OTRO') NOT NULL,
    referencia_pago VARCHAR(255),
    
    -- Estado
    estado ENUM('BORRADOR', 'EMITIDA', 'ENVIADA', 'PAGADA', 'ANULADA') DEFAULT 'BORRADOR',
    
    -- Archivos
    ruta_pdf VARCHAR(500),
    pdf_generado BOOLEAN DEFAULT FALSE,
    
    -- Información adicional
    notas TEXT,
    condiciones_pago VARCHAR(255),
    
    -- Auditoría
    usuario_creacion VARCHAR(100),
    usuario_emision VARCHAR(100),
    fecha_emision_real DATETIME,
    usuario_pago VARCHAR(100),
    fecha_pago DATETIME,
    fecha_ultima_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Relaciones
    FOREIGN KEY (negocio_id) REFERENCES negocios(id) ON DELETE RESTRICT,
    FOREIGN KEY (cotizacion_id) REFERENCES cotizaciones(id) ON DELETE RESTRICT,
    
    -- Índices
    INDEX idx_negocio_id (negocio_id),
    INDEX idx_cotizacion_id (cotizacion_id),
    INDEX idx_numero_factura (numero_factura),
    INDEX idx_estado (estado),
    INDEX idx_fecha_emision (fecha_emision)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==================== CREAR TABLA LINEAS_FACTURA ====================

CREATE TABLE IF NOT EXISTS lineas_factura (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    factura_id BIGINT NOT NULL,
    item_cotizacion_id BIGINT,
    
    numero_linea INT NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    valor_unitario DECIMAL(15,2) NOT NULL,
    valor_linea DECIMAL(15,2) GENERATED ALWAYS AS (cantidad * valor_unitario) STORED,
    
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE,
    FOREIGN KEY (item_cotizacion_id) REFERENCES items_cotizacion(id) ON DELETE SET NULL,
    
    INDEX idx_factura_id (factura_id),
    INDEX idx_item_cotizacion_id (item_cotizacion_id)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==================== CREAR TABLA FACTURAS_AUDITORIA ====================

CREATE TABLE IF NOT EXISTS facturas_auditoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    factura_id BIGINT NOT NULL,
    
    accion VARCHAR(50) NOT NULL,
    estado_anterior VARCHAR(50),
    estado_nuevo VARCHAR(50),
    descripcion TEXT,
    
    usuario VARCHAR(100),
    fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE,
    INDEX idx_factura_id (factura_id),
    INDEX idx_fecha_cambio (fecha_cambio)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==================== CREAR VISTA PARA REPORTES ====================

CREATE OR REPLACE VIEW v_facturas_resumen AS
SELECT 
    f.id,
    f.numero_factura,
    f.estado,
    n.nombre_negocio,
    n.nombre_cliente,
    n.email_cliente,
    n.rut_cliente,
    n.anticipo,
    f.subtotal_items,
    f.base_gravable,
    f.iva_19,
    f.total_a_pagar,
    f.medio_pago,
    f.fecha_emision,
    f.fecha_vencimiento,
    f.pdf_generado,
    COUNT(lf.id) as cantidad_items
FROM facturas f
JOIN negocios n ON f.negocio_id = n.id
LEFT JOIN lineas_factura lf ON f.id = lf.factura_id
GROUP BY f.id;
