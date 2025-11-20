-- Drop existing audit table
DROP TABLE IF EXISTS facturas_auditoria CASCADE;

-- Drop existing foreign key constraints and tables
DROP TABLE IF EXISTS lineas_factura CASCADE;
DROP TABLE IF EXISTS facturas CASCADE;

-- Create facturas table with new structure
CREATE TABLE facturas (
    id BIGSERIAL PRIMARY KEY,
    numero_factura VARCHAR(50) UNIQUE NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_envio TIMESTAMP,
    negocio_id BIGINT NOT NULL,
    subtotal NUMERIC(15, 2) NOT NULL DEFAULT 0,
    iva NUMERIC(15, 2) NOT NULL DEFAULT 0,
    total NUMERIC(15, 2) NOT NULL DEFAULT 0,
    estado VARCHAR(50) NOT NULL DEFAULT 'EN_REVISION' CHECK (estado IN ('EN_REVISION', 'ENVIADA')),
    observaciones TEXT,
    usuario_creacion VARCHAR(100),
    usuario_envio VARCHAR(100),
    path_pdf VARCHAR(500),
    CONSTRAINT fk_facturas_negocio FOREIGN KEY (negocio_id) REFERENCES negocios(id) ON DELETE RESTRICT
);

CREATE INDEX idx_facturas_negocio_id ON facturas(negocio_id);
CREATE INDEX idx_facturas_numero ON facturas(numero_factura);
CREATE INDEX idx_facturas_estado ON facturas(estado);
CREATE INDEX idx_facturas_fecha_creacion ON facturas(fecha_creacion);

-- Create lineas_factura table with new structure
CREATE TABLE lineas_factura (
    id BIGSERIAL PRIMARY KEY,
    factura_id BIGINT NOT NULL,
    numero_linea INTEGER NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    cantidad INTEGER NOT NULL,
    valor_unitario NUMERIC(15, 2) NOT NULL,
    total NUMERIC(15, 2) GENERATED ALWAYS AS (cantidad * valor_unitario) STORED,
    CONSTRAINT fk_lineas_factura_factura FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE,
    CONSTRAINT uc_factura_numero_linea UNIQUE (factura_id, numero_linea)
);

CREATE INDEX idx_lineas_factura_factura_id ON lineas_factura(factura_id);

-- Create simplified audit table
CREATE TABLE facturas_auditoria (
    id BIGSERIAL PRIMARY KEY,
    factura_id BIGINT NOT NULL,
    estado_anterior VARCHAR(50),
    estado_nuevo VARCHAR(50) NOT NULL,
    usuario VARCHAR(100),
    fecha_cambio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_auditoria_factura FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE
);

CREATE INDEX idx_auditoria_factura_id ON facturas_auditoria(factura_id);
CREATE INDEX idx_auditoria_fecha ON facturas_auditoria(fecha_cambio);

-- Create summary view
CREATE VIEW v_facturas_resumen AS
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
GROUP BY f.id, f.numero_factura, f.fecha_creacion, f.fecha_envio, 
         n.id, n.codigo, f.subtotal, f.iva, f.total, f.estado,
         f.usuario_creacion, f.usuario_envio;
