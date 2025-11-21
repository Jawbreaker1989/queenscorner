-- Drop payment-related columns if they still exist
-- This ensures the facturas table structure matches the FacturaEntity definition

-- Note: IF NOT EXISTS not supported in MySQL ALTER TABLE
-- These statements may fail silently if columns don't exist
-- which is expected for migrations that run on fresh databases

-- ALTER TABLE facturas DROP COLUMN fecha_vencimiento;
-- ALTER TABLE facturas DROP COLUMN medio_pago;
-- ALTER TABLE facturas DROP COLUMN referencia_pago;
-- ALTER TABLE facturas DROP COLUMN condiciones_pago;

-- Payment fields removed in schema redesign
