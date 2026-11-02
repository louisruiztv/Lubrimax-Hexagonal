package org.lubrimax.com.infrastructure.config;

import org.lubrimax.com.application.port.in.RequerimientoDeMantenimientoUseCase;
import org.lubrimax.com.application.port.out.*;
import org.lubrimax.com.application.service.RequerimientoDeMantenimientoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ApplicationConfig {
    @Bean
    public RequerimientoDeMantenimientoUseCase requerimientoDeMantenimientoUseCase(RequerimientoDeMantenimientoRepositoryPort repository, OrdenClientPort orden, ProductoClientPort producto) {
        return new RequerimientoDeMantenimientoService(repository, orden, producto);
    }
}
