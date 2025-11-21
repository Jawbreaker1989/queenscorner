#!/usr/bin/env pwsh

$baseUrl = "http://localhost:8080/api"

$loginPayload = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

$loginResp = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -ContentType "application/json" -Body $loginPayload

Write-Host "Respuesta completa:" -ForegroundColor Green
$loginResp | ConvertTo-Json
