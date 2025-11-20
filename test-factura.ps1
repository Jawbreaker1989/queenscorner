#!/usr/bin/env pwsh

Write-Host ""
Write-Host "EMISION DE FACTURA - QUEENSCORNER" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/api"
$negocioId = 1

$authHeaders = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MzU5MDM4NywiZXhwIjoxNzYzNjc2Nzg3fQ.W_LItqHqtv_PbY-MDNp_iv7SwtyWYAIADwRKoKDJdpA"
}

Write-Host "Configuracion inicial:" -ForegroundColor Green
Write-Host "   URL Base: $baseUrl" -ForegroundColor Cyan
Write-Host "   Negocio ID: $negocioId" -ForegroundColor Cyan
Write-Host ""

# 1. Crear factura
Write-Host "1. Creando factura..." -ForegroundColor Green

$facturaPayload = @{
    negocioId = $negocioId
    observaciones = "Factura de prueba - Sistema validado"
    lineas = @(
        @{
            descripcion = "Servicio de instalacion"
            cantidad = 1.0
            valorUnitario = 500000.00
        },
        @{
            descripcion = "Transporte"
            cantidad = 1.0
            valorUnitario = 50000.00
        }
    )
} | ConvertTo-Json -Depth 10

try {
    $facturaResp = Invoke-RestMethod -Uri "$baseUrl/facturas" -Method POST -Headers $authHeaders -Body $facturaPayload -ErrorAction Stop
    $facturaId = $facturaResp.id
    $numeroFactura = $facturaResp.numeroFactura
    
    Write-Host "Factura creada exitosamente" -ForegroundColor Green
    Write-Host "   ID: $facturaId" -ForegroundColor Cyan
    Write-Host "   Numero: $numeroFactura" -ForegroundColor Cyan
    Write-Host "   Estado: $($facturaResp.estado)" -ForegroundColor Cyan
    Write-Host "   Subtotal: $($facturaResp.subtotal)" -ForegroundColor Cyan
    Write-Host "   IVA: $($facturaResp.iva)" -ForegroundColor Cyan
    Write-Host "   Total: $($facturaResp.total)" -ForegroundColor Cyan
} catch {
    Write-Host "ERROR creando factura:" -ForegroundColor Red
    Write-Host "   $_" -ForegroundColor Red
    exit 1
}

Write-Host ""

# 2. Enviar factura
Write-Host "2. Enviando factura..." -ForegroundColor Green

try {
    $enviarResp = Invoke-RestMethod -Uri "$baseUrl/facturas/$facturaId/enviar" -Method POST -Headers $authHeaders -Body "{}" -ErrorAction Stop
    
    Write-Host "Factura enviada exitosamente" -ForegroundColor Green
    Write-Host "   Estado: $($enviarResp.estado)" -ForegroundColor Cyan
    Write-Host "   Fecha Envio: $($enviarResp.fechaEnvio)" -ForegroundColor Cyan
    Write-Host "   Path PDF: $($enviarResp.pathPdf)" -ForegroundColor Cyan
} catch {
    Write-Host "ERROR enviando factura:" -ForegroundColor Red
    Write-Host "   $_" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "PROCESO COMPLETADO EXITOSAMENTE" -ForegroundColor Green
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Resumen de factura:" -ForegroundColor Yellow
Write-Host "   Numero: $numeroFactura" -ForegroundColor Cyan
Write-Host "   Subtotal: $($enviarResp.subtotal)" -ForegroundColor Cyan
Write-Host "   IVA: $($enviarResp.iva)" -ForegroundColor Cyan
Write-Host "   TOTAL: $($enviarResp.total)" -ForegroundColor Green
Write-Host ""
Write-Host "PDF generado en:" -ForegroundColor Yellow
Write-Host "   $($enviarResp.pathPdf)" -ForegroundColor Cyan
Write-Host ""
