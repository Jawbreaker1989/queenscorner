#!/usr/bin/env pwsh

$baseUrl = "http://localhost:8080/api"

# LOGIN
$loginResp = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -ContentType "application/json" -Body (@{ username = "admin"; password = "admin123" } | ConvertTo-Json)
$token = $loginResp.token

Write-Host "Token: $token`n" -ForegroundColor Green

$authHeaders = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer $token"
}

# CREAR COTIZACION
$cotPayload = @{
    clienteId = 1
    descripcion = "Test"
    fechaValidez = "2025-12-31"
    items = @(@{ descripcion = "Item 1"; cantidad = 1; precioUnitario = 100000 })
} | ConvertTo-Json -Depth 10

$cotResp = Invoke-RestMethod -Uri "$baseUrl/cotizaciones" -Method POST -Headers $authHeaders -Body $cotPayload

Write-Host "Respuesta cotizacion creada:" -ForegroundColor Green
$cotResp | ConvertTo-Json
