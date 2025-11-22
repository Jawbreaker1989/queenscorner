-- ============================================
-- USUARIOS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- CLIENTES TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    documento_identidad VARCHAR(50),
    email VARCHAR(100),
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    ciudad VARCHAR(100),
    pais VARCHAR(100),
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nombre (nombre),
    INDEX idx_documento (documento_identidad)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- COTIZACIONES TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS cotizaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    numero_cotizacion VARCHAR(50) NOT NULL UNIQUE,
    fecha_cotizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_expiracion DATE,
    estado ENUM('BORRADOR', 'ENVIADA', 'ACEPTADA', 'RECHAZADA', 'VENCIDA') NOT NULL DEFAULT 'BORRADOR',
    total_base DECIMAL(19,2) NOT NULL DEFAULT 0,
    total_iva DECIMAL(19,2) NOT NULL DEFAULT 0,
    total_general DECIMAL(19,2) NOT NULL DEFAULT 0,
    descuento_porcentaje DECIMAL(5,2) DEFAULT 0,
    notas TEXT,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE,
    INDEX idx_cliente (cliente_id),
    INDEX idx_numero (numero_cotizacion),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- ITEMS COTIZACION TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS items_cotizacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cotizacion_id BIGINT NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,
    valor_unitario DECIMAL(19,2) NOT NULL,
    subtotal DECIMAL(19,2) NOT NULL,
    INDEX idx_cotizacion (cotizacion_id),
    FOREIGN KEY (cotizacion_id) REFERENCES cotizaciones(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- NEGOCIOS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS negocios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cotizacion_id BIGINT NOT NULL UNIQUE,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_inicio DATE,
    fecha_fin_estimada DATE,
    estado ENUM('EN_REVISION', 'CANCELADO', 'FINALIZADO') NOT NULL DEFAULT 'EN_REVISION',
    presupuesto_asignado DECIMAL(19,2) NOT NULL DEFAULT 0,
    presupuesto_utilizado DECIMAL(19,2) NOT NULL DEFAULT 0,
    anticipo DECIMAL(19,2) NOT NULL DEFAULT 0,
    responsable VARCHAR(255),
    descripcion TEXT,
    observaciones TEXT,
    FOREIGN KEY (cotizacion_id) REFERENCES cotizaciones(id) ON DELETE RESTRICT,
    INDEX idx_estado (estado),
    INDEX idx_fecha_creacion (fecha_creacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- FACTURAS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS facturas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_factura VARCHAR(50) NOT NULL UNIQUE,
    negocio_id BIGINT NOT NULL,
    cotizacion_id BIGINT NOT NULL,
    fecha_emision DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_vencimiento DATE,
    estado ENUM('BORRADOR', 'EMITIDA', 'ENVIADA', 'PAGADA', 'ANULADA') NOT NULL DEFAULT 'BORRADOR',
    medio_pago ENUM('EFECTIVO', 'TRANSFERENCIA', 'CHEQUE', 'TARJETA', 'OTRO') NOT NULL DEFAULT 'EFECTIVO',
    base_gravable DECIMAL(19,2) NOT NULL DEFAULT 0,
    iva_19 DECIMAL(19,2) NOT NULL DEFAULT 0,
    total_a_pagar DECIMAL(19,2) NOT NULL DEFAULT 0,
    pdf_generado BOOLEAN NOT NULL DEFAULT FALSE,
    ruta_pdf VARCHAR(500),
    observaciones TEXT,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (negocio_id) REFERENCES negocios(id) ON DELETE RESTRICT,
    FOREIGN KEY (cotizacion_id) REFERENCES cotizaciones(id) ON DELETE RESTRICT,
    INDEX idx_numero (numero_factura),
    INDEX idx_estado (estado),
    INDEX idx_fecha_emision (fecha_emision),
    INDEX idx_negocio (negocio_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- LINEAS FACTURA TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS lineas_factura (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    factura_id BIGINT NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,
    valor_unitario DECIMAL(19,2) NOT NULL,
    subtotal DECIMAL(19,2) NOT NULL,
    FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE,
    INDEX idx_factura (factura_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- FACTURAS AUDITORIA TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS facturas_auditoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_factura VARCHAR(50),
    numero_cotizacion VARCHAR(50),
    codigo_negocio VARCHAR(50),
    cliente_nombre VARCHAR(255),
    estado VARCHAR(50),
    total_a_pagar DECIMAL(19,2),
    razon_cambio VARCHAR(255),
    fecha_auditoria DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_numero_factura (numero_factura)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
