-- Eliminar presupuesto_utilizado de tabla negocios (anticipo ya existe)
-- El anticipo es el monto pagado/reservado para el negocio

ALTER TABLE negocios DROP COLUMN presupuesto_utilizado;
