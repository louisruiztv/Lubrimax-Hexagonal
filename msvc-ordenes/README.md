# msvc-ordenes

Gestión de órdenes, datos del cliente, propuestas y autorizaciones.

- Puerto: `8013`.
- Ruta principal: `/api/ordenes`.
- Persistencia: MySQL.

El código está separado en `domain`, `application` e `infrastructure`.

Consulta vehículos y servicios. El estado de la orden se sincroniza con su ejecución.

Para arrancarlo desde la raíz del proyecto, configura la conexión en
`src/main/resources/application.properties` del módulo y ejecuta:

```bash
mvn -f msvc-ordenes/pom.xml spring-boot:run
```
