package org.lubrimax.com.infrastructure.adapter.out.client;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.*;
import org.lubrimax.com.application.exception.ServicioExternoNoDisponibleException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServicioRestAdapter implements ServicioClientPort {
    private final ServicioFeignClient client;

    @Override
    public boolean existePorId(Long id) {
        try {
            client.buscarPorId(id);
            return true;
        } catch (FeignException.NotFound exception) {
            return false;
        } catch (FeignException exception) {
            throw new ServicioExternoNoDisponibleException("No se pudo consultar msvc-servicios.", exception);
        }
    }
}
