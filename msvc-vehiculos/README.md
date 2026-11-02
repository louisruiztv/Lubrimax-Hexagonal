# msvc-vehiculos

Registro de vehículos, kilometraje y ficha técnica.

- Puerto: `8010`.
- Ruta principal: `/api/vehiculos`.
- Persistencia: MySQL.

El código está separado en `domain`, `application` e `infrastructure`.

Para arrancarlo desde la raíz del proyecto, configura la conexión en
`src/main/resources/application.properties` del módulo y ejecuta:

```bash
mvn -f msvc-vehiculos/pom.xml spring-boot:run
```
