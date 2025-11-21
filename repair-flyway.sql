-- Script para reparar Flyway

-- Ver el estado actual
SELECT * FROM flyway_schema_history;

-- Limpiar el historial problemático
-- DELETE FROM flyway_schema_history WHERE version >= 2;

-- Para reparar una migración fallida, usar:
-- UPDATE flyway_schema_history SET success = 1 WHERE version = 3;
