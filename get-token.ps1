#!/usr/bin/env pwsh

$baseUrl = "http://localhost:8080/api"

$loginPayload = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

Write-Host "Obteniendo token..." -ForegroundColor Yellow

try {
    $loginResp = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -ContentType "application/json" -Body $loginPayload -ErrorAction Stop
    $token = $loginResp.token
    
    Write-Host "Token obtenido:" -ForegroundColor Green
    Write-Host $token
    
} catch {
    Write-Host "Error en login:" -ForegroundColor Red
    Write-Host $_.Exception.Message
}
