-- ============================================
-- V017__Consolidate_All_Migrations.sql
-- ============================================
-- Esta migración consolida todas las migraciones anteriores (V001-V016)
-- Representa el esquema final después de todas las modificaciones
-- Ejecutar solo en bases de datos nuevas o después de respaldo completo
-- ============================================

-- ============================================
-- USUARIOS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN') NOT NULL DEFAULT 'ADMIN',
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
    estado ENUM('BORRADOR', 'ENVIADA', 'APROBADA', 'RECHAZADA', 'VENCIDA') NOT NULL DEFAULT 'BORRADOR',
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
    anticipo DECIMAL(19,2) NOT NULL DEFAULT 0,
    responsable VARCHAR(255),
    descripcion TEXT,
    observaciones TEXT,
    -- Campos desnormalizados agregados en V002
    codigo_cotizacion VARCHAR(50),
    estado_cotizacion VARCHAR(20),
    fecha_cotizacion DATETIME,
    fecha_validez_cotizacion DATE,
    descripcion_cotizacion TEXT,
    subtotal_cotizacion DECIMAL(15, 2),
    impuestos_cotizacion DECIMAL(15, 2),
    total_cotizacion DECIMAL(15, 2),
    observaciones_cotizacion TEXT,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cotizacion_id) REFERENCES cotizaciones(id) ON DELETE RESTRICT,
    INDEX idx_estado (estado),
    INDEX idx_fecha_creacion (fecha_creacion),
    INDEX idx_negocios_estado_cotizacion (estado_cotizacion),
    INDEX idx_negocios_codigo_cotizacion (codigo_cotizacion),
    INDEX idx_negocios_fecha_actualizacion (fecha_actualizacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- FACTURAS TABLE (Estructura final de V011)
-- ============================================
CREATE TABLE IF NOT EXISTS facturas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_factura VARCHAR(50) UNIQUE NOT NULL,
    codigo VARCHAR(255) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_emision TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_vencimiento DATE NOT NULL,
    fecha_envio TIMESTAMP,
    negocio_id BIGINT NOT NULL,
    cotizacion_id BIGINT,
    subtotal_items DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    anticipo DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    subtotal DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    iva DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    iva_19 DECIMAL(15, 2),
    total_a_pagar DECIMAL(15, 2),
    total DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    estado VARCHAR(50) NOT NULL DEFAULT 'EN_REVISION',
    medio_pago VARCHAR(50) NOT NULL DEFAULT 'EFECTIVO',
    observaciones TEXT,
    referencia_pago VARCHAR(255),
    notas TEXT,
    condiciones_pago VARCHAR(255),
    usuario_creacion VARCHAR(100),
    usuario_envio VARCHAR(100),
    usuario_pago VARCHAR(100),
    fecha_pago DATETIME,
    path_pdf VARCHAR(500),
    CONSTRAINT fk_facturas_negocio FOREIGN KEY (negocio_id) REFERENCES negocios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_facturas_cotizacion FOREIGN KEY (cotizacion_id) REFERENCES cotizaciones(id) ON DELETE SET NULL,
    INDEX idx_facturas_negocio_id (negocio_id),
    INDEX idx_facturas_cotizacion_id (cotizacion_id),
    INDEX idx_facturas_numero (numero_factura),
    INDEX idx_facturas_estado (estado),
    INDEX idx_facturas_fecha_creacion (fecha_creacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- LINEAS FACTURA TABLE (Estructura final de V011)
-- ============================================
CREATE TABLE IF NOT EXISTS lineas_factura (
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

-- ============================================
-- FACTURAS AUDITORIA TABLE (Estructura final de V011)
-- ============================================
CREATE TABLE IF NOT EXISTS facturas_auditoria (
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

-- ============================================
-- VISTA DE RESUMEN (De V011)
-- ============================================
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
GROUP BY f.id;</content>
<parameter name="filePath">d:\queenscorner\src\main\resources\db\migration\V017__Consolidate_All_Migrations.sql