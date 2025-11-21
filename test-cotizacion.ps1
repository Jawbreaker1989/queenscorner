#!/usr/bin/env pwsh

Write-Host ""
Write-Host "PRUEBA DE GENERACION DE PDF - COTIZACIÓN" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/api"
$clienteId = 1

$authHeaders = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MzU5MDM4NywiZXhwIjoxNzYzNjc2Nzg3fQ.W_LItqHqtv_PbY-MDNp_iv7SwtyWYAIADwRKoKDJdpA"
}

Write-Host "Configuracion:" -ForegroundColor Green
Write-Host "   URL Base: $baseUrl" -ForegroundColor Cyan
Write-Host "   Cliente ID: $clienteId" -ForegroundColor Cyan
Write-Host ""

# 1. Crear cotización
Write-Host "1. Creando cotización..." -ForegroundColor Green

$cotizacionPayload = @{
    clienteId = $clienteId
    descripcion = "Servicio de consultoría técnica para desarrollo de software"
    fechaValidez = "2025-12-31"
    observaciones = "Presupuesto sujeto a confirmación de alcance"
    items = @(
        @{
            descripcion = "Análisis de requerimientos"
            cantidad = 40
            precioUnitario = 100000
        },
        @{
            descripcion = "Desarrollo de componentes"
            cantidad = 160
            precioUnitario = 120000
        },
        @{
            descripcion = "Testing y QA"
            cantidad = 40
            precioUnitario = 80000
        }
    )
} | ConvertTo-Json -Depth 10

try {
    $cotizacionResp = Invoke-RestMethod -Uri "$baseUrl/cotizaciones" -Method POST -Headers $authHeaders -Body $cotizacionPayload -ErrorAction Stop
    $cotizacionId = $cotizacionResp.id
    $codigo = $cotizacionResp.codigo
    
    Write-Host "Cotización creada exitosamente" -ForegroundColor Green
    Write-Host "   ID: $cotizacionId" -ForegroundColor Cyan
    Write-Host "   Código: $codigo" -ForegroundColor Cyan
    Write-Host "   Estado: $($cotizacionResp.estado)" -ForegroundColor Cyan
    Write-Host "   Total: $($cotizacionResp.total)" -ForegroundColor Cyan
} catch {
    Write-Host "ERROR creando cotización:" -ForegroundColor Red
    Write-Host "   $_" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "2. Aprobando cotización (esto debe generar PDF en background)..." -ForegroundColor Green

$aprobarPayload = @{
    estado = "APROBADA"
} | ConvertTo-Json

try {
    $aprobarResp = Invoke-RestMethod -Uri "$baseUrl/cotizaciones/$cotizacionId" -Method PUT -Headers $authHeaders -Body $aprobarPayload -ErrorAction Stop
    
    Write-Host "Cotización aprobada exitosamente" -ForegroundColor Green
    Write-Host "   Estado: $($aprobarResp.estado)" -ForegroundColor Cyan
} catch {
    Write-Host "ERROR aprobando cotización:" -ForegroundColor Red
    Write-Host "   $_" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "3. Esperando generación de PDF (5 segundos)..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

Write-Host ""
Write-Host "4. Verificando si se creó el archivo PDF..." -ForegroundColor Green

$pdfPath = "D:\queenscorner\queenscornerarchives\cotizaciones\$codigo.pdf"

if (Test-Path $pdfPath) {
    $fileSize = (Get-Item $pdfPath).Length
    Write-Host "✅ PDF CREADO EXITOSAMENTE" -ForegroundColor Green
    Write-Host "   Ruta: $pdfPath" -ForegroundColor Cyan
    Write-Host "   Tamaño: $fileSize bytes" -ForegroundColor Cyan
} else {
    Write-Host "❌ PDF NO ENCONTRADO" -ForegroundColor Red
    Write-Host "   Ruta esperada: $pdfPath" -ForegroundColor Red
    Write-Host "   Verificando directorio..." -ForegroundColor Yellow
    
    $directorio = "D:\queenscorner\queenscornerarchives\cotizaciones"
    if (Test-Path $directorio) {
        Write-Host "   Archivos en el directorio:" -ForegroundColor Cyan
        Get-ChildItem $directorio | ForEach-Object { Write-Host "   - $($_.Name)" -ForegroundColor Gray }
    } else {
        Write-Host "   Directorio no existe: $directorio" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "PRUEBA COMPLETADA" -ForegroundColor Green
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""
