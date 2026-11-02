-- NUEVA EDICIÓN: UTF-8 / utf8mb4. Importar seleccionando UTF-8.
-- Incluye tildes y eñes reales: á é í ó ú ü ñ Á É Í Ó Ú Ñ.
-- Datos ficticios del archivo del proyecto; NO es una copia de tu BD actual.
-- DESTRUCTIVO: elimina y recrea las seis bases msvc_*. Respaldar antes de importar.
-- Pendientes sin fechas ni tareas avanzadas; órdenes coherentes con ejecución.
-- Orden: PENDIENTE -> AUTORIZADA -> EN_PROCESO -> FINALIZADA; CANCELADA terminal.
-- No quitar SET NAMES utf8mb4. Ejecutar completo en una conexión MySQL 8.

-- LUBRIMAX: recreación completa y datos ficticios de prueba.
-- Basado en los seis microservicios y las entidades JPA inspeccionadas el 2026-10-06.
-- ATENCIÓN: este script ELIMINA y recrea exclusivamente las seis bases msvc_* indicadas.
-- Detener los microservicios antes de ejecutarlo. Usar solo en un entorno de pruebas.
-- MySQL 8.0; ejecutar el archivo completo en una misma conexión (Workbench o cliente mysql).
-- No se encontró un dump ni otro SQL previo: no fue posible conservar su volumen original.
-- 16 tablas: 12 @Entity y 4 tablas @ElementCollection. No se crean tablas auxiliares nuevas.
-- FichaTecnicaEntity es @Embeddable y permanece completa en vehiculo.
-- DatosDelCliente, LiquidacionDeServicio y ProgramacionDeProximoServicio son embebidos.
-- TareaEjecutadaEntity usa inicio/fin, NO fecha_inicio/fecha_fin.
-- EstadoDisponibilidad se persiste como enum STRING de Hibernate/MySQL.
-- Los estados de ejecución/tarea son VARCHAR, validados por sus enums Java.
-- Las órdenes usan PENDIENTE, AUTORIZADA, EN_PROCESO, FINALIZADA y CANCELADA.
-- Los estados de otros catálogos continúan como String; no se añaden CHECK ni tablas.
-- tecnico_id: valores ficticios 1-4. No existe entidad, tabla ni cliente de técnicos
-- en este proyecto; su única regla implementada es que sea positivo.
-- Las referencias entre microservicios son lógicas: no se añaden FK entre bases.
-- Los nombres de FK sin nombre explícito en JPA son descriptivos, no hashes Hibernate.
-- CascadeType.ALL/orphanRemoval operan en JPA; no implican ON DELETE CASCADE en SQL.
-- Las listas con @OrderColumn comienzan en posición 0; @OrderBy ordena por el ID.
-- Las cantidades de consumo expresan envases/unidades de la presentación elegida.
-- Importes: ROUND(SUM(cantidad_real * precio_aplicado), 2) + subtotal_mano_obra.
-- Órdenes 1-6: sin autorización (1-2 sin propuesta; 3-6 con propuesta).
-- Órdenes 7-9: autorizadas sin ejecución; la 7 tiene autorización parcial válida.
-- Órdenes 10-20: ejecuciones 1-11 PENDIENTES, sin fechas, consumos ni importes.
-- Órdenes 21-22: ejecuciones 12-13 EN_PROCESO; órdenes 23-24: 14-15 FINALIZADAS.
-- Se incluyen disponibilidad DISPONIBLE/PARCIAL/FALTANTE y una tarea NO_REALIZADA.
-- DDL provoca commits implícitos en MySQL; la transacción cubre únicamente los INSERT.

SET @lubrimax_sql_mode_anterior = @@SESSION.sql_mode;
SET @lubrimax_fk_anteriores = @@SESSION.foreign_key_checks;
SET SESSION sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
SET SESSION foreign_key_checks = 1;
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

DROP DATABASE IF EXISTS msvc_ejecucion;
DROP DATABASE IF EXISTS msvc_requerimientos;
DROP DATABASE IF EXISTS msvc_ordenes;
DROP DATABASE IF EXISTS msvc_servicios;
DROP DATABASE IF EXISTS msvc_productos;
DROP DATABASE IF EXISTS msvc_vehiculos;
CREATE DATABASE msvc_vehiculos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE msvc_productos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE msvc_servicios CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE msvc_ordenes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE msvc_requerimientos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE msvc_ejecucion CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE msvc_vehiculos;
CREATE TABLE vehiculo (
    vehiculo_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(20) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    anio INT NOT NULL,
    tipo_combustible VARCHAR(50) NOT NULL,
    kilometraje_actual INT NOT NULL,
    estado VARCHAR(50) NOT NULL,
    ficha_marca VARCHAR(100) NOT NULL,
    ficha_modelo VARCHAR(100) NOT NULL,
    ficha_anio INT NOT NULL,
    motor VARCHAR(100) NOT NULL,
    cilindrada VARCHAR(50) NOT NULL,
    ficha_tipo_combustible VARCHAR(50) NOT NULL,
    CONSTRAINT uk_vehiculo_placa UNIQUE (placa)
) ENGINE=InnoDB;


USE msvc_productos;
CREATE TABLE producto (
    producto_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    CONSTRAINT uk_producto_codigo UNIQUE (codigo)
) ENGINE=InnoDB;

CREATE TABLE presentacion_producto (
    presentacion_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(250) NOT NULL,
    contenido DECIMAL(12,3) NOT NULL,
    unidad_medida VARCHAR(30) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    producto_id BIGINT NOT NULL,
    CONSTRAINT fk_presentacion_producto FOREIGN KEY (producto_id) REFERENCES producto (producto_id)
) ENGINE=InnoDB;


