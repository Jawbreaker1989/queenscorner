-- Renombrar presupuesto_utilizado a anticipo en tabla negocios
-- Esta columna ahora representa el anticipo otorgado con la cotización aprobada

ALTER TABLE negocios 
CHANGE COLUMN presupuesto_utilizado anticipo DECIMAL(15, 2) DEFAULT 0.00;

-- Actualizar índices si existen referencias
-- (El nombre de la columna no afecta los índices existentes)
