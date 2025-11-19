-- Corregir inconsistencias de enums
-- 1. Usuario Rol solo debe tener ADMIN
ALTER TABLE usuarios 
MODIFY COLUMN rol ENUM('ADMIN') NOT NULL DEFAULT 'ADMIN';

-- 2. Verificar que cotizaciones tiene APROBADA (ya aplicado en V006)
-- Esta línea es solo referencial
-- ALTER TABLE cotizaciones 
-- MODIFY COLUMN estado ENUM('BORRADOR', 'ENVIADA', 'APROBADA', 'RECHAZADA', 'VENCIDA') NOT NULL DEFAULT 'BORRADOR';
