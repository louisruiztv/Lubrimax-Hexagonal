package org.lubrimax.com.infrastructure.config;

import org.lubrimax.com.application.port.in.OrdenDeServicioUseCase;
import org.lubrimax.com.application.port.out.*;
import org.lubrimax.com.application.service.OrdenDeServicioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ApplicationConfig {
    @Bean
    public OrdenDeServicioUseCase ordenDeServicioUseCase(OrdenDeServicioRepositoryPort repository, VehiculoClientPort vehiculo, ServicioClientPort servicio, EjecucionClientPort ejecucion) {
        return new OrdenDeServicioService(repository, vehiculo, servicio, ejecucion);
    }
}
