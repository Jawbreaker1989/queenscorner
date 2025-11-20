#!/usr/bin/env pwsh

$baseUrl = "http://localhost:8080/api"

# Headers
$headers = @{
    "Content-Type" = "application/json"
}

Write-Host "🔄 Iniciando proceso de creación y emisión de factura..." -ForegroundColor Cyan

# 0. Obtener token (registro/login)
Write-Host "`n0️⃣ Obteniendo token de autenticación..." -ForegroundColor Green
$loginPayload = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

try {
    $tokenResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -Headers $headers -Body $loginPayload
    $token = $tokenResponse.token
    $headers["Authorization"] = "Bearer $token"
    Write-Host "✅ Token obtenido" -ForegroundColor Green
} catch {
    Write-Host "⚠️ Token request failed, intentando continuar..." -ForegroundColor Yellow
}

# 1. Crear Cliente
Write-Host "`n1️⃣ Creando cliente..." -ForegroundColor Green
$clientePayload = @{
    nombre = "Empresa Test"
    documento = "1234567890"
    email = "empresa@test.com"
    telefono = "3105551234"
    direccion = "Calle 1 #1"
    ciudad = "Bogotá"
} | ConvertTo-Json

try {
    $clienteResponse = Invoke-RestMethod -Uri "$baseUrl/clientes" -Method POST -Headers $headers -Body $clientePayload
    $clienteId = $clienteResponse.id
    Write-Host "✅ Cliente creado: ID=$clienteId" -ForegroundColor Green
} catch {
    Write-Host "❌ Error creando cliente: $_" -ForegroundColor Red
    exit 1
}

# 2. Crear Cotización
Write-Host "`n2️⃣ Creando cotización..." -ForegroundColor Green
$cotizacionPayload = @{
    cliente_id = $clienteId
    codigo = "COT-$(Get-Date -Format 'yyyyMMdd-HHmmss')"
    descripcion = "Prueba de facturación"
    items = @(
        @{
            descripcion = "Instalación"
            cantidad = 1
            valor_unitario = 500000
        },
        @{
            descripcion = "Transporte"
            cantidad = 1
            valor_unitario = 50000
        }
    )
} | ConvertTo-Json

try {
    $cotizacionResponse = Invoke-RestMethod -Uri "$baseUrl/cotizaciones" -Method POST -Headers $headers -Body $cotizacionPayload
    $cotizacionId = $cotizacionResponse.id
    Write-Host "✅ Cotización creada: ID=$cotizacionId" -ForegroundColor Green
} catch {
    Write-Host "❌ Error creando cotización: $_" -ForegroundColor Red
    exit 1
}

# 3. Crear Negocio
Write-Host "`n3️⃣ Creando negocio..." -ForegroundColor Green
$negocioPayload = @{
    cotizacion_id = $cotizacionId
} | ConvertTo-Json

try {
    $negocioResponse = Invoke-RestMethod -Uri "$baseUrl/negocios" -Method POST -Headers $headers -Body $negocioPayload
    $negocioId = $negocioResponse.id
    Write-Host "✅ Negocio creado: ID=$negocioId" -ForegroundColor Green
} catch {
    Write-Host "❌ Error creando negocio: $_" -ForegroundColor Red
    exit 1
}

# 4. Crear Factura
Write-Host "`n4️⃣ Creando factura..." -ForegroundColor Green
$facturaPayload = @{
    negocioId = $negocioId
    observaciones = "Factura de prueba para validación del sistema"
    lineas = @(
        @{
            descripcion = "Instalación de sistema"
            cantidad = 1
            valorUnitario = 500000
        },
        @{
            descripcion = "Transporte e instalación"
            cantidad = 1
            valorUnitario = 50000
        }
    )
} | ConvertTo-Json

try {
    $facturaResponse = Invoke-RestMethod -Uri "$baseUrl/facturas" -Method POST -Headers $headers -Body $facturaPayload
    $facturaId = $facturaResponse.id
    Write-Host "✅ Factura creada: ID=$facturaId, Número=$($facturaResponse.numeroFactura)" -ForegroundColor Green
} catch {
    Write-Host "❌ Error creando factura: $_" -ForegroundColor Red
    exit 1
}

# 5. Enviar Factura
Write-Host "`n5️⃣ Enviando factura (genera PDF)..." -ForegroundColor Green
$enviarPayload = @{} | ConvertTo-Json

try {
    $enviarResponse = Invoke-RestMethod -Uri "$baseUrl/facturas/$facturaId/enviar" -Method POST -Headers $headers -Body $enviarPayload
    Write-Host "✅ Factura enviada exitosamente" -ForegroundColor Green
    Write-Host "   Estado: $($enviarResponse.estado)" -ForegroundColor Cyan
    Write-Host "   Subtotal: $$($enviarResponse.subtotal)" -ForegroundColor Cyan
    Write-Host "   IVA (19%): $$($enviarResponse.iva)" -ForegroundColor Cyan
    Write-Host "   Total: $$($enviarResponse.total)" -ForegroundColor Cyan
    Write-Host "   PDF generado en: $($enviarResponse.pathPdf)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Error enviando factura: $_" -ForegroundColor Red
    exit 1
}

Write-Host "`n✅ Proceso completado exitosamente!" -ForegroundColor Green
