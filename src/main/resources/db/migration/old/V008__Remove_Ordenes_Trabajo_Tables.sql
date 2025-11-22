-- V008__Remove_Ordenes_Trabajo_Tables.sql
-- Remover completamente las tablas de órdenes de trabajo del proyecto

SET FOREIGN_KEY_CHECKS = 0;

-- Dropear tablas de detalles primero (FK)
DROP TABLE IF EXISTS detalles_orden_trabajo;

-- Dropear tabla principal
DROP TABLE IF EXISTS ordenes_trabajo;

SET FOREIGN_KEY_CHECKS = 1;
