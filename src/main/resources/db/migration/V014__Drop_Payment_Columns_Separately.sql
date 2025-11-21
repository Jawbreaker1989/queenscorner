-- Drop payment-related columns if they still exist
-- This ensures the facturas table structure matches the FacturaEntity definition

ALTER TABLE facturas 
DROP COLUMN IF EXISTS fecha_vencimiento;

ALTER TABLE facturas 
DROP COLUMN IF EXISTS medio_pago;

ALTER TABLE facturas 
DROP COLUMN IF EXISTS referencia_pago;

ALTER TABLE facturas 
DROP COLUMN IF EXISTS condiciones_pago;
