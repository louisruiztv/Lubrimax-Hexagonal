# msvc-productos

Catálogo de productos y sus presentaciones.

- Puerto: `8011`.
- Ruta principal: `/api/productos`.
- Persistencia: MySQL.

El código está separado en `domain`, `application` e `infrastructure`.

Para arrancarlo desde la raíz del proyecto, configura la conexión en
`src/main/resources/application.properties` del módulo y ejecuta:

```bash
mvn -f msvc-productos/pom.xml spring-boot:run
```
