package org.lubrimax.com.infrastructure.config;

import org.lubrimax.com.application.port.in.ServicioDeMantenimientoUseCase;
import org.lubrimax.com.application.port.out.ServicioDeMantenimientoRepositoryPort;
import org.lubrimax.com.application.service.ServicioDeMantenimientoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ApplicationConfig {
    @Bean
    public ServicioDeMantenimientoUseCase servicioDeMantenimientoUseCase(ServicioDeMantenimientoRepositoryPort repository) {
        return new ServicioDeMantenimientoService(repository);
    }
}
