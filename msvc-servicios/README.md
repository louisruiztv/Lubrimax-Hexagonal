# msvc-servicios

Catálogo de servicios de mantenimiento y tipos de productos requeridos.

- Puerto: `8012`.
- Ruta principal: `/api/servicios`.
- Persistencia: MySQL.

El código está separado en `domain`, `application` e `infrastructure`.

Para arrancarlo desde la raíz del proyecto, configura la conexión en
`src/main/resources/application.properties` del módulo y ejecuta:

```bash
mvn -f msvc-servicios/pom.xml spring-boot:run
```
