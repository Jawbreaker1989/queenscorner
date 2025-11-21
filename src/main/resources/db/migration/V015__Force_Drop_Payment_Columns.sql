-- Force drop payment columns from facturas table
-- Using ALTER IGNORE to skip errors if columns don't exist

ALTER TABLE facturas DROP COLUMN IF EXISTS fecha_vencimiento;
ALTER TABLE facturas DROP COLUMN IF EXISTS medio_pago;
ALTER TABLE facturas DROP COLUMN IF EXISTS referencia_pago;
ALTER TABLE facturas DROP COLUMN IF EXISTS condiciones_pago;
