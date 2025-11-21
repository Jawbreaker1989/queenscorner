-- V016__Remove_Invoice_States_Use_ENVIADA.sql
-- Remove invoice state management - all invoices are created with state ENVIADA
-- Change enum to varchar and set all invoices to ENVIADA

-- Change the estado column from ENUM to VARCHAR and set default to ENVIADA
ALTER TABLE facturas 
MODIFY COLUMN estado VARCHAR(50) NOT NULL DEFAULT 'ENVIADA';

-- Update all existing invoices to ENVIADA state
UPDATE facturas SET estado = 'ENVIADA' WHERE estado IS NULL OR estado != 'ENVIADA';

-- Create an index on estado for performance (already exists but ensure it's there)
CREATE INDEX idx_facturas_estado ON facturas(estado);
