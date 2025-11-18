-- Script para recrear la tabla negocios con los nuevos atributos

DROP TABLE IF EXISTS negocios;

CREATE TABLE negocios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cotizacion_id BIGINT NOT NULL UNIQUE,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_inicio DATE,
    fecha_fin_estimada DATE,
    estado ENUM('EN_REVISION', 'CANCELADO', 'FINALIZADO') NOT NULL DEFAULT 'EN_REVISION',
    presupuesto_asignado DECIMAL(19,2) NOT NULL DEFAULT 0,
    presupuesto_utilizado DECIMAL(19,2) NOT NULL DEFAULT 0,
    responsable VARCHAR(255),
    descripcion TEXT,
    observaciones TEXT,
    FOREIGN KEY (cotizacion_id) REFERENCES cotizaciones(id) ON DELETE RESTRICT,
    INDEX idx_estado (estado),
    INDEX idx_fecha_creacion (fecha_creacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
