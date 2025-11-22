-- Actualizar ENUM de estado en cotizaciones para incluir APROBADA
ALTER TABLE cotizaciones 
MODIFY COLUMN estado ENUM('BORRADOR', 'ENVIADA', 'APROBADA', 'RECHAZADA', 'VENCIDA') NOT NULL DEFAULT 'BORRADOR';
