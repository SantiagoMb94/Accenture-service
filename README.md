# Franchise Management Service

API reactiva para la administración de franquicias, sucursales y productos, desarrollada con Java 17, Spring WebFlux y R2DBC sobre PostgreSQL.

El proyecto implementa una arquitectura limpia (hexagonal) donde el dominio y los casos de uso se mantienen aislados de frameworks e infraestructura.

---

## 🌐 Despliegue en Vivo en la Nube (Producción)

La solución se encuentra completamente aprovisionada y **desplegada en vivo en la nube** con base de datos PostgreSQL administrada y servicio de contenedores Docker:

- **URL Base:** `https://accenture-franchise-service.onrender.com`
- **Healthcheck Actuator:** `https://accenture-franchise-service.onrender.com/actuator/health`
- **API Base:** `https://accenture-franchise-service.onrender.com/api/v1`

> [!TIP]
> Puedes probar la API en vivo directamente enviando peticiones HTTPS a `https://accenture-franchise-service.onrender.com` o ejecutando los ejemplos `cURL` de este documento sustituyendo `http://localhost:8080` por la URL en la nube.

---

## Inicio rápido

Para levantar la aplicación y la base de datos localmente solo necesitas Docker:

```bash
docker compose up --build
```

Una vez arriba:
- **API:** `http://localhost:8080/api/v1`
- **Healthcheck:** `http://localhost:8080/actuator/health`
- **PostgreSQL:** `localhost:5432` (db: `franchise_db`, user: `postgres`, pass: `postgrespassword`)

Para apagar los contenedores y limpiar volúmenes:
```bash
docker compose down -v
```

Si prefieres correrlo sin Docker:
1. Asegúrate de tener una base PostgreSQL corriendo en el puerto 5432.
2. Ejecuta:
```bash
./gradlew bootRun
```

---

## Endpoints

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `POST` | `/api/v1/franchises` | Crear franquicia |
| `GET` | `/api/v1/franchises` | Listar franquicias |
| `GET` | `/api/v1/franchises/{id}` | Obtener franquicia por ID |
| `PATCH` | `/api/v1/franchises/{id}/name` | Modificar nombre de franquicia |
| `POST` | `/api/v1/franchises/{franchiseId}/branches` | Agregar sucursal a una franquicia |
| `GET` | `/api/v1/franchises/{franchiseId}/branches` | Listar sucursales de una franquicia |
| `PATCH` | `/api/v1/branches/{id}/name` | Modificar nombre de sucursal |
| `POST` | `/api/v1/branches/{branchId}/products` | Agregar producto a una sucursal |
| `GET` | `/api/v1/branches/{branchId}/products` | Listar productos de una sucursal |
| `PATCH` | `/api/v1/products/{id}/name` | Modificar nombre de producto |
| `PATCH` | `/api/v1/products/{id}/stock` | Modificar stock de producto |
| `DELETE` | `/api/v1/products/{id}` | Eliminar producto |
| `GET` | `/api/v1/franchises/{id}/max-stock-products` | **Consulta analítica:** Producto con mayor stock por sucursal |

---

## Ejemplos cURL para probar el flujo

Puedes copiar y pegar estos comandos en orden para validar toda la funcionalidad:

### 1. Crear una franquicia
```bash
curl -X POST http://localhost:8080/api/v1/franchises \
  -H "Content-Type: application/json" \
  -d '{"name": "Franquicias Juan Valdez"}'
```

### 2. Crear dos sucursales para esa franquicia
```bash
curl -X POST http://localhost:8080/api/v1/franchises/1/branches \
  -H "Content-Type: application/json" \
  -d '{"name": "Sucursal Aeropuerto El Dorado"}'

curl -X POST http://localhost:8080/api/v1/franchises/1/branches \
  -H "Content-Type: application/json" \
  -d '{"name": "Sucursal Parque de la 93"}'
```

### 3. Agregar productos con stock
```bash
# Productos en Sucursal 1
curl -X POST http://localhost:8080/api/v1/branches/1/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Café Nevado Tradicional", "stock": 40}'

curl -X POST http://localhost:8080/api/v1/branches/1/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Café Grano Huila 500g", "stock": 110}'

# Productos en Sucursal 2
curl -X POST http://localhost:8080/api/v1/branches/2/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Latte Vainilla", "stock": 75}'

curl -X POST http://localhost:8080/api/v1/branches/2/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Croissant Almendras", "stock": 20}'
```

### 4. Consultar el producto con mayor stock por sucursal
Este endpoint resuelve la consulta analítica reactiva. Retorna para cada sucursal de la franquicia cuál es su producto con mayor stock:

```bash
curl -X GET http://localhost:8080/api/v1/franchises/1/max-stock-products
```

