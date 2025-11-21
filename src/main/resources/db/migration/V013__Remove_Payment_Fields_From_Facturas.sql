-- Remove payment-related columns from facturas table
-- These fields are no longer needed as payment information is managed separately

ALTER TABLE facturas 
DROP COLUMN IF EXISTS fecha_vencimiento,
DROP COLUMN IF EXISTS medio_pago,
DROP COLUMN IF EXISTS referencia_pago,
DROP COLUMN IF EXISTS condiciones_pago;
