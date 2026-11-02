package org.lubrimax.com.infrastructure.config;

import org.lubrimax.com.application.port.in.ProductoUseCase;
import org.lubrimax.com.application.port.out.ProductoRepositoryPort;
import org.lubrimax.com.application.service.ProductoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ApplicationConfig {
    @Bean
    public ProductoUseCase productoUseCase(ProductoRepositoryPort repository) {
        return new ProductoService(repository);
    }
}
