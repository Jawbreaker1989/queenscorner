-- Renombrar presupuesto_utilizado a anticipo en tabla negocios
-- El anticipo es el monto pagado/reservado para el negocio (campo exclusivo del negocio, no de cotización)

ALTER TABLE negocios 
CHANGE COLUMN presupuesto_utilizado anticipo DECIMAL(15, 2) DEFAULT 0.00;

-- Actualizar índices si existen referencias
-- (El nombre de la columna no afecta los índices existentes)
