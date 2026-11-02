# msvc-requerimientos

Materiales necesarios para cada orden, cantidades disponibles y reservas.

- Puerto: `8014`.
- Ruta principal: `/api/requerimientos`.
- Persistencia: MySQL.

El código está separado en `domain`, `application` e `infrastructure`.

Consulta órdenes y productos para validar las referencias.

Para arrancarlo desde la raíz del proyecto, configura la conexión en
`src/main/resources/application.properties` del módulo y ejecuta:

```bash
mvn -f msvc-requerimientos/pom.xml spring-boot:run
```
