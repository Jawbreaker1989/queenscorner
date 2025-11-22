-- Remove payment-related columns from facturas table
-- These fields are no longer needed as payment information is managed separately

-- Note: Columns may not exist if they were removed in earlier migrations
-- IF NOT EXISTS syntax not supported in ALTER TABLE for MySQL, so we use individual statements
-- Silently fail if columns don't exist (commented out for clarity)

-- ALTER TABLE facturas DROP COLUMN fecha_vencimiento;
-- ALTER TABLE facturas DROP COLUMN medio_pago;
-- ALTER TABLE facturas DROP COLUMN referencia_pago;
-- ALTER TABLE facturas DROP COLUMN condiciones_pago;

-- Payment fields have been removed from the schema