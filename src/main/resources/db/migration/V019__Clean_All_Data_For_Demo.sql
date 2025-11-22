-- ============================================
-- V019__Clean_All_Data_For_Demo.sql
-- ============================================
-- Limpia completamente la base de datos para demo
-- Mantiene la estructura pero elimina todos los datos
-- ============================================

SET FOREIGN_KEY_CHECKS = 0;

-- Limpiar tablas en orden inverso a las dependencias
TRUNCATE TABLE facturas_auditoria;
TRUNCATE TABLE lineas_factura;
TRUNCATE TABLE facturas;
TRUNCATE TABLE items_cotizacion;
TRUNCATE TABLE cotizaciones;
TRUNCATE TABLE negocios;
TRUNCATE TABLE clientes;
TRUNCATE TABLE usuarios;

-- Resetear AUTO_INCREMENT
ALTER TABLE usuarios AUTO_INCREMENT = 1;
ALTER TABLE clientes AUTO_INCREMENT = 1;
ALTER TABLE cotizaciones AUTO_INCREMENT = 1;
ALTER TABLE items_cotizacion AUTO_INCREMENT = 1;
ALTER TABLE negocios AUTO_INCREMENT = 1;
ALTER TABLE facturas AUTO_INCREMENT = 1;
ALTER TABLE lineas_factura AUTO_INCREMENT = 1;
ALTER TABLE facturas_auditoria AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;