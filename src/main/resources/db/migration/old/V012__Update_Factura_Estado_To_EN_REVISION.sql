-- V012__Update_Factura_Estado_To_EN_REVISION.sql
-- Verificación de que el estado de facturas es EN_REVISION
-- (Esta migración es principalmente para documentación)
-- La estructura de facturas ya fue recreada en V011 con estado EN_REVISION

-- Verificar que todas las facturas existentes tengan estado válido
-- Si hay migraciones anteriores a V011, asegurar que el estado sea correcto
UPDATE facturas 
SET estado = 'EN_REVISION' 
WHERE estado NOT IN ('EN_REVISION', 'ENVIADA', 'PAGADA', 'ANULADA') OR estado IS NULL;

