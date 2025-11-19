-- V005__Clean_All_Data.sql
-- Limpiar completamente la base de datos manteniendo la estructura

-- Deshabilitar verificación de claves foráneas temporalmente
SET FOREIGN_KEY_CHECKS = 0;

-- Limpiar tablas en orden (respetando dependencias)
TRUNCATE TABLE facturas_auditoria;
TRUNCATE TABLE lineas_factura;
TRUNCATE TABLE facturas;
TRUNCATE TABLE ordenes_trabajo;
TRUNCATE TABLE detalles_orden_trabajo;
TRUNCATE TABLE pagos;
TRUNCATE TABLE pagos_auditar;
TRUNCATE TABLE items_cotizacion;
TRUNCATE TABLE cotizaciones;
TRUNCATE TABLE negocios;
TRUNCATE TABLE clientes;
TRUNCATE TABLE usuarios;

-- Re-habilitar verificación de claves foráneas
SET FOREIGN_KEY_CHECKS = 1;

-- Resetear AUTO_INCREMENT en todas las tablas
ALTER TABLE usuarios AUTO_INCREMENT = 1;
ALTER TABLE clientes AUTO_INCREMENT = 1;
ALTER TABLE cotizaciones AUTO_INCREMENT = 1;
ALTER TABLE items_cotizacion AUTO_INCREMENT = 1;
ALTER TABLE negocios AUTO_INCREMENT = 1;
ALTER TABLE facturas AUTO_INCREMENT = 1;
ALTER TABLE lineas_factura AUTO_INCREMENT = 1;
ALTER TABLE ordenes_trabajo AUTO_INCREMENT = 1;
ALTER TABLE detalles_orden_trabajo AUTO_INCREMENT = 1;
ALTER TABLE pagos AUTO_INCREMENT = 1;
ALTER TABLE pagos_auditar AUTO_INCREMENT = 1;
ALTER TABLE facturas_auditoria AUTO_INCREMENT = 1;
