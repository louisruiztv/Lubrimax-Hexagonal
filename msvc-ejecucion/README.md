# msvc-ejecucion

Seguimiento del mantenimiento: tareas, consumos, liquidación y próximo servicio.

- Puerto: `8015`.
- Ruta principal: `/api/ejecuciones`.
- Persistencia: MySQL.

El código está separado en `domain`, `application` e `infrastructure`.

Consulta órdenes y productos. El mantenimiento se gestiona con las acciones de iniciar, finalizar y cancelar.

Para arrancarlo desde la raíz del proyecto, configura la conexión en
`src/main/resources/application.properties` del módulo y ejecuta:

```bash
mvn -f msvc-ejecucion/pom.xml spring-boot:run
```
