package org.lubrimax.com.infrastructure.config;

import org.lubrimax.com.application.port.in.EjecucionDeMantenimientoUseCase;
import org.lubrimax.com.application.port.out.*;
import org.lubrimax.com.application.service.EjecucionDeMantenimientoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ApplicationConfig {
    @Bean
    public EjecucionDeMantenimientoUseCase ejecucionDeMantenimientoUseCase(
            EjecucionDeMantenimientoRepositoryPort repository, OrdenClientPort orden, ProductoClientPort producto) {
        return new EjecucionDeMantenimientoService(repository, orden, producto);
    }
}
