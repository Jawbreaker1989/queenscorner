-- ==================== CREAR TABLA FACTURAS ====================
-- Nota: Esta tabla será recreada en V011 con la estructura final

CREATE TABLE IF NOT EXISTS facturas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    negocio_id BIGINT NOT NULL,
    cotizacion_id BIGINT,
    numero_factura VARCHAR(50) NOT NULL UNIQUE,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_emision DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_envio DATETIME,
    codigo VARCHAR(255) NOT NULL,
    
    anticipo DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    subtotal DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    subtotal_items DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    iva DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    iva_19 DECIMAL(15,2),
    total DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    total_a_pagar DECIMAL(15,2),
    
    estado VARCHAR(50) NOT NULL DEFAULT 'ENVIADA',
    observaciones TEXT,
    
    usuario_creacion VARCHAR(100),
    usuario_envio VARCHAR(100),
    path_pdf VARCHAR(500),
    
    FOREIGN KEY (negocio_id) REFERENCES negocios(id) ON DELETE RESTRICT,
    FOREIGN KEY (cotizacion_id) REFERENCES cotizaciones(id) ON DELETE SET NULL,
    
    INDEX idx_negocio_id (negocio_id),
    INDEX idx_numero_factura (numero_factura),
    INDEX idx_estado (estado),
    INDEX idx_fecha_creacion (fecha_creacion)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==================== CREAR TABLA LINEAS_FACTURA ====================

CREATE TABLE IF NOT EXISTS lineas_factura (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    factura_id BIGINT NOT NULL,
    
    numero_linea INT NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    valor_unitario DECIMAL(15,2) NOT NULL,
    
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE,
    
    INDEX idx_factura_id (factura_id)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

