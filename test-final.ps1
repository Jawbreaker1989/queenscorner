#!/usr/bin/env pwsh

$baseUrl = "http://localhost:8080/api"
$clienteId = 1

# LOGIN
$loginResp = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -ContentType "application/json" -Body (@{ username = "admin"; password = "admin123" } | ConvertTo-Json) -ErrorAction Stop
$token = $loginResp.token

Write-Host "Token obtenido. Continuando..." -ForegroundColor Green

$authHeaders = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer $token"
}

# CREAR COTIZACION
$cotPayload = @{
    clienteId = $clienteId
    descripcion = "Test PDF"
    fechaValidez = "2025-12-31"
    items = @(@{ descripcion = "Item 1"; cantidad = 1; precioUnitario = 100000 })
} | ConvertTo-Json -Depth 10

$cotResp = Invoke-RestMethod -Uri "$baseUrl/cotizaciones" -Method POST -Headers $authHeaders -Body $cotPayload -ErrorAction Stop
$cotId = $cotResp.id
$codigo = $cotResp.codigo

Write-Host "Cotizacion creada: $codigo" -ForegroundColor Green

# APROBAR INMEDIATAMENTE
$aprPayload = @{ estado = "APROBADA" } | ConvertTo-Json

$aprResp = Invoke-RestMethod -Uri "$baseUrl/cotizaciones/$cotId" -Method PUT -Headers $authHeaders -Body $aprPayload -ErrorAction Stop

Write-Host "Cotizacion aprobada. Esperando generacion..." -ForegroundColor Green

# ESPERAR Y VERIFICAR
Start-Sleep -Seconds 6

$pdfPath = "D:\queenscorner\queenscornerarchives\cotizaciones\$codigo.pdf"

if (Test-Path $pdfPath) {
    $size = (Get-Item $pdfPath).Length
    Write-Host ""
    Write-Host "EXITO - PDF GENERADO" -ForegroundColor Green
    Write-Host "Archivo: $codigo.pdf" -ForegroundColor Cyan
    Write-Host "Tamano: $size bytes" -ForegroundColor Cyan
} else {
    Write-Host ""
    Write-Host "ERROR - PDF NO ENCONTRADO" -ForegroundColor Red
    Write-Host "Ruta esperada: $pdfPath" -ForegroundColor Red
}