USE msvc_servicios;
CREATE TABLE servicio_mantenimiento (
    servicio_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    tipo_servicio VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    estado VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE servicio_tipo_producto_requerido (
    servicio_id BIGINT NOT NULL,
    posicion INT NOT NULL,
    categoria_producto VARCHAR(100) NOT NULL,
    tipo_producto VARCHAR(100) NOT NULL,
    obligatorio BIT(1) NOT NULL,
    PRIMARY KEY (servicio_id, posicion),
    CONSTRAINT fk_tipo_requerido_servicio FOREIGN KEY (servicio_id) REFERENCES servicio_mantenimiento (servicio_id)
) ENGINE=InnoDB;


USE msvc_ordenes;
CREATE TABLE propuesta_tecnica (
    propuesta_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    estado VARCHAR(50) NOT NULL,
    observaciones VARCHAR(1000)
) ENGINE=InnoDB;

CREATE TABLE autorizacion (
    autorizacion_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    medio VARCHAR(100) NOT NULL,
    alcance VARCHAR(500) NOT NULL,
    fecha DATETIME(6) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE orden_servicio (
    orden_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    vehiculo_id BIGINT NOT NULL,
    kilometraje INT NOT NULL,
    mantenimiento_solicitado VARCHAR(500) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha DATETIME(6) NOT NULL,
    cliente_nombre VARCHAR(150) NOT NULL,
    cliente_telefono VARCHAR(30) NOT NULL,
    cliente_documento VARCHAR(30) NOT NULL,
    propuesta_id BIGINT,
    autorizacion_id BIGINT,
    CONSTRAINT uk_orden_propuesta UNIQUE (propuesta_id),
    CONSTRAINT uk_orden_autorizacion UNIQUE (autorizacion_id),
    CONSTRAINT fk_orden_propuesta FOREIGN KEY (propuesta_id) REFERENCES propuesta_tecnica (propuesta_id),
    CONSTRAINT fk_orden_autorizacion FOREIGN KEY (autorizacion_id) REFERENCES autorizacion (autorizacion_id)
) ENGINE=InnoDB;

CREATE TABLE orden_servicios (
    orden_id BIGINT NOT NULL,
    posicion INT NOT NULL,
    servicio_id BIGINT NOT NULL,
    PRIMARY KEY (orden_id, posicion),
    CONSTRAINT fk_orden_servicios_propietario FOREIGN KEY (orden_id) REFERENCES orden_servicio (orden_id)
) ENGINE=InnoDB;

CREATE TABLE propuesta_servicios (
    propuesta_id BIGINT NOT NULL,
    posicion INT NOT NULL,
    servicio_id BIGINT NOT NULL,
    PRIMARY KEY (propuesta_id, posicion),
    CONSTRAINT fk_propuesta_servicios_propietario FOREIGN KEY (propuesta_id) REFERENCES propuesta_tecnica (propuesta_id)
) ENGINE=InnoDB;

CREATE TABLE autorizacion_servicios (
    autorizacion_id BIGINT NOT NULL,
    posicion INT NOT NULL,
    servicio_id BIGINT NOT NULL,
    PRIMARY KEY (autorizacion_id, posicion),
    CONSTRAINT fk_autorizacion_servicios_propietario FOREIGN KEY (autorizacion_id) REFERENCES autorizacion (autorizacion_id)
) ENGINE=InnoDB;


USE msvc_requerimientos;
CREATE TABLE requerimiento_mantenimiento (
    requerimiento_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orden_id BIGINT NOT NULL,
    estado VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE detalle_requerimiento (
    detalle_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    presentacion_id BIGINT NOT NULL,
    cantidad_requerida DECIMAL(12,3) NOT NULL,
    cantidad_disponible DECIMAL(12,3) NOT NULL,
    estado_disponibilidad ENUM('DISPONIBLE','FALTANTE','PARCIAL') NOT NULL,
    reservado BIT(1) NOT NULL,
    observacion VARCHAR(1000),
    requerimiento_id BIGINT NOT NULL,
    CONSTRAINT fk_detalle_requerimiento FOREIGN KEY (requerimiento_id) REFERENCES requerimiento_mantenimiento (requerimiento_id)
) ENGINE=InnoDB;


USE msvc_ejecucion;
CREATE TABLE ejecucion_mantenimiento (
    ejecucion_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orden_id BIGINT NOT NULL,
    tecnico_id BIGINT NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_inicio DATETIME(6),
    fecha_fin DATETIME(6),
    subtotal_productos DECIMAL(19,2) NOT NULL,
    subtotal_mano_obra DECIMAL(19,2) NOT NULL,
    total DECIMAL(19,2) NOT NULL,
    fecha_sugerida DATE,
    kilometraje_sugerido INT,
    CONSTRAINT uk_ejecucion_orden UNIQUE (orden_id)
) ENGINE=InnoDB;

CREATE TABLE tarea_ejecutada (
    tarea_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tipo_tarea VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    inicio DATETIME(6),
    fin DATETIME(6),
    ejecucion_id BIGINT NOT NULL,
    CONSTRAINT fk_tarea_ejecucion FOREIGN KEY (ejecucion_id) REFERENCES ejecucion_mantenimiento (ejecucion_id)
) ENGINE=InnoDB;

CREATE TABLE consumo_real (
    consumo_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    presentacion_id BIGINT NOT NULL,
    cantidad_real DECIMAL(12,3) NOT NULL,
    precio_aplicado DECIMAL(12,2) NOT NULL,
    ejecucion_id BIGINT NOT NULL,
    CONSTRAINT fk_consumo_ejecucion FOREIGN KEY (ejecucion_id) REFERENCES ejecucion_mantenimiento (ejecucion_id)
) ENGINE=InnoDB;

-- Carga en orden de dependencias, con las FK activas.
START TRANSACTION;

USE msvc_vehiculos;
-- vehiculo: 24 registros.
INSERT INTO vehiculo (vehiculo_id, placa, marca, modelo, anio, tipo_combustible, kilometraje_actual, estado, ficha_marca, ficha_modelo, ficha_anio, motor, cilindrada, ficha_tipo_combustible) VALUES
    (1, 'L01-101', 'Toyota', 'Corolla', 2020, 'Gasolina', 32500, 'ACTIVO', 'Toyota', 'Corolla', 2020, '1.8 Dual VVT-i', '1798 cc', 'Gasolina'),
    (2, 'L02-102', 'Kia', 'Rio', 2021, 'Gasolina', 35000, 'ACTIVO', 'Kia', 'Rio', 2021, '1.4 MPI', '1368 cc', 'Gasolina'),
    (3, 'L03-103', 'Hyundai', 'Accent', 2019, 'Gasolina', 37500, 'ACTIVO', 'Hyundai', 'Accent', 2019, '1.4 MPI', '1368 cc', 'Gasolina'),
    (4, 'L04-104', 'Nissan', 'Versa', 2022, 'Gasolina', 40000, 'ACTIVO', 'Nissan', 'Versa', 2022, '1.6 DOHC', '1598 cc', 'Gasolina'),
    (5, 'L05-105', 'Chevrolet', 'Onix', 2023, 'Gasolina', 42500, 'ACTIVO', 'Chevrolet', 'Onix', 2023, '1.0 Turbo', '999 cc', 'Gasolina'),
    (6, 'L06-106', 'Suzuki', 'Swift', 2021, 'Gasolina', 45000, 'ACTIVO', 'Suzuki', 'Swift', 2021, '1.2 VVT', '1197 cc', 'Gasolina'),
    (7, 'L07-107', 'Toyota', 'Yaris', 2018, 'Gasolina', 47500, 'ACTIVO', 'Toyota', 'Yaris', 2018, '1.3 VVT-i', '1329 cc', 'Gasolina'),
    (8, 'L08-108', 'Mazda', '3', 2020, 'Gasolina', 50000, 'ACTIVO', 'Mazda', '3', 2020, '2.0 Skyactiv-G', '1998 cc', 'Gasolina'),
    (9, 'L09-109', 'Toyota', 'Corolla', 2020, 'Gasolina', 52500, 'ACTIVO', 'Toyota', 'Corolla', 2020, '1.8 Dual VVT-i', '1798 cc', 'Gasolina'),
    (10, 'L10-110', 'Kia', 'Rio', 2021, 'Gasolina', 55000, 'ACTIVO', 'Kia', 'Rio', 2021, '1.4 MPI', '1368 cc', 'Gasolina'),
    (11, 'L11-111', 'Hyundai', 'Accent', 2019, 'Gasolina', 57500, 'ACTIVO', 'Hyundai', 'Accent', 2019, '1.4 MPI', '1368 cc', 'Gasolina'),
    (12, 'L12-112', 'Nissan', 'Versa', 2022, 'Gasolina', 60000, 'ACTIVO', 'Nissan', 'Versa', 2022, '1.6 DOHC', '1598 cc', 'Gasolina'),
    (13, 'L13-113', 'Chevrolet', 'Onix', 2023, 'Gasolina', 62500, 'ACTIVO', 'Chevrolet', 'Onix', 2023, '1.0 Turbo', '999 cc', 'Gasolina'),
    (14, 'L14-114', 'Suzuki', 'Swift', 2021, 'Gasolina', 65000, 'ACTIVO', 'Suzuki', 'Swift', 2021, '1.2 VVT', '1197 cc', 'Gasolina'),
    (15, 'L15-115', 'Toyota', 'Yaris', 2018, 'Gasolina', 67500, 'ACTIVO', 'Toyota', 'Yaris', 2018, '1.3 VVT-i', '1329 cc', 'Gasolina'),
    (16, 'L16-116', 'Mazda', '3', 2020, 'Gasolina', 70000, 'ACTIVO', 'Mazda', '3', 2020, '2.0 Skyactiv-G', '1998 cc', 'Gasolina'),
    (17, 'L17-117', 'Toyota', 'Corolla', 2020, 'Gasolina', 72500, 'ACTIVO', 'Toyota', 'Corolla', 2020, '1.8 Dual VVT-i', '1798 cc', 'Gasolina'),
    (18, 'L18-118', 'Kia', 'Rio', 2021, 'Gasolina', 75000, 'ACTIVO', 'Kia', 'Rio', 2021, '1.4 MPI', '1368 cc', 'Gasolina'),
    (19, 'L19-119', 'Hyundai', 'Accent', 2019, 'Gasolina', 77500, 'ACTIVO', 'Hyundai', 'Accent', 2019, '1.4 MPI', '1368 cc', 'Gasolina'),
    (20, 'L20-120', 'Nissan', 'Versa', 2022, 'Gasolina', 80000, 'ACTIVO', 'Nissan', 'Versa', 2022, '1.6 DOHC', '1598 cc', 'Gasolina'),
    (21, 'L21-121', 'Chevrolet', 'Onix', 2023, 'Gasolina', 82500, 'ACTIVO', 'Chevrolet', 'Onix', 2023, '1.0 Turbo', '999 cc', 'Gasolina'),
    (22, 'L22-122', 'Suzuki', 'Swift', 2021, 'Gasolina', 85000, 'ACTIVO', 'Suzuki', 'Swift', 2021, '1.2 VVT', '1197 cc', 'Gasolina'),
    (23, 'L23-123', 'Toyota', 'Yaris', 2018, 'Gasolina', 87500, 'ACTIVO', 'Toyota', 'Yaris', 2018, '1.3 VVT-i', '1329 cc', 'Gasolina'),
    (24, 'L24-124', 'Mazda', '3', 2020, 'Gasolina', 90000, 'ACTIVO', 'Mazda', '3', 2020, '2.0 Skyactiv-G', '1998 cc', 'Gasolina');


USE msvc_productos;
-- producto: 12 registros.
INSERT INTO producto (producto_id, codigo, nombre, descripcion, marca, categoria, tipo, estado) VALUES
    (1, 'ACE-5W30', 'Aceite sintético 5W-30', 'Aceite sintético 5W-30; referencia ficticia para pruebas de mantenimiento.', 'Lubrimax', 'LUBRICANTE', 'ACEITE_MOTOR', 'ACTIVO'),
    (2, 'ACE-10W40', 'Aceite semisintético 10W-40', 'Aceite semisintético 10W-40; referencia ficticia para pruebas de mantenimiento.', 'Lubrimax', 'LUBRICANTE', 'ACEITE_MOTOR', 'ACTIVO'),
    (3, 'FIL-ACE', 'Filtro de aceite', 'Filtro de aceite; referencia ficticia para pruebas de mantenimiento.', 'Filtra', 'FILTRO', 'FILTRO_ACEITE', 'ACTIVO'),
    (4, 'FIL-AIR', 'Filtro de aire', 'Filtro de aire; referencia ficticia para pruebas de mantenimiento.', 'Filtra', 'FILTRO', 'FILTRO_AIRE', 'ACTIVO'),
    (5, 'FIL-CAB', 'Filtro de cabina', 'Filtro de cabina; referencia ficticia para pruebas de mantenimiento.', 'Filtra', 'FILTRO', 'FILTRO_CABINA', 'ACTIVO'),
    (6, 'LIQ-DOT4', 'Líquido de frenos DOT 4', 'Líquido de frenos DOT 4; referencia ficticia para pruebas de mantenimiento.', 'Frena', 'FLUIDO', 'LIQUIDO_FRENOS', 'ACTIVO'),
    (7, 'REF-50', 'Refrigerante premezclado 50/50', 'Refrigerante premezclado 50/50; referencia ficticia para pruebas de mantenimiento.', 'Termo', 'FLUIDO', 'REFRIGERANTE', 'ACTIVO'),
    (8, 'LIM-FRE', 'Limpiador de frenos', 'Limpiador de frenos; referencia ficticia para pruebas de mantenimiento.', 'Limpia', 'LIMPIEZA', 'LIMPIADOR_FRENOS', 'ACTIVO'),
    (9, 'ACE-75W90', 'Aceite de transmisión 75W-90', 'Aceite de transmisión 75W-90; referencia ficticia para pruebas de mantenimiento.', 'Lubrimax', 'LUBRICANTE', 'ACEITE_TRANSMISION', 'ACTIVO'),
    (10, 'GRA-LIT', 'Grasa de litio', 'Grasa de litio; referencia ficticia para pruebas de mantenimiento.', 'Lubrimax', 'LUBRICANTE', 'GRASA', 'ACTIVO'),
    (11, 'LIQ-PAR', 'Líquido limpiaparabrisas', 'Líquido limpiaparabrisas; referencia ficticia para pruebas de mantenimiento.', 'Limpia', 'LIMPIEZA', 'LIMPIAPARABRISAS', 'ACTIVO'),
    (12, 'FIL-COM', 'Filtro de combustible', 'Filtro de combustible; referencia ficticia para pruebas de mantenimiento.', 'Filtra', 'FILTRO', 'FILTRO_COMBUSTIBLE', 'ACTIVO');

-- presentacion_producto: 18 registros.
INSERT INTO presentacion_producto (presentacion_id, descripcion, contenido, unidad_medida, estado, producto_id) VALUES
    (1, 'Botella de 1 litro', 1, 'L', 'ACTIVO', 1),
    (2, 'Envase de 4 litros', 4, 'L', 'ACTIVO', 1),
    (3, 'Botella de 1 litro', 1, 'L', 'ACTIVO', 2),
    (4, 'Envase de 4 litros', 4, 'L', 'ACTIVO', 2),
    (5, 'Filtro individual', 1, 'UNIDAD', 'ACTIVO', 3),
    (6, 'Filtro individual', 1, 'UNIDAD', 'ACTIVO', 4),
    (7, 'Filtro individual', 1, 'UNIDAD', 'ACTIVO', 5),
    (8, 'Botella de 500 ml', 0.5, 'L', 'ACTIVO', 6),
    (9, 'Botella de 1 litro', 1, 'L', 'ACTIVO', 6),
    (10, 'Envase de 1 litro', 1, 'L', 'ACTIVO', 7),
    (11, 'Envase de 4 litros', 4, 'L', 'ACTIVO', 7),
    (12, 'Aerosol de 500 ml', 0.5, 'L', 'ACTIVO', 8),
    (13, 'Botella de 1 litro', 1, 'L', 'ACTIVO', 9),
    (14, 'Envase de 4 litros', 4, 'L', 'ACTIVO', 9),
    (15, 'Cartucho de 400 g', 0.4, 'KG', 'ACTIVO', 10),
    (16, 'Botella de 1 litro', 1, 'L', 'ACTIVO', 11),
    (17, 'Envase de 4 litros', 4, 'L', 'ACTIVO', 11),
    (18, 'Filtro individual', 1, 'UNIDAD', 'ACTIVO', 12);


USE msvc_servicios;
-- servicio_mantenimiento: 8 registros.
INSERT INTO servicio_mantenimiento (servicio_id, nombre, tipo_servicio, descripcion, estado) VALUES
    (1, 'Cambio de aceite y filtro', 'LUBRICACION', 'Cambio de aceite y filtro con revisión visual de componentes.', 'ACTIVO'),
    (2, 'Cambio de filtro de aire', 'FILTRACION', 'Cambio de filtro de aire con revisión visual de componentes.', 'ACTIVO'),
    (3, 'Cambio de filtro de cabina', 'FILTRACION', 'Cambio de filtro de cabina con revisión visual de componentes.', 'ACTIVO'),
    (4, 'Servicio de líquido de frenos', 'FRENOS', 'Servicio de líquido de frenos con revisión visual de componentes.', 'ACTIVO'),
    (5, 'Cambio de refrigerante', 'REFRIGERACION', 'Cambio de refrigerante con revisión visual de componentes.', 'ACTIVO'),
    (6, 'Cambio de aceite de transmisión', 'TRANSMISION', 'Cambio de aceite de transmisión con revisión visual de componentes.', 'ACTIVO'),
    (7, 'Lubricación y revisión general', 'PREVENTIVO', 'Lubricación y revisión general con revisión visual de componentes.', 'ACTIVO'),
    (8, 'Cambio de filtro de combustible', 'FILTRACION', 'Cambio de filtro de combustible con revisión visual de componentes.', 'ACTIVO');

-- servicio_tipo_producto_requerido: 11 registros.
INSERT INTO servicio_tipo_producto_requerido (servicio_id, posicion, categoria_producto, tipo_producto, obligatorio) VALUES
    (1, 0, 'LUBRICANTE', 'ACEITE_MOTOR', b'1'),
    (1, 1, 'FILTRO', 'FILTRO_ACEITE', b'1'),
    (2, 0, 'FILTRO', 'FILTRO_AIRE', b'1'),
    (3, 0, 'FILTRO', 'FILTRO_CABINA', b'1'),
    (4, 0, 'FLUIDO', 'LIQUIDO_FRENOS', b'1'),
    (4, 1, 'LIMPIEZA', 'LIMPIADOR_FRENOS', b'0'),
    (5, 0, 'FLUIDO', 'REFRIGERANTE', b'1'),
    (6, 0, 'LUBRICANTE', 'ACEITE_TRANSMISION', b'1'),
    (7, 0, 'LUBRICANTE', 'GRASA', b'1'),
    (7, 1, 'LIMPIEZA', 'LIMPIAPARABRISAS', b'0'),
    (8, 0, 'FILTRO', 'FILTRO_COMBUSTIBLE', b'1');


USE msvc_ordenes;
-- propuesta_tecnica: 22 registros.
INSERT INTO propuesta_tecnica (propuesta_id, estado, observaciones) VALUES
    (3, 'PENDIENTE', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (4, 'PENDIENTE', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (5, 'PENDIENTE', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (6, 'PENDIENTE', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (7, 'ACEPTADA', 'Cliente autoriza solo aceite y filtro; pospone filtros de aire y cabina.'),
    (8, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (9, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (10, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (11, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (12, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (13, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (14, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (15, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (16, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (17, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (18, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (19, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (20, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (21, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (22, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (23, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.'),
    (24, 'ACEPTADA', 'Propuesta de mantenimiento según inspección del vehículo.');

-- autorizacion: 18 registros.
INSERT INTO autorizacion (autorizacion_id, medio, alcance, fecha) VALUES
    (7, 'TELEFONO', 'Autoriza únicamente el cambio de aceite y filtro.', '2026-09-08 08:30:00'),
    (8, 'WHATSAPP', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-09 08:30:00'),
    (9, 'PRESENCIAL', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-10 08:30:00'),
    (10, 'TELEFONO', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-11 08:30:00'),
    (11, 'WHATSAPP', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-12 08:30:00'),
    (12, 'PRESENCIAL', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-13 08:30:00'),
    (13, 'TELEFONO', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-14 08:30:00'),
    (14, 'WHATSAPP', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-15 08:30:00'),
    (15, 'PRESENCIAL', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-16 08:30:00'),
    (16, 'TELEFONO', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-17 08:30:00'),
    (17, 'WHATSAPP', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-18 08:30:00'),
    (18, 'PRESENCIAL', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-19 08:30:00'),
    (19, 'TELEFONO', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-20 08:30:00'),
    (20, 'WHATSAPP', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-21 08:30:00'),
    (21, 'PRESENCIAL', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-22 08:30:00'),
    (22, 'TELEFONO', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-23 08:30:00'),
    (23, 'WHATSAPP', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-24 08:30:00'),
    (24, 'PRESENCIAL', 'Autoriza los servicios enumerados en esta autorización y su propuesta técnica.', '2026-09-25 08:30:00');

-- orden_servicio: 24 registros.
INSERT INTO orden_servicio (orden_id, vehiculo_id, kilometraje, mantenimiento_solicitado, estado, fecha, cliente_nombre, cliente_telefono, cliente_documento, propuesta_id, autorizacion_id) VALUES
    (1, 1, 32500, 'Cambio de aceite y filtro; Cambio de filtro de aire', 'PENDIENTE', '2026-09-02 08:00:00', 'Ana Torres', '900000001', '70000001', NULL, NULL),
    (2, 2, 35000, 'Cambio de aceite y filtro; Cambio de filtro de cabina', 'PENDIENTE', '2026-09-03 08:00:00', 'Luis Rojas', '900000002', '70000002', NULL, NULL),
    (3, 3, 37500, 'Servicio de líquido de frenos', 'PENDIENTE', '2026-09-04 08:00:00', 'María Quispe', '900000003', '70000003', 3, NULL),
    (4, 4, 40000, 'Cambio de refrigerante', 'PENDIENTE', '2026-09-05 08:00:00', 'Carlos Medina', '900000004', '70000004', 4, NULL),
    (5, 5, 42500, 'Cambio de aceite de transmisión', 'PENDIENTE', '2026-09-06 08:00:00', 'Lucía Vargas', '900000005', '70000005', 5, NULL),
    (6, 6, 45000, 'Lubricación y revisión general', 'PENDIENTE', '2026-09-07 08:00:00', 'Jorge Salas', '900000006', '70000006', 6, NULL),
    (7, 7, 47500, 'Cambio de aceite y filtro; Cambio de filtro de aire; Cambio de filtro de cabina', 'AUTORIZADA', '2026-09-08 08:00:00', 'Rosa Chávez', '900000007', '70000007', 7, 7),
    (8, 8, 50000, 'Cambio de aceite y filtro; Cambio de filtro de aire; Cambio de filtro de cabina', 'AUTORIZADA', '2026-09-09 08:00:00', 'Pedro Flores', '900000008', '70000008', 8, 8),
    (9, 9, 52500, 'Cambio de aceite y filtro; Cambio de filtro de aire', 'AUTORIZADA', '2026-09-10 08:00:00', 'Ana Torres', '900000001', '70000001', 9, 9),
    (10, 10, 55000, 'Cambio de aceite y filtro; Cambio de filtro de cabina', 'AUTORIZADA', '2026-09-11 08:00:00', 'Luis Rojas', '900000002', '70000002', 10, 10),
    (11, 11, 57500, 'Servicio de líquido de frenos', 'AUTORIZADA', '2026-09-12 08:00:00', 'María Quispe', '900000003', '70000003', 11, 11),
    (12, 12, 60000, 'Cambio de refrigerante', 'AUTORIZADA', '2026-09-13 08:00:00', 'Carlos Medina', '900000004', '70000004', 12, 12),
    (13, 13, 62500, 'Cambio de aceite de transmisión', 'AUTORIZADA', '2026-09-14 08:00:00', 'Lucía Vargas', '900000005', '70000005', 13, 13),
    (14, 14, 65000, 'Lubricación y revisión general', 'AUTORIZADA', '2026-09-15 08:00:00', 'Jorge Salas', '900000006', '70000006', 14, 14),
    (15, 15, 67500, 'Cambio de filtro de combustible', 'AUTORIZADA', '2026-09-16 08:00:00', 'Rosa Chávez', '900000007', '70000007', 15, 15),
    (16, 16, 70000, 'Cambio de aceite y filtro; Cambio de filtro de aire; Cambio de filtro de cabina', 'AUTORIZADA', '2026-09-17 08:00:00', 'Pedro Flores', '900000008', '70000008', 16, 16),
    (17, 17, 72500, 'Cambio de aceite y filtro; Cambio de filtro de aire', 'AUTORIZADA', '2026-09-18 08:00:00', 'Ana Torres', '900000001', '70000001', 17, 17),
    (18, 18, 75000, 'Cambio de aceite y filtro; Cambio de filtro de cabina', 'AUTORIZADA', '2026-09-19 08:00:00', 'Luis Rojas', '900000002', '70000002', 18, 18),
    (19, 19, 77500, 'Servicio de líquido de frenos', 'AUTORIZADA', '2026-09-20 08:00:00', 'María Quispe', '900000003', '70000003', 19, 19),
    (20, 20, 80000, 'Cambio de refrigerante', 'AUTORIZADA', '2026-09-21 08:00:00', 'Carlos Medina', '900000004', '70000004', 20, 20),
    (21, 21, 82500, 'Cambio de aceite de transmisión', 'EN_PROCESO', '2026-09-22 08:00:00', 'Lucía Vargas', '900000005', '70000005', 21, 21),
    (22, 22, 85000, 'Lubricación y revisión general', 'EN_PROCESO', '2026-09-23 08:00:00', 'Jorge Salas', '900000006', '70000006', 22, 22),
    (23, 23, 87500, 'Cambio de filtro de combustible', 'FINALIZADA', '2026-09-24 08:00:00', 'Rosa Chávez', '900000007', '70000007', 23, 23),
    (24, 24, 90000, 'Cambio de aceite y filtro; Cambio de filtro de aire; Cambio de filtro de cabina', 'FINALIZADA', '2026-09-25 08:00:00', 'Pedro Flores', '900000008', '70000008', 24, 24);

-- orden_servicios: 38 registros.
INSERT INTO orden_servicios (orden_id, posicion, servicio_id) VALUES
    (1, 0, 1),
    (1, 1, 2),
    (2, 0, 1),
    (2, 1, 3),
    (3, 0, 4),
    (4, 0, 5),
    (5, 0, 6),
    (6, 0, 7),
    (7, 0, 1),
    (7, 1, 2),
    (7, 2, 3),
    (8, 0, 1),
    (8, 1, 2),
    (8, 2, 3),
    (9, 0, 1),
    (9, 1, 2),
    (10, 0, 1),
    (10, 1, 3),
    (11, 0, 4),
    (12, 0, 5),
    (13, 0, 6),
    (14, 0, 7),
    (15, 0, 8),
    (16, 0, 1),
    (16, 1, 2),
    (16, 2, 3),
    (17, 0, 1),
    (17, 1, 2),
    (18, 0, 1),
    (18, 1, 3),
    (19, 0, 4),
    (20, 0, 5),
    (21, 0, 6),
    (22, 0, 7),
    (23, 0, 8),
    (24, 0, 1),
    (24, 1, 2),
    (24, 2, 3);

-- propuesta_servicios: 34 registros.
INSERT INTO propuesta_servicios (propuesta_id, posicion, servicio_id) VALUES
    (3, 0, 4),
    (4, 0, 5),
    (5, 0, 6),
    (6, 0, 7),
    (7, 0, 1),
    (7, 1, 2),
    (7, 2, 3),
    (8, 0, 1),
    (8, 1, 2),
    (8, 2, 3),
    (9, 0, 1),
    (9, 1, 2),
    (10, 0, 1),
    (10, 1, 3),
    (11, 0, 4),
    (12, 0, 5),
    (13, 0, 6),
    (14, 0, 7),
    (15, 0, 8),
    (16, 0, 1),
    (16, 1, 2),
    (16, 2, 3),
    (17, 0, 1),
    (17, 1, 2),
    (18, 0, 1),
    (18, 1, 3),
    (19, 0, 4),
    (20, 0, 5),
    (21, 0, 6),
    (22, 0, 7),
    (23, 0, 8),
    (24, 0, 1),
    (24, 1, 2),
    (24, 2, 3);

-- autorizacion_servicios: 28 registros.
INSERT INTO autorizacion_servicios (autorizacion_id, posicion, servicio_id) VALUES
    (7, 0, 1),
    (8, 0, 1),
    (8, 1, 2),
    (8, 2, 3),
    (9, 0, 1),
    (9, 1, 2),
    (10, 0, 1),
    (10, 1, 3),
    (11, 0, 4),
    (12, 0, 5),
    (13, 0, 6),
    (14, 0, 7),
    (15, 0, 8),
    (16, 0, 1),
    (16, 1, 2),
    (16, 2, 3),
    (17, 0, 1),
    (17, 1, 2),
    (18, 0, 1),
    (18, 1, 3),
    (19, 0, 4),
    (20, 0, 5),
    (21, 0, 6),
    (22, 0, 7),
    (23, 0, 8),
    (24, 0, 1),
    (24, 1, 2),
    (24, 2, 3);


USE msvc_requerimientos;
-- requerimiento_mantenimiento: 20 registros.
INSERT INTO requerimiento_mantenimiento (requerimiento_id, orden_id, estado) VALUES
    (1, 5, 'PENDIENTE'),
    (2, 6, 'PENDIENTE'),
    (3, 7, 'PENDIENTE'),
    (4, 8, 'PENDIENTE'),
    (5, 9, 'PENDIENTE'),
    (6, 10, 'PENDIENTE'),
    (7, 11, 'PENDIENTE'),
    (8, 12, 'PENDIENTE'),
    (9, 13, 'PENDIENTE'),
    (10, 14, 'PENDIENTE'),
    (11, 15, 'PENDIENTE'),
    (12, 16, 'PENDIENTE'),
    (13, 17, 'PENDIENTE'),
    (14, 18, 'PENDIENTE'),
    (15, 19, 'PENDIENTE'),
    (16, 20, 'PENDIENTE'),
    (17, 21, 'ATENDIDO'),
    (18, 22, 'ATENDIDO'),
    (19, 23, 'ATENDIDO'),
    (20, 24, 'ATENDIDO');

-- detalle_requerimiento: 43 registros.
INSERT INTO detalle_requerimiento (detalle_id, producto_id, presentacion_id, cantidad_requerida, cantidad_disponible, estado_disponibilidad, reservado, observacion, requerimiento_id) VALUES
    (1, 9, 13, 2, 0, 'FALTANTE', b'0', 'Pendiente de reposición.', 1),
    (2, 10, 15, 0.5, 0.25, 'PARCIAL', b'0', 'Disponibilidad insuficiente; aún no se reserva.', 2),
    (3, 11, 16, 1, 3, 'DISPONIBLE', b'0', 'Material disponible para la atención.', 2),
    (4, 1, 2, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 3),
    (5, 3, 5, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 3),
    (6, 1, 2, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 4),
    (7, 3, 5, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 4),
    (8, 4, 6, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 4),
    (9, 5, 7, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 4),
    (10, 1, 2, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 5),
    (11, 3, 5, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 5),
    (12, 4, 6, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 5),
    (13, 1, 2, 1, 0, 'FALTANTE', b'0', 'Pendiente de reposición.', 6),
    (14, 3, 5, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 6),
    (15, 5, 7, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 6),
    (16, 6, 9, 1, 0.5, 'PARCIAL', b'0', 'Disponibilidad insuficiente; aún no se reserva.', 7),
    (17, 8, 12, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 7),
    (18, 7, 11, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 8),
    (19, 9, 13, 2, 6, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 9),
    (20, 10, 15, 0.5, 1.5, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 10),
    (21, 11, 16, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 10),
    (22, 12, 18, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 11),
    (23, 1, 2, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 12),
    (24, 3, 5, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 12),
    (25, 4, 6, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 12),
    (26, 5, 7, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 12),
    (27, 1, 2, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 13),
    (28, 3, 5, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 13),
    (29, 4, 6, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 13),
    (30, 1, 2, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 14),
    (31, 3, 5, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 14),
    (32, 5, 7, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 14),
    (33, 6, 9, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 15),
    (34, 8, 12, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 15),
    (35, 7, 11, 1, 3, 'DISPONIBLE', b'1', 'Material disponible para la atención.', 16),
    (36, 9, 13, 2, 6, 'DISPONIBLE', b'0', 'Material disponible para la atención.', 17),
    (37, 10, 15, 0.5, 1.5, 'DISPONIBLE', b'0', 'Material disponible para la atención.', 18),
    (38, 11, 16, 1, 3, 'DISPONIBLE', b'0', 'Material disponible para la atención.', 18),
    (39, 12, 18, 1, 3, 'DISPONIBLE', b'0', 'Material disponible para la atención.', 19),
    (40, 1, 2, 1, 3, 'DISPONIBLE', b'0', 'Material disponible para la atención.', 20),
    (41, 3, 5, 1, 3, 'DISPONIBLE', b'0', 'Material disponible para la atención.', 20),
    (42, 4, 6, 1, 3, 'DISPONIBLE', b'0', 'Material disponible para la atención.', 20),
    (43, 5, 7, 1, 3, 'DISPONIBLE', b'0', 'Material disponible para la atención.', 20);


USE msvc_ejecucion;
-- ejecucion_mantenimiento: 15 registros.
INSERT INTO ejecucion_mantenimiento (ejecucion_id, orden_id, tecnico_id, estado, fecha_inicio, fecha_fin, subtotal_productos, subtotal_mano_obra, total, fecha_sugerida, kilometraje_sugerido) VALUES
    (1, 10, 1, 'PENDIENTE', NULL, NULL, 0.00, 0, 0.00, NULL, NULL),
    (2, 11, 2, 'PENDIENTE', NULL, NULL, 0.00, 0, 0.00, NULL, NULL),
    (3, 12, 3, 'PENDIENTE', NULL, NULL, 0.00, 0, 0.00, NULL, NULL),
    (4, 13, 4, 'PENDIENTE', NULL, NULL, 0.00, 0, 0.00, NULL, NULL),
    (5, 14, 1, 'PENDIENTE', NULL, NULL, 0.00, 0, 0.00, NULL, NULL),
    (6, 15, 2, 'PENDIENTE', NULL, NULL, 0.00, 0, 0.00, NULL, NULL),
    (7, 16, 3, 'PENDIENTE', NULL, NULL, 0.00, 0, 0.00, NULL, NULL),
    (8, 17, 4, 'PENDIENTE', NULL, NULL, 0.00, 0, 0.00, NULL, NULL),
    (9, 18, 1, 'PENDIENTE', NULL, NULL, 0.00, 0, 0.00, NULL, NULL),
    (10, 19, 2, 'PENDIENTE', NULL, NULL, 0.00, 0, 0.00, NULL, NULL),
    (11, 20, 3, 'PENDIENTE', NULL, NULL, 0.00, 0, 0.00, NULL, NULL),
    (12, 21, 4, 'EN_PROCESO', '2026-09-22 09:00:00', NULL, 110.00, 40.00, 150.00, NULL, NULL),
    (13, 22, 1, 'EN_PROCESO', '2026-09-23 09:00:00', NULL, 24.00, 40.00, 64.00, NULL, NULL),
    (14, 23, 2, 'FINALIZADA', '2026-09-24 09:00:00', '2026-09-24 11:00:00', 48.00, 80.00, 128.00, '2027-03-23', 92500),
    (15, 24, 3, 'FINALIZADA', '2026-09-25 09:00:00', '2026-09-25 11:00:00', 233.00, 80.00, 313.00, '2027-03-24', 95000);

-- tarea_ejecutada: 53 registros.
INSERT INTO tarea_ejecutada (tarea_id, tipo_tarea, estado, inicio, fin, ejecucion_id) VALUES
    (1, 'Inspección inicial', 'PENDIENTE', NULL, NULL, 1),
    (2, 'Cambio de aceite y filtro', 'PENDIENTE', NULL, NULL, 1),
    (3, 'Cambio de filtro de cabina', 'PENDIENTE', NULL, NULL, 1),
    (4, 'Control final', 'PENDIENTE', NULL, NULL, 1),
    (5, 'Inspección inicial', 'PENDIENTE', NULL, NULL, 2),
    (6, 'Servicio de líquido de frenos', 'PENDIENTE', NULL, NULL, 2),
    (7, 'Control final', 'PENDIENTE', NULL, NULL, 2),
    (8, 'Inspección inicial', 'PENDIENTE', NULL, NULL, 3),
    (9, 'Cambio de refrigerante', 'PENDIENTE', NULL, NULL, 3),
    (10, 'Control final', 'PENDIENTE', NULL, NULL, 3),
    (11, 'Inspección inicial', 'PENDIENTE', NULL, NULL, 4),
    (12, 'Cambio de aceite de transmisión', 'PENDIENTE', NULL, NULL, 4),
    (13, 'Control final', 'PENDIENTE', NULL, NULL, 4),
    (14, 'Inspección inicial', 'PENDIENTE', NULL, NULL, 5),
    (15, 'Lubricación y revisión general', 'PENDIENTE', NULL, NULL, 5),
    (16, 'Control final', 'PENDIENTE', NULL, NULL, 5),
    (17, 'Inspección inicial', 'PENDIENTE', NULL, NULL, 6),
    (18, 'Cambio de filtro de combustible', 'PENDIENTE', NULL, NULL, 6),
    (19, 'Control final', 'PENDIENTE', NULL, NULL, 6),
    (20, 'Inspección inicial', 'PENDIENTE', NULL, NULL, 7),
    (21, 'Cambio de aceite y filtro', 'PENDIENTE', NULL, NULL, 7),
    (22, 'Cambio de filtro de aire', 'PENDIENTE', NULL, NULL, 7),
    (23, 'Cambio de filtro de cabina', 'PENDIENTE', NULL, NULL, 7),
    (24, 'Control final', 'PENDIENTE', NULL, NULL, 7),
    (25, 'Inspección inicial', 'PENDIENTE', NULL, NULL, 8),
    (26, 'Cambio de aceite y filtro', 'PENDIENTE', NULL, NULL, 8),
    (27, 'Cambio de filtro de aire', 'PENDIENTE', NULL, NULL, 8),
    (28, 'Control final', 'PENDIENTE', NULL, NULL, 8),
    (29, 'Inspección inicial', 'PENDIENTE', NULL, NULL, 9),
    (30, 'Cambio de aceite y filtro', 'PENDIENTE', NULL, NULL, 9),
    (31, 'Cambio de filtro de cabina', 'PENDIENTE', NULL, NULL, 9),
    (32, 'Control final', 'PENDIENTE', NULL, NULL, 9),
    (33, 'Inspección inicial', 'PENDIENTE', NULL, NULL, 10),
    (34, 'Servicio de líquido de frenos', 'PENDIENTE', NULL, NULL, 10),
    (35, 'Control final', 'PENDIENTE', NULL, NULL, 10),
    (36, 'Inspección inicial', 'PENDIENTE', NULL, NULL, 11),
    (37, 'Cambio de refrigerante', 'PENDIENTE', NULL, NULL, 11),
    (38, 'Control final', 'PENDIENTE', NULL, NULL, 11),
    (39, 'Inspección inicial', 'FINALIZADA', '2026-09-22 09:00:00', '2026-09-22 09:10:00', 12),
    (40, 'Cambio de aceite de transmisión', 'EN_PROCESO', '2026-09-22 09:15:00', NULL, 12),
    (41, 'Control final', 'PENDIENTE', NULL, NULL, 12),
    (42, 'Inspección inicial', 'FINALIZADA', '2026-09-23 09:00:00', '2026-09-23 09:10:00', 13),
    (43, 'Lubricación y revisión general', 'EN_PROCESO', '2026-09-23 09:15:00', NULL, 13),
    (44, 'Control final', 'PENDIENTE', NULL, NULL, 13),
    (45, 'Inspección inicial', 'FINALIZADA', '2026-09-24 09:00:00', '2026-09-24 09:10:00', 14),
    (46, 'Cambio de filtro de combustible', 'FINALIZADA', '2026-09-24 09:15:00', '2026-09-24 09:25:00', 14),
    (47, 'Control final', 'FINALIZADA', '2026-09-24 09:30:00', '2026-09-24 09:40:00', 14),
    (48, 'Inspección inicial', 'FINALIZADA', '2026-09-25 09:00:00', '2026-09-25 09:10:00', 15),
    (49, 'Cambio de aceite y filtro', 'FINALIZADA', '2026-09-25 09:15:00', '2026-09-25 09:25:00', 15),
    (50, 'Cambio de filtro de aire', 'FINALIZADA', '2026-09-25 09:30:00', '2026-09-25 09:40:00', 15),
    (51, 'Cambio de filtro de cabina', 'FINALIZADA', '2026-09-25 09:45:00', '2026-09-25 09:55:00', 15),
    (52, 'Control final', 'FINALIZADA', '2026-09-25 10:00:00', '2026-09-25 10:10:00', 15),
    (53, 'Inspección visual complementaria de bajos: no realizada', 'NO_REALIZADA', NULL, NULL, 14);

-- consumo_real: 8 registros.
INSERT INTO consumo_real (consumo_id, producto_id, presentacion_id, cantidad_real, precio_aplicado, ejecucion_id) VALUES
    (1, 9, 13, 2, 55.00, 12),
    (2, 10, 15, 0.5, 24.00, 13),
    (3, 11, 16, 1, 12.00, 13),
    (4, 12, 18, 1, 48.00, 14),
    (5, 1, 2, 1, 125.00, 15),
    (6, 3, 5, 1, 28.00, 15),
    (7, 4, 6, 1, 42.00, 15),
    (8, 5, 7, 1, 38.00, 15);

COMMIT;

-- VERIFICACIONES: cada fila de esta consulta debe tener violaciones = 0.

SELECT 'FK productos.presentacion_producto.producto_id' AS verificacion, (SELECT COUNT(*) FROM msvc_productos.presentacion_producto c LEFT JOIN msvc_productos.producto p ON p.producto_id=c.producto_id WHERE c.producto_id IS NOT NULL AND p.producto_id IS NULL) AS violaciones
UNION ALL
SELECT 'FK servicios.servicio_tipo_producto_requerido.servicio_id' AS verificacion, (SELECT COUNT(*) FROM msvc_servicios.servicio_tipo_producto_requerido c LEFT JOIN msvc_servicios.servicio_mantenimiento p ON p.servicio_id=c.servicio_id WHERE c.servicio_id IS NOT NULL AND p.servicio_id IS NULL) AS violaciones
UNION ALL
SELECT 'FK ordenes.orden_servicio.propuesta_id' AS verificacion, (SELECT COUNT(*) FROM msvc_ordenes.orden_servicio c LEFT JOIN msvc_ordenes.propuesta_tecnica p ON p.propuesta_id=c.propuesta_id WHERE c.propuesta_id IS NOT NULL AND p.propuesta_id IS NULL) AS violaciones
UNION ALL
SELECT 'FK ordenes.orden_servicio.autorizacion_id' AS verificacion, (SELECT COUNT(*) FROM msvc_ordenes.orden_servicio c LEFT JOIN msvc_ordenes.autorizacion p ON p.autorizacion_id=c.autorizacion_id WHERE c.autorizacion_id IS NOT NULL AND p.autorizacion_id IS NULL) AS violaciones
UNION ALL
SELECT 'FK ordenes.orden_servicios.orden_id' AS verificacion, (SELECT COUNT(*) FROM msvc_ordenes.orden_servicios c LEFT JOIN msvc_ordenes.orden_servicio p ON p.orden_id=c.orden_id WHERE c.orden_id IS NOT NULL AND p.orden_id IS NULL) AS violaciones
UNION ALL
SELECT 'FK ordenes.propuesta_servicios.propuesta_id' AS verificacion, (SELECT COUNT(*) FROM msvc_ordenes.propuesta_servicios c LEFT JOIN msvc_ordenes.propuesta_tecnica p ON p.propuesta_id=c.propuesta_id WHERE c.propuesta_id IS NOT NULL AND p.propuesta_id IS NULL) AS violaciones
UNION ALL
SELECT 'FK ordenes.autorizacion_servicios.autorizacion_id' AS verificacion, (SELECT COUNT(*) FROM msvc_ordenes.autorizacion_servicios c LEFT JOIN msvc_ordenes.autorizacion p ON p.autorizacion_id=c.autorizacion_id WHERE c.autorizacion_id IS NOT NULL AND p.autorizacion_id IS NULL) AS violaciones
UNION ALL
SELECT 'FK requerimientos.detalle_requerimiento.requerimiento_id' AS verificacion, (SELECT COUNT(*) FROM msvc_requerimientos.detalle_requerimiento c LEFT JOIN msvc_requerimientos.requerimiento_mantenimiento p ON p.requerimiento_id=c.requerimiento_id WHERE c.requerimiento_id IS NOT NULL AND p.requerimiento_id IS NULL) AS violaciones
UNION ALL
SELECT 'FK ejecucion.tarea_ejecutada.ejecucion_id' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.tarea_ejecutada c LEFT JOIN msvc_ejecucion.ejecucion_mantenimiento p ON p.ejecucion_id=c.ejecucion_id WHERE c.ejecucion_id IS NOT NULL AND p.ejecucion_id IS NULL) AS violaciones
UNION ALL
SELECT 'FK ejecucion.consumo_real.ejecucion_id' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.consumo_real c LEFT JOIN msvc_ejecucion.ejecucion_mantenimiento p ON p.ejecucion_id=c.ejecucion_id WHERE c.ejecucion_id IS NOT NULL AND p.ejecucion_id IS NULL) AS violaciones
UNION ALL
SELECT 'Ficha técnica completa y consistente' AS verificacion, (SELECT COUNT(*) FROM msvc_vehiculos.vehiculo WHERE marca<>ficha_marca OR modelo<>ficha_modelo OR anio<>ficha_anio OR tipo_combustible<>ficha_tipo_combustible OR kilometraje_actual<0 OR anio NOT BETWEEN 1 AND 9999 OR TRIM(motor)='' OR TRIM(cilindrada)='') AS violaciones
UNION ALL
SELECT 'Referencia orden a vehículo' AS verificacion, (SELECT COUNT(*) FROM msvc_ordenes.orden_servicio o LEFT JOIN msvc_vehiculos.vehiculo v ON v.vehiculo_id=o.vehiculo_id WHERE v.vehiculo_id IS NULL OR o.kilometraje<0) AS violaciones
UNION ALL
SELECT 'Referencia a servicio: orden_servicios' AS verificacion, (SELECT COUNT(*) FROM msvc_ordenes.orden_servicios c LEFT JOIN msvc_servicios.servicio_mantenimiento s ON s.servicio_id=c.servicio_id WHERE s.servicio_id IS NULL) AS violaciones
UNION ALL
SELECT 'Servicios sin duplicados: orden_servicios' AS verificacion, (SELECT COUNT(*) FROM (SELECT orden_id,servicio_id FROM msvc_ordenes.orden_servicios GROUP BY orden_id,servicio_id HAVING COUNT(*)>1) duplicados) AS violaciones
UNION ALL
SELECT 'Referencia a servicio: propuesta_servicios' AS verificacion, (SELECT COUNT(*) FROM msvc_ordenes.propuesta_servicios c LEFT JOIN msvc_servicios.servicio_mantenimiento s ON s.servicio_id=c.servicio_id WHERE s.servicio_id IS NULL) AS violaciones
UNION ALL
SELECT 'Servicios sin duplicados: propuesta_servicios' AS verificacion, (SELECT COUNT(*) FROM (SELECT propuesta_id,servicio_id FROM msvc_ordenes.propuesta_servicios GROUP BY propuesta_id,servicio_id HAVING COUNT(*)>1) duplicados) AS violaciones
UNION ALL
SELECT 'Referencia a servicio: autorizacion_servicios' AS verificacion, (SELECT COUNT(*) FROM msvc_ordenes.autorizacion_servicios c LEFT JOIN msvc_servicios.servicio_mantenimiento s ON s.servicio_id=c.servicio_id WHERE s.servicio_id IS NULL) AS violaciones
UNION ALL
SELECT 'Servicios sin duplicados: autorizacion_servicios' AS verificacion, (SELECT COUNT(*) FROM (SELECT autorizacion_id,servicio_id FROM msvc_ordenes.autorizacion_servicios GROUP BY autorizacion_id,servicio_id HAVING COUNT(*)>1) duplicados) AS violaciones
UNION ALL
SELECT 'Posiciones desde cero: servicio_tipo_producto_requerido' AS verificacion, (SELECT COUNT(*) FROM (SELECT servicio_id FROM msvc_servicios.servicio_tipo_producto_requerido GROUP BY servicio_id HAVING MIN(posicion)<>0 OR MAX(posicion)<>COUNT(*)-1) listas) AS violaciones
UNION ALL
SELECT 'Posiciones desde cero: orden_servicios' AS verificacion, (SELECT COUNT(*) FROM (SELECT orden_id FROM msvc_ordenes.orden_servicios GROUP BY orden_id HAVING MIN(posicion)<>0 OR MAX(posicion)<>COUNT(*)-1) listas) AS violaciones
UNION ALL
SELECT 'Posiciones desde cero: propuesta_servicios' AS verificacion, (SELECT COUNT(*) FROM (SELECT propuesta_id FROM msvc_ordenes.propuesta_servicios GROUP BY propuesta_id HAVING MIN(posicion)<>0 OR MAX(posicion)<>COUNT(*)-1) listas) AS violaciones
UNION ALL
SELECT 'Posiciones desde cero: autorizacion_servicios' AS verificacion, (SELECT COUNT(*) FROM (SELECT autorizacion_id FROM msvc_ordenes.autorizacion_servicios GROUP BY autorizacion_id HAVING MIN(posicion)<>0 OR MAX(posicion)<>COUNT(*)-1) listas) AS violaciones
UNION ALL
SELECT 'Autorizaciones completas y con servicios' AS verificacion, (SELECT COUNT(*) FROM msvc_ordenes.autorizacion a WHERE TRIM(a.medio)='' OR TRIM(a.alcance)='' OR a.fecha IS NULL OR NOT EXISTS (SELECT 1 FROM msvc_ordenes.autorizacion_servicios s WHERE s.autorizacion_id=a.autorizacion_id)) AS violaciones
UNION ALL
SELECT 'Orden AUTORIZADA con autorización y propuesta' AS verificacion, (SELECT COUNT(*) FROM msvc_ordenes.orden_servicio o LEFT JOIN msvc_ordenes.autorizacion a ON a.autorizacion_id=o.autorizacion_id LEFT JOIN msvc_ordenes.propuesta_tecnica p ON p.propuesta_id=o.propuesta_id WHERE o.estado='AUTORIZADA' AND (a.autorizacion_id IS NULL OR p.propuesta_id IS NULL)) AS violaciones
UNION ALL
SELECT 'Servicios autorizados incluidos en propuesta' AS verificacion, (SELECT COUNT(*) FROM msvc_ordenes.orden_servicio o JOIN msvc_ordenes.autorizacion_servicios a ON a.autorizacion_id=o.autorizacion_id LEFT JOIN msvc_ordenes.propuesta_servicios p ON p.propuesta_id=o.propuesta_id AND p.servicio_id=a.servicio_id WHERE p.servicio_id IS NULL) AS violaciones
UNION ALL
SELECT 'Autorización posterior a recepción' AS verificacion, (SELECT COUNT(*) FROM msvc_ordenes.orden_servicio o JOIN msvc_ordenes.autorizacion a ON a.autorizacion_id=o.autorizacion_id WHERE a.fecha<o.fecha) AS violaciones
UNION ALL
SELECT 'Propuestas y autorizaciones asociadas a órdenes' AS verificacion, (SELECT (SELECT COUNT(*) FROM msvc_ordenes.propuesta_tecnica p WHERE NOT EXISTS (SELECT 1 FROM msvc_ordenes.orden_servicio o WHERE o.propuesta_id=p.propuesta_id)) + (SELECT COUNT(*) FROM msvc_ordenes.autorizacion a WHERE NOT EXISTS (SELECT 1 FROM msvc_ordenes.orden_servicio o WHERE o.autorizacion_id=a.autorizacion_id))) AS violaciones
UNION ALL
SELECT 'Referencia a orden: requerimiento_mantenimiento' AS verificacion, (SELECT COUNT(*) FROM msvc_requerimientos.requerimiento_mantenimiento c LEFT JOIN msvc_ordenes.orden_servicio o ON o.orden_id=c.orden_id WHERE o.orden_id IS NULL) AS violaciones
UNION ALL
SELECT 'Referencia a orden: ejecucion_mantenimiento' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.ejecucion_mantenimiento c LEFT JOIN msvc_ordenes.orden_servicio o ON o.orden_id=c.orden_id WHERE o.orden_id IS NULL) AS violaciones
UNION ALL
SELECT 'Producto y presentación: detalle_requerimiento' AS verificacion, (SELECT COUNT(*) FROM msvc_requerimientos.detalle_requerimiento c LEFT JOIN msvc_productos.producto p ON p.producto_id=c.producto_id LEFT JOIN msvc_productos.presentacion_producto pr ON pr.presentacion_id=c.presentacion_id AND pr.producto_id=c.producto_id WHERE p.producto_id IS NULL OR pr.presentacion_id IS NULL) AS violaciones
UNION ALL
SELECT 'Producto y presentación: consumo_real' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.consumo_real c LEFT JOIN msvc_productos.producto p ON p.producto_id=c.producto_id LEFT JOIN msvc_productos.presentacion_producto pr ON pr.presentacion_id=c.presentacion_id AND pr.producto_id=c.producto_id WHERE p.producto_id IS NULL OR pr.presentacion_id IS NULL) AS violaciones
UNION ALL
SELECT 'Tipos requeridos representados en catálogo' AS verificacion, (SELECT COUNT(*) FROM msvc_servicios.servicio_tipo_producto_requerido r WHERE NOT EXISTS (SELECT 1 FROM msvc_productos.producto p WHERE p.categoria=r.categoria_producto AND p.tipo=r.tipo_producto)) AS violaciones
UNION ALL
SELECT 'Contenido de presentaciones positivo' AS verificacion, (SELECT COUNT(*) FROM msvc_productos.presentacion_producto WHERE contenido<=0) AS violaciones
UNION ALL
SELECT 'Detalles sin producto y presentación duplicados' AS verificacion, (SELECT COUNT(*) FROM (SELECT requerimiento_id,producto_id,presentacion_id FROM msvc_requerimientos.detalle_requerimiento GROUP BY requerimiento_id,producto_id,presentacion_id HAVING COUNT(*)>1) duplicados) AS violaciones
UNION ALL
SELECT 'Disponibilidad calculada y reservas válidas' AS verificacion, (SELECT COUNT(*) FROM msvc_requerimientos.detalle_requerimiento WHERE cantidad_requerida<=0 OR cantidad_disponible<0 OR estado_disponibilidad<>CASE WHEN cantidad_disponible=0 THEN 'FALTANTE' WHEN cantidad_disponible<cantidad_requerida THEN 'PARCIAL' ELSE 'DISPONIBLE' END OR (reservado=b'1' AND estado_disponibilidad<>'DISPONIBLE')) AS violaciones
UNION ALL
SELECT 'Una ejecución por orden' AS verificacion, (SELECT COUNT(*) FROM (SELECT orden_id FROM msvc_ejecucion.ejecucion_mantenimiento GROUP BY orden_id HAVING COUNT(*)>1) duplicados) AS violaciones
UNION ALL
SELECT 'Ejecuciones con autorización válida' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.ejecucion_mantenimiento e LEFT JOIN msvc_ordenes.orden_servicio o ON o.orden_id=e.orden_id LEFT JOIN msvc_ordenes.autorizacion a ON a.autorizacion_id=o.autorizacion_id LEFT JOIN msvc_ordenes.propuesta_tecnica p ON p.propuesta_id=o.propuesta_id WHERE o.orden_id IS NULL OR o.estado NOT IN ('AUTORIZADA','EN_PROCESO','FINALIZADA') OR a.autorizacion_id IS NULL OR p.propuesta_id IS NULL OR TRIM(a.medio)='' OR TRIM(a.alcance)='' OR a.fecha IS NULL OR NOT EXISTS (SELECT 1 FROM msvc_ordenes.autorizacion_servicios s WHERE s.autorizacion_id=a.autorizacion_id) OR EXISTS (SELECT 1 FROM msvc_ordenes.autorizacion_servicios s WHERE s.autorizacion_id=a.autorizacion_id AND NOT EXISTS (SELECT 1 FROM msvc_ordenes.propuesta_servicios ps WHERE ps.propuesta_id=p.propuesta_id AND ps.servicio_id=s.servicio_id))) AS violaciones
UNION ALL
SELECT 'Identificador técnico positivo (sin catálogo en proyecto)' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.ejecucion_mantenimiento WHERE tecnico_id<=0) AS violaciones
UNION ALL
SELECT 'Enums exactos de ejecución y tarea' AS verificacion, (SELECT (SELECT COUNT(*) FROM msvc_ejecucion.ejecucion_mantenimiento WHERE BINARY estado NOT IN ('PENDIENTE','EN_PROCESO','FINALIZADA','CANCELADA')) + (SELECT COUNT(*) FROM msvc_ejecucion.tarea_ejecutada WHERE BINARY estado NOT IN ('PENDIENTE','EN_PROCESO','FINALIZADA','NO_REALIZADA'))) AS violaciones
UNION ALL
SELECT 'Ejecuciones pendientes sin fechas ni importes' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.ejecucion_mantenimiento WHERE estado='PENDIENTE' AND (fecha_inicio IS NOT NULL OR fecha_fin IS NOT NULL OR subtotal_productos<>0 OR subtotal_mano_obra<>0 OR total<>0)) AS violaciones
UNION ALL
SELECT 'Tareas pendientes de ejecución pendiente' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.tarea_ejecutada t JOIN msvc_ejecucion.ejecucion_mantenimiento e ON e.ejecucion_id=t.ejecucion_id WHERE e.estado='PENDIENTE' AND (t.estado<>'PENDIENTE' OR t.inicio IS NOT NULL OR t.fin IS NOT NULL)) AS violaciones
UNION ALL
SELECT 'Sin consumos en ejecuciones pendientes' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.consumo_real c JOIN msvc_ejecucion.ejecucion_mantenimiento e ON e.ejecucion_id=c.ejecucion_id WHERE e.estado='PENDIENTE') AS violaciones
UNION ALL
SELECT 'Fechas de ejecuciones en proceso y finalizadas' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.ejecucion_mantenimiento WHERE (estado='EN_PROCESO' AND (fecha_inicio IS NULL OR fecha_fin IS NOT NULL)) OR (estado='FINALIZADA' AND (fecha_inicio IS NULL OR fecha_fin IS NULL)) OR (fecha_fin IS NOT NULL AND (fecha_inicio IS NULL OR fecha_fin<fecha_inicio))) AS violaciones
UNION ALL
SELECT 'Inicio posterior a autorización' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.ejecucion_mantenimiento e JOIN msvc_ordenes.orden_servicio o ON o.orden_id=e.orden_id JOIN msvc_ordenes.autorizacion a ON a.autorizacion_id=o.autorizacion_id WHERE e.fecha_inicio<a.fecha) AS violaciones
UNION ALL
SELECT 'Finalizadas sin tareas abiertas' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.tarea_ejecutada t JOIN msvc_ejecucion.ejecucion_mantenimiento e ON e.ejecucion_id=t.ejecucion_id WHERE e.estado='FINALIZADA' AND t.estado NOT IN ('FINALIZADA','NO_REALIZADA')) AS violaciones
UNION ALL
SELECT 'Fechas coherentes por estado de tarea' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.tarea_ejecutada WHERE (estado='PENDIENTE' AND (inicio IS NOT NULL OR fin IS NOT NULL)) OR (estado='EN_PROCESO' AND (inicio IS NULL OR fin IS NOT NULL)) OR (estado='FINALIZADA' AND (inicio IS NULL OR fin IS NULL)) OR (fin IS NOT NULL AND (inicio IS NULL OR fin<inicio))) AS violaciones
UNION ALL
SELECT 'Tareas dentro del intervalo de ejecución' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.tarea_ejecutada t JOIN msvc_ejecucion.ejecucion_mantenimiento e ON e.ejecucion_id=t.ejecucion_id WHERE (t.inicio IS NOT NULL AND (e.fecha_inicio IS NULL OR t.inicio<e.fecha_inicio OR (e.fecha_fin IS NOT NULL AND t.inicio>e.fecha_fin))) OR (t.fin IS NOT NULL AND (e.fecha_inicio IS NULL OR t.fin<e.fecha_inicio OR (e.fecha_fin IS NOT NULL AND t.fin>e.fecha_fin)))) AS violaciones
UNION ALL
SELECT 'Cantidades y precios de consumos' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.consumo_real WHERE cantidad_real<=0 OR precio_aplicado<0) AS violaciones
UNION ALL
SELECT 'Subtotales y total iguales al cálculo de dominio' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.ejecucion_mantenimiento e LEFT JOIN (SELECT ejecucion_id,ROUND(SUM(cantidad_real*precio_aplicado),2) subtotal FROM msvc_ejecucion.consumo_real GROUP BY ejecucion_id) c ON c.ejecucion_id=e.ejecucion_id WHERE e.subtotal_productos<>COALESCE(c.subtotal,0) OR e.total<>e.subtotal_productos+e.subtotal_mano_obra OR e.subtotal_mano_obra<0) AS violaciones
UNION ALL
SELECT 'Próximo servicio completo y posterior al cierre' AS verificacion, (SELECT COUNT(*) FROM msvc_ejecucion.ejecucion_mantenimiento e JOIN msvc_ordenes.orden_servicio o ON o.orden_id=e.orden_id WHERE (e.fecha_sugerida IS NULL AND e.kilometraje_sugerido IS NOT NULL) OR (e.fecha_sugerida IS NOT NULL AND (e.kilometraje_sugerido IS NULL OR e.kilometraje_sugerido<o.kilometraje OR e.fecha_sugerida<=DATE(e.fecha_fin)))) AS violaciones
UNION ALL
SELECT 'Mayoría pendiente y variedad de ejecuciones' AS verificacion, (SELECT CASE WHEN SUM(estado='PENDIENTE')>COUNT(*)/2 AND SUM(estado='EN_PROCESO')>=2 AND SUM(estado='FINALIZADA')>=2 THEN 0 ELSE 1 END FROM msvc_ejecucion.ejecucion_mantenimiento) AS violaciones;

-- Resumen esperado: 6 pendientes / 14 autorizadas / 2 en proceso / 2 finalizadas; ejecuciones 11 / 2 / 2.
SELECT 'ordenes_PENDIENTES' AS caso, COUNT(*) AS registros
FROM msvc_ordenes.orden_servicio WHERE estado = 'PENDIENTE'
UNION ALL SELECT 'ordenes_autorizadas', COUNT(*)
FROM msvc_ordenes.orden_servicio WHERE estado = 'AUTORIZADA'
UNION ALL SELECT 'ordenes_EN_PROCESO', COUNT(*)
FROM msvc_ordenes.orden_servicio WHERE estado = 'EN_PROCESO'
UNION ALL SELECT 'ordenes_FINALIZADAS', COUNT(*)
FROM msvc_ordenes.orden_servicio WHERE estado = 'FINALIZADA'
UNION ALL SELECT 'ejecuciones_PENDIENTES', COUNT(*)
FROM msvc_ejecucion.ejecucion_mantenimiento WHERE estado = 'PENDIENTE'
UNION ALL SELECT 'ejecuciones_EN_PROCESO', COUNT(*)
FROM msvc_ejecucion.ejecucion_mantenimiento WHERE estado = 'EN_PROCESO'
UNION ALL SELECT 'ejecuciones_FINALIZADAS', COUNT(*)
FROM msvc_ejecucion.ejecucion_mantenimiento WHERE estado = 'FINALIZADA';

-- Órdenes autorizadas disponibles para crear una ejecución desde Postman: 7, 8 y 9.
SELECT o.orden_id FROM msvc_ordenes.orden_servicio o
LEFT JOIN msvc_ejecucion.ejecucion_mantenimiento e ON e.orden_id=o.orden_id
WHERE o.estado='AUTORIZADA' AND e.ejecucion_id IS NULL ORDER BY o.orden_id;

-- Inventario final: todas las tablas persistentes tienen datos.

SELECT 'msvc_vehiculos.vehiculo' AS tabla, COUNT(*) AS registros FROM msvc_vehiculos.vehiculo
UNION ALL
SELECT 'msvc_productos.producto' AS tabla, COUNT(*) AS registros FROM msvc_productos.producto
UNION ALL
SELECT 'msvc_productos.presentacion_producto' AS tabla, COUNT(*) AS registros FROM msvc_productos.presentacion_producto
UNION ALL
SELECT 'msvc_servicios.servicio_mantenimiento' AS tabla, COUNT(*) AS registros FROM msvc_servicios.servicio_mantenimiento
UNION ALL
SELECT 'msvc_servicios.servicio_tipo_producto_requerido' AS tabla, COUNT(*) AS registros FROM msvc_servicios.servicio_tipo_producto_requerido
UNION ALL
SELECT 'msvc_ordenes.propuesta_tecnica' AS tabla, COUNT(*) AS registros FROM msvc_ordenes.propuesta_tecnica
UNION ALL
SELECT 'msvc_ordenes.autorizacion' AS tabla, COUNT(*) AS registros FROM msvc_ordenes.autorizacion
UNION ALL
SELECT 'msvc_ordenes.orden_servicio' AS tabla, COUNT(*) AS registros FROM msvc_ordenes.orden_servicio
UNION ALL
SELECT 'msvc_ordenes.orden_servicios' AS tabla, COUNT(*) AS registros FROM msvc_ordenes.orden_servicios
UNION ALL
SELECT 'msvc_ordenes.propuesta_servicios' AS tabla, COUNT(*) AS registros FROM msvc_ordenes.propuesta_servicios
UNION ALL
SELECT 'msvc_ordenes.autorizacion_servicios' AS tabla, COUNT(*) AS registros FROM msvc_ordenes.autorizacion_servicios
UNION ALL
SELECT 'msvc_requerimientos.requerimiento_mantenimiento' AS tabla, COUNT(*) AS registros FROM msvc_requerimientos.requerimiento_mantenimiento
UNION ALL
SELECT 'msvc_requerimientos.detalle_requerimiento' AS tabla, COUNT(*) AS registros FROM msvc_requerimientos.detalle_requerimiento
UNION ALL
SELECT 'msvc_ejecucion.ejecucion_mantenimiento' AS tabla, COUNT(*) AS registros FROM msvc_ejecucion.ejecucion_mantenimiento
UNION ALL
SELECT 'msvc_ejecucion.tarea_ejecutada' AS tabla, COUNT(*) AS registros FROM msvc_ejecucion.tarea_ejecutada
UNION ALL
SELECT 'msvc_ejecucion.consumo_real' AS tabla, COUNT(*) AS registros FROM msvc_ejecucion.consumo_real;

SET SESSION sql_mode = @lubrimax_sql_mode_anterior;
SET SESSION foreign_key_checks = @lubrimax_fk_anteriores;

-- Comprobaciones adicionales del ciclo de vida (todas deben devolver cero).
SELECT COUNT(*) AS orden_ejecucion FROM msvc_ejecucion.ejecucion_mantenimiento e JOIN msvc_ordenes.orden_servicio o ON o.orden_id=e.orden_id WHERE o.estado <> CASE e.estado WHEN 'PENDIENTE' THEN 'AUTORIZADA' ELSE e.estado END;
SELECT COUNT(*) AS pendientes_sin_fechas FROM msvc_ejecucion.ejecucion_mantenimiento WHERE estado='PENDIENTE' AND (fecha_inicio IS NOT NULL OR fecha_fin IS NOT NULL);
SELECT COUNT(*) AS pendientes_sin_tareas_avanzadas FROM msvc_ejecucion.tarea_ejecutada t JOIN msvc_ejecucion.ejecucion_mantenimiento e ON e.ejecucion_id=t.ejecucion_id WHERE e.estado='PENDIENTE' AND (t.estado<>'PENDIENTE' OR t.inicio IS NOT NULL OR t.fin IS NOT NULL);
SELECT COUNT(*) AS finalizadas_sin_abiertas FROM msvc_ejecucion.tarea_ejecutada t JOIN msvc_ejecucion.ejecucion_mantenimiento e ON e.ejecucion_id=t.ejecucion_id WHERE e.estado='FINALIZADA' AND t.estado NOT IN ('FINALIZADA','NO_REALIZADA');
SELECT COUNT(*) AS fechas_tareas FROM msvc_ejecucion.tarea_ejecutada t JOIN msvc_ejecucion.ejecucion_mantenimiento e ON e.ejecucion_id=t.ejecucion_id WHERE (t.inicio IS NOT NULL AND (e.fecha_inicio IS NULL OR t.inicio<e.fecha_inicio OR (e.fecha_fin IS NOT NULL AND t.inicio>e.fecha_fin))) OR (t.fin IS NOT NULL AND (t.inicio IS NULL OR t.fin<t.inicio OR (e.fecha_fin IS NOT NULL AND t.fin>e.fecha_fin)));

-- Si este texto no muestra tildes, revisar la codificación del cliente.
SELECT 'Inspección, transmisión, líquido, María, año' AS prueba_utf8;
