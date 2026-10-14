# FleetGo - Taller de Microservicios

Implementación del ejercicio de la Sesión 6 sobre comunicación entre microservicios
con Spring Boot y REST.

## Microservicios

| Microservicio | Puerto | Responsabilidad |
|---|---:|---|
| `ms-vehiculos` | `8081` | Expone el catálogo de vehículos |
| `ms-reservas` | `8082` | Crea y consulta reservas; consume el catálogo por REST |

`ms-reservas` consulta a `ms-vehiculos` mediante `GET /api/vehiculos/{id}`. Los datos
se almacenan en memoria, por lo que se reinician al detener la aplicación.

## Requisitos

- JDK 17
- Maven 3.8 o superior
- IntelliJ IDEA (opcional)
- Postman o una herramienta equivalente para probar la API

## Compilar

Desde la raíz del repositorio, compilar cada microservicio por separado:

```bash
cd ms-vehiculos
mvn clean package

cd ../ms-reservas
mvn clean package
```

La compilación correcta termina con `BUILD SUCCESS`.

## Ejecutar

Abrir dos terminales. En la primera, iniciar el catálogo:

```bash
cd ms-vehiculos
mvn spring-boot:run
```

En la segunda, iniciar reservas:

```bash
cd ms-reservas
mvn spring-boot:run
```

Esperar a que ambas aplicaciones indiquen que Tomcat inició:

- Catálogo: `http://localhost:8081`
- Reservas: `http://localhost:8082`

También se pueden ejecutar las clases `MsVehiculosApplication` y
`MsReservasApplication` desde IntelliJ IDEA.

## Validar el avance

Los validadores son programas Java independientes y no requieren que los servicios
estén ejecutándose. Desde IntelliJ IDEA, ejecutar:

- `ms-vehiculos/src/main/java/com/nttdata/ms_vehiculos/validacion/Validador.java`
- `ms-reservas/src/main/java/com/nttdata/ms_reservas/validacion/Validador.java`

El resultado esperado es:

```text
PUNTAJE ms-vehiculos: 6 / 6
PUNTAJE ms-reservas: 14 / 14
```

Puntaje total: **20 / 20**.

## API del catálogo

### Listar vehículos

```http
GET http://localhost:8081/api/vehiculos
```

### Obtener un vehículo

```http
GET http://localhost:8081/api/vehiculos/1
```

Devuelve `200 OK` para un vehículo existente y `404 Not Found` si no existe.

## API de reservas

### Crear una reserva

```http
POST http://localhost:8082/api/reservas
Content-Type: application/json
```

Body:

```json
{
  "vehiculoId": 1,
  "cliente": "Ana Torres",
  "dias": 3
}
```

Una reserva válida devuelve `201 Created`. Para reservas de 7 días o más se aplica
un descuento del 10%.

### Listar reservas

```http
GET http://localhost:8082/api/reservas
```

### Escenarios de negocio

| Escenario | Respuesta |
|---|---|
| Datos válidos | `201 Created` - `CONFIRMADA` |
| Días menores que 1 o mayores que 30, o cliente vacío | `400 Bad Request` - `DATOS INVALIDOS` |
| Vehículo no disponible | `409 Conflict` - `VEHICULO NO DISPONIBLE` |
| Vehículo ya reservado | `409 Conflict` - `YA RESERVADO` |
| Vehículo inexistente | `404 Not Found` - `VEHICULO NO EXISTE` |
| Catálogo detenido o inaccesible | `503 Service Unavailable` - `CATALOGO NO DISPONIBLE` |

## Colecciones de Postman

Se incluyen colecciones listas para importar:

- `ms-vehiculos/ms-vehiculos.postman_collection.json`
- `ms-reservas/ms-reservas.postman_collection.json`

## Evidencias sugeridas para la entrega

1. Captura de `mvn clean package` con `BUILD SUCCESS`.
2. Captura de los validadores con `6 / 6`, `14 / 14` y el total `20 / 20`.
3. Captura de la aplicación ejecutándose o de una respuesta exitosa de Postman.

## Estructura

```text
fleetgo-base/
├── ms-vehiculos/
│   ├── src/
│   ├── pom.xml
│   └── README.md
├── ms-reservas/
│   ├── src/
│   ├── pom.xml
│   └── README.md
└── README.md
```
