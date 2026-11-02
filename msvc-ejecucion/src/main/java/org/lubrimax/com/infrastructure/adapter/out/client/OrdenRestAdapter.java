package org.lubrimax.com.infrastructure.adapter.out.client;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.*;
import org.lubrimax.com.application.exception.ServicioExternoNoDisponibleException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrdenRestAdapter implements OrdenClientPort {
    private final OrdenFeignClient client;

    @Override
    public boolean estaAutorizada(Long ordenId) {
        try {
            return Boolean.TRUE.equals(client.estaAutorizada(ordenId));
        } catch (FeignException.NotFound exception) {
            return false;
        } catch (FeignException exception) {
            throw new ServicioExternoNoDisponibleException("No se pudo consultar msvc-ordenes.", exception);
        }
    }
    @Override
    public void sincronizarEjecucion(Long ordenId, Long ejecucionId) {
        try {
            client.sincronizarEjecucion(ordenId, ejecucionId);
        } catch (FeignException ex) {
            throw new ServicioExternoNoDisponibleException(
                    "La ejecución está guardada, pero no se pudo sincronizar la orden. Recargue y use Sincronizar orden para reintentar.", ex);
        }
    }
}
