$ErrorActionPreference = 'Stop'
$baseUri = 'https://accenture-franchise-service.onrender.com'
$headers = @{ 'Content-Type' = 'application/json' }

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " PRUEBA E2E EN LA NUBE: $baseUri" -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

# 1. Healthcheck
Write-Host "`n1. Verificando Healthcheck Actuator..." -ForegroundColor Yellow
$health = Invoke-RestMethod -Uri "$baseUri/actuator/health" -Method Get
Write-Host "  -> Estado Global: $($health.status)" -ForegroundColor Green
Write-Host "  -> Base de Datos R2DBC: $($health.components.r2dbc.status) ($($health.components.r2dbc.details.database))" -ForegroundColor Green

# 2. Crear Franquicia
Write-Host "`n2. Creando Franquicia..." -ForegroundColor Yellow
$fName = "Juan Valdez Cloud " + (Get-Random -Minimum 100 -Maximum 999)
$f = Invoke-RestMethod -Uri "$baseUri/api/v1/franchises" -Method Post -Headers $headers -Body (@{ name = $fName } | ConvertTo-Json)
Write-Host "  -> Franquicia Creada: ID=$($f.id) | Nombre=$($f.name)" -ForegroundColor Green

# 3. Crear 2 Sucursales
Write-Host "`n3. Creando Sucursales en la Franquicia $($f.id)..." -ForegroundColor Yellow
$b1 = Invoke-RestMethod -Uri "$baseUri/api/v1/franchises/$($f.id)/branches" -Method Post -Headers $headers -Body (@{ name = "Sucursal El Poblado" } | ConvertTo-Json)
$b2 = Invoke-RestMethod -Uri "$baseUri/api/v1/franchises/$($f.id)/branches" -Method Post -Headers $headers -Body (@{ name = "Sucursal Laureles" } | ConvertTo-Json)
Write-Host "  -> Sucursal 1: ID=$($b1.id) | $($b1.name)" -ForegroundColor Green
Write-Host "  -> Sucursal 2: ID=$($b2.id) | $($b2.name)" -ForegroundColor Green

# 4. Agregar Productos
Write-Host "`n4. Agregando Productos con diferentes stocks..." -ForegroundColor Yellow
$p1 = Invoke-RestMethod -Uri "$baseUri/api/v1/branches/$($b1.id)/products" -Method Post -Headers $headers -Body (@{ name = "Nevado de Arequipe"; stock = 40 } | ConvertTo-Json)
$p2 = Invoke-RestMethod -Uri "$baseUri/api/v1/branches/$($b1.id)/products" -Method Post -Headers $headers -Body (@{ name = "Café Molido Gourmet"; stock = 120 } | ConvertTo-Json)
$p3 = Invoke-RestMethod -Uri "$baseUri/api/v1/branches/$($b2.id)/products" -Method Post -Headers $headers -Body (@{ name = "Tinto Campesino"; stock = 90 } | ConvertTo-Json)
$p4 = Invoke-RestMethod -Uri "$baseUri/api/v1/branches/$($b2.id)/products" -Method Post -Headers $headers -Body (@{ name = "Pod de Espresso"; stock = 50 } | ConvertTo-Json)
Write-Host "  -> Prod en $($b1.name): $($p1.name) (Stock: $($p1.stock)), $($p2.name) (Stock: $($p2.stock))" -ForegroundColor Green
Write-Host "  -> Prod en $($b2.name): $($p3.name) (Stock: $($p3.stock)), $($p4.name) (Stock: $($p4.stock))" -ForegroundColor Green

# 5. Consulta Analítica: Mayor Stock por Sucursal
Write-Host "`n5. [CRITERIO 7] Consultando Producto con Mayor Stock por Sucursal..." -ForegroundColor Yellow
$top = Invoke-RestMethod -Uri "$baseUri/api/v1/franchises/$($f.id)/max-stock-products" -Method Get
foreach ($item in $top) {
    Write-Host "  ★ Sucursal: $($item.branchName) => TOP PRODUCTO: $($item.topProduct.name) (Stock: $($item.topProduct.stock))" -ForegroundColor Magenta
}

# 6. Actualizar Stock
Write-Host "`n6. Modificando Stock de $($p4.name) de $($p4.stock) a 300 unidades..." -ForegroundColor Yellow
$p4Mod = Invoke-RestMethod -Uri "$baseUri/api/v1/products/$($p4.id)/stock" -Method Patch -Headers $headers -Body (@{ stock = 300 } | ConvertTo-Json)
Write-Host "  -> Nuevo stock de $($p4Mod.name): $($p4Mod.stock)" -ForegroundColor Green

# 7. Re-consultar Mayor Stock (debe cambiar el top)
Write-Host "`n7. Re-consultando Mayor Stock (verificar nuevo lider en $($b2.name))..." -ForegroundColor Yellow
$top2 = Invoke-RestMethod -Uri "$baseUri/api/v1/franchises/$($f.id)/max-stock-products" -Method Get
foreach ($item in $top2) {
    Write-Host "  ★ Sucursal: $($item.branchName) => NUEVO TOP: $($item.topProduct.name) (Stock: $($item.topProduct.stock))" -ForegroundColor Magenta
}

# 8. Actualizar Nombres (Puntos Extra)
Write-Host "`n8. [PUNTOS EXTRA] Actualizando nombres de Franquicia, Sucursal y Producto..." -ForegroundColor Yellow
$fUp = Invoke-RestMethod -Uri "$baseUri/api/v1/franchises/$($f.id)/name" -Method Patch -Headers $headers -Body (@{ name = "$fName Premium" } | ConvertTo-Json)
$bUp = Invoke-RestMethod -Uri "$baseUri/api/v1/branches/$($b1.id)/name" -Method Patch -Headers $headers -Body (@{ name = "$($b1.name) Flagship" } | ConvertTo-Json)
$pUp = Invoke-RestMethod -Uri "$baseUri/api/v1/products/$($p1.id)/name" -Method Patch -Headers $headers -Body (@{ name = "$($p1.name) Especial" } | ConvertTo-Json)
Write-Host "  -> Franquicia renombrada a: $($fUp.name)" -ForegroundColor Green
Write-Host "  -> Sucursal renombrada a:   $($bUp.name)" -ForegroundColor Green
Write-Host "  -> Producto renombrado a:   $($pUp.name)" -ForegroundColor Green

# 9. Eliminar Producto
Write-Host "`n9. Eliminando Producto $($p1.id) ($($p1.name))..." -ForegroundColor Yellow
Invoke-RestMethod -Uri "$baseUri/api/v1/products/$($p1.id)" -Method Delete
Write-Host "  -> Producto eliminado exitosamente (HTTP 204 No Content)." -ForegroundColor Green

$remaining = Invoke-RestMethod -Uri "$baseUri/api/v1/branches/$($b1.id)/products" -Method Get
Write-Host "  -> Productos restantes en $($b1.name): $($remaining.Count) (quedo: $($remaining.name))" -ForegroundColor Green

Write-Host "`n==========================================================" -ForegroundColor Cyan
Write-Host " TODOS LOS ENDPOINTS FUNCIONAN AL 100% EN LA NUBE" -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan
