#!/usr/bin/env pwsh

$baseUrl = "http://localhost:8080/api"

# LOGIN
$loginResp = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -ContentType "application/json" -Body (@{ username = "admin"; password = "admin123" } | ConvertTo-Json)
$token = $loginResp.token

$authHeaders = @{
    "Authorization" = "Bearer $token"
}

Write-Host "1. Obteniendo cotizaciones existentes..." -ForegroundColor Green
$allCots = Invoke-RestMethod -Uri "$baseUrl/cotizaciones" -Method GET -Headers $authHeaders

Write-Host "   Total cotizaciones: $($allCots.data.Count)" -ForegroundColor Cyan

if ($allCots.data.Count -gt 0) {
    $lastCot = $allCots.data[-1]
    $cotId = $lastCot.id
    $codigo = $lastCot.codigo
    
    Write-Host "   Ultima: $codigo (ID: $cotId)" -ForegroundColor Green
    
    # LLAMAR ENDPOINT DE PDF
    Write-Host ""
    Write-Host "2. Triggeando generacion de PDF..." -ForegroundColor Green
    
    try {
        $pdfResp = Invoke-RestMethod -Uri "$baseUrl/cotizaciones/$cotId/pdf" -Method POST -Headers $authHeaders -Body "{}" -ErrorAction Stop
        Write-Host "   Respuesta: $($pdfResp.message)" -ForegroundColor Green
    } catch {
        Write-Host "   ERROR: $_" -ForegroundColor Red
    }
    
    Write-Host ""
    Write-Host "3. Esperando generacion (5 seg)..." -ForegroundColor Yellow
    Start-Sleep -Seconds 5
    
    # VERIFICAR ARCHIVO
    Write-Host ""
    Write-Host "4. Verificando PDF..." -ForegroundColor Green
    
    $pdfPath = "D:\queenscorner\queenscornerarchives\cotizaciones\$codigo.pdf"
    
    if (Test-Path $pdfPath) {
        $size = (Get-Item $pdfPath).Length
        Write-Host "   EXITO - PDF GENERADO" -ForegroundColor Green
        Write-Host "   Archivo: $codigo.pdf" -ForegroundColor Cyan
        Write-Host "   Tamano: $size bytes" -ForegroundColor Cyan
    } else {
        Write-Host "   FALLO - PDF NO ENCONTRADO" -ForegroundColor Red
        Write-Host "   Ruta: $pdfPath" -ForegroundColor Red
    }
} else {
    Write-Host "   No hay cotizaciones!" -ForegroundColor Red
}
