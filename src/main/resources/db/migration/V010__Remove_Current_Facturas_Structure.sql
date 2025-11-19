-- V010__Remove_Current_Facturas_Structure.sql
-- Remover estructura actual de facturas para reestructuración mayor

SET FOREIGN_KEY_CHECKS = 0;

-- Remover vista de reportes
DROP VIEW IF EXISTS v_facturas_resumen;

-- Remover tablas en orden (FK primero)
DROP TABLE IF EXISTS facturas_auditoria;
DROP TABLE IF EXISTS lineas_factura;
DROP TABLE IF EXISTS facturas;

SET FOREIGN_KEY_CHECKS = 1;
