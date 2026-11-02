package org.lubrimax.com.infrastructure.config;

import org.lubrimax.com.application.port.in.VehiculoUseCase;
import org.lubrimax.com.application.port.out.VehiculoRepositoryPort;
import org.lubrimax.com.application.service.VehiculoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ApplicationConfig {
    @Bean
    public VehiculoUseCase vehiculoUseCase(VehiculoRepositoryPort repository) {
        return new VehiculoService(repository);
    }
}
