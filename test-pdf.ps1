#!/usr/bin/env pwsh

Write-Host ""
Write-Host "TEST PDF COTIZACION" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/api"
$clienteId = 1

# 1. LOGIN
Write-Host "1. Obteniendo token..." -ForegroundColor Green

$loginPayload = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

try {
    $loginResp = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -ContentType "application/json" -Body $loginPayload -ErrorAction Stop
    $token = $loginResp.token
    Write-Host "   Token: OK" -ForegroundColor Green
} catch {
    Write-Host "   ERROR: $_" -ForegroundColor Red
    exit 1
}

$authHeaders = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer $token"
}

# 2. CREAR COTIZACION
Write-Host ""
Write-Host "2. Creando cotizacion..." -ForegroundColor Green

$cotizacionPayload = @{
    clienteId = $clienteId
    descripcion = "Desarrollo de software"
    fechaValidez = "2025-12-31"
    observaciones = "Test PDF"
    items = @(
        @{
            descripcion = "Desarrollo"
            cantidad = 40
            precioUnitario = 100000
        }
    )
} | ConvertTo-Json -Depth 10

try {
    $cotizacionResp = Invoke-RestMethod -Uri "$baseUrl/cotizaciones" -Method POST -Headers $authHeaders -Body $cotizacionPayload -ErrorAction Stop
    $cotizacionId = $cotizacionResp.id
    $codigo = $cotizacionResp.codigo
    Write-Host "   Codigo: $codigo" -ForegroundColor Green
    Write-Host "   ID: $cotizacionId" -ForegroundColor Green
} catch {
    Write-Host "   ERROR: $_" -ForegroundColor Red
    exit 1
}

# 3. APROBAR COTIZACION
Write-Host ""
Write-Host "3. Aprobando cotizacion..." -ForegroundColor Green

$aprobarPayload = @{
    estado = "APROBADA"
} | ConvertTo-Json

try {
    $aprobarResp = Invoke-RestMethod -Uri "$baseUrl/cotizaciones/$cotizacionId" -Method PUT -Headers $authHeaders -Body $aprobarPayload -ErrorAction Stop
    Write-Host "   Estado: $($aprobarResp.estado)" -ForegroundColor Green
} catch {
    Write-Host "   ERROR: $_" -ForegroundColor Red
    exit 1
}

# 4. ESPERAR GENERACION
Write-Host ""
Write-Host "4. Esperando generacion PDF (5 seg)..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

# 5. VERIFICAR PDF
Write-Host ""
Write-Host "5. Verificando archivo..." -ForegroundColor Green

$pdfPath = "D:\queenscorner\queenscornerarchives\cotizaciones\$codigo.pdf"

if (Test-Path $pdfPath) {
    $fileSize = (Get-Item $pdfPath).Length
    Write-Host "   EXITO PDF CREADO" -ForegroundColor Green
    Write-Host "   Archivo: $codigo.pdf" -ForegroundColor Cyan
    Write-Host "   Tamano: $fileSize bytes" -ForegroundColor Cyan
} else {
    Write-Host "   ERROR PDF NO ENCONTRADO" -ForegroundColor Red
    Write-Host "   Ruta: $pdfPath" -ForegroundColor Red
    
    $directorio = "D:\queenscorner\queenscornerarchives\cotizaciones"
    if (Test-Path $directorio) {
        $archivos = Get-ChildItem $directorio
        if ($archivos) {
            Write-Host "   Archivos en directorio:" -ForegroundColor Yellow
            $archivos | ForEach-Object { Write-Host "   - $($_.Name) ($($_.Length) bytes)" }
        } else {
            Write-Host "   Directorio vacio" -ForegroundColor Yellow
        }
    }
}

Write-Host ""
Write-Host "================================================" -ForegroundColor Cyan
