# Consolidación de Migraciones - QueensCorner

## Resumen de Cambios
Se ha consolidado todas las migraciones de base de datos (V001-V016) en una sola migración V017__Consolidate_All_Migrations.sql que representa el esquema final.

## Archivos Movidos a `old/`
- V001__Initial_Schema.sql
- V002__Add_Denormalized_Cotizacion_Fields_To_Negocios.sql
- V003__Rename_PresupuestoUtilizado_To_Anticipo.sql
- V004__Create_Facturas_Tables.sql
- V005__Clean_All_Data.sql
- V006__Update_Cotizacion_Estado_Enum.sql
- V007__Fix_Enum_Inconsistencies.sql
- V008__Remove_Ordenes_Trabajo_Tables.sql
- V009__Remove_Pagos_Tables.sql
- V010__Remove_Current_Facturas_Structure.sql
- V011__Rebuild_Facturas_Structure.sql
- V012__Update_Factura_Estado_To_EN_REVISION.sql
- V013__Remove_Payment_Fields_From_Facturas.sql
- V014__Drop_Payment_Columns_Separately.sql
- V015__Force_Drop_Payment_Columns.sql
- V016__Remove_Invoice_States_Use_ENVIADA.sql

## Archivos Nuevos
- V017__Consolidate_All_Migrations.sql: Esquema consolidado final
- V018__Validate_Consolidation.sql: Validación de la consolidación

## Próximos Pasos para Producción
1. **Probar en Staging**: Ejecutar V017 en una copia de la base de datos de producción
2. **Validar**: Ejecutar V018 para verificar que todo esté correcto
3. **Merge al branch principal**: Una vez probado, mergear `feature/consolidate-migrations` a `release/v1.0.0-final`
4. **Desplegar**: Actualizar la aplicación en producción con la nueva migración

## Precauciones Tomadas
- ✅ Branch separado para cambios
- ✅ Historial Git preservado
- ✅ Migraciones antiguas movidas, no eliminadas
- ✅ Validación incluida
- ✅ Sin cambios en código de aplicación

## Rollback si es Necesario
Si hay problemas, revertir el merge y las migraciones antiguas estarán disponibles en `old/`.