Respuesta:
```json
[
  {
    "branchId": 1,
    "branchName": "Sucursal Aeropuerto El Dorado",
    "topProduct": {
      "id": 2,
      "branchId": 1,
      "name": "Café Grano Huila 500g",
      "stock": 110,
      "createdAt": "2026-10-02T18:02:00Z",
      "updatedAt": "2026-10-02T18:02:00Z"
    }
  },
  {
    "branchId": 2,
    "branchName": "Sucursal Parque de la 93",
    "topProduct": {
      "id": 3,
      "branchId": 2,
      "name": "Latte Vainilla",
      "stock": 75,
      "createdAt": "2026-10-02T18:03:00Z",
      "updatedAt": "2026-10-02T18:03:00Z"
    }
  }
]
```

### 5. Actualizaciones de nombre y stock
```bash
# Cambiar nombre de franquicia
curl -X PATCH http://localhost:8080/api/v1/franchises/1/name \
  -H "Content-Type: application/json" \
  -d '{"name": "Juan Valdez Café Colombia"}'

# Cambiar nombre de sucursal
curl -X PATCH http://localhost:8080/api/v1/branches/1/name \
  -H "Content-Type: application/json" \
  -d '{"name": "Sucursal El Dorado T1"}'

# Modificar stock de un producto
curl -X PATCH http://localhost:8080/api/v1/products/1/stock \
  -H "Content-Type: application/json" \
  -d '{"stock": 150}'

# Eliminar un producto
curl -X DELETE http://localhost:8080/api/v1/products/4
```

### 6. Respuestas de error (RFC 7807)
La API devuelve errores en formato Problem Details estándar:

```bash
# Recurso inexistente (404)
curl -X GET http://localhost:8080/api/v1/franchises/999
```
```json
{
  "type": "https://api.accenture.com/errors/not-found",
  "title": "Recurso No Encontrado",
  "status": 404,
  "detail": "No se encontró la franquicia con ID: 999",
  "instance": "/api/v1/franchises/999",
  "timestamp": "2026-10-02T18:15:00Z"
}
```

```bash
# Validación de stock negativo (422)
curl -X PATCH http://localhost:8080/api/v1/products/1/stock \
  -H "Content-Type: application/json" \
  -d '{"stock": -10}'
```

---

## Estructura y decisiones técnicas

```text
com.accenture.service
├── domain               -> Modelos POJO, excepciones y contratos (puertos)
├── application/usecase  -> Lógica de aplicación dividida por caso de uso
└── infrastructure       -> Controladores WebFlux, configuración Spring y adaptadores R2DBC
```

### Puntos clave del diseño:
- **Dominio sin frameworks:** Las clases de `domain/model` son POJOs puros. No tienen anotaciones de base de datos ni de serialización. Si mañana se cambia R2DBC por MongoDB o Redis, el dominio no se toca.
- **Sin `@Service` en casos de uso:** Para evitar acoplar la capa de aplicación con Spring, los casos de uso son clases Java estándar y se registran como beans en `UseCaseConfig.java`.
- **Cero bloqueos en base de datos:** Se utiliza R2DBC en lugar de JDBC/JPA para que la comunicación con PostgreSQL sea reactiva y fluya por el Event Loop de Netty sin bloquear hilos del SO.
- **Cálculo del producto con mayor stock:** En `GetTopProductPerBranchUseCase`, en lugar de hacer un `GROUP BY` complejo en SQL, obtenemos las sucursales y encadenamos con `.flatMap()` y `.reduce()`. Esto procesa las sucursales en paralelo y obtiene el máximo por sucursal en memoria funcionalmente en $O(n)$. Si una sucursal no tiene productos, `.defaultIfEmpty()` la mantiene en la respuesta con valor nulo.

---

## Pruebas unitarias

Para correr la suite de tests:

```bash
./gradlew test
```

Se validan las reglas de negocio y los flujos reactivos usando **JUnit 5**, **Mockito** y **`StepVerifier`** de Project Reactor para verificar los `Mono` y `Flux` de forma determinista (incluyendo casos borde como sucursales vacías, stocks negativos y entidades inexistentes).

---

## Infraestructura (Terraform)

En la carpeta `/terraform` están los archivos para provisionar la arquitectura base en AWS:
- Red con VPC, subnets públicas/privadas en 2 zonas de disponibilidad, IGW y NAT Gateway.
- Instancia administrada RDS PostgreSQL 16 en subnet privada.
- Clúster ECS Fargate con Application Load Balancer y healthcheck apuntando a `/actuator/health`.

Para desplegar:
```bash
cd terraform
terraform init
terraform plan
terraform apply
```
