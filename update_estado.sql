USE queens_corner_prod_v2;
UPDATE facturas SET estado = 'ENVIADA';
SELECT COUNT(*) as total, estado FROM facturas GROUP BY estado;
