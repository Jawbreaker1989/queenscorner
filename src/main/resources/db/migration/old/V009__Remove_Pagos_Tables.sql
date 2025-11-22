-- V009__Remove_Pagos_Tables.sql
-- Remover completamente las tablas de pagos del proyecto (fueron removidas del modelo)

SET FOREIGN_KEY_CHECKS = 0;

-- Dropear tablas de auditoría primero (más seguro)
DROP TABLE IF EXISTS pagos_auditar;

-- Dropear tabla principal
DROP TABLE IF EXISTS pagos;

SET FOREIGN_KEY_CHECKS = 1;
