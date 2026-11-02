package org.lubrimax.com.infrastructure.adapter.out.client;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.EjecucionClientPort;
import org.lubrimax.com.application.exception.ServicioExternoNoDisponibleException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EjecucionRestAdapter implements EjecucionClientPort {
    private final EjecucionFeignClient client;

    @Override
    public String estadoDeEjecucion(Long ordenId, Long ejecucionId) {
        try {
            var ejecucion = client.buscarPorId(ejecucionId);
            if (ejecucion == null || !ejecucionId.equals(ejecucion.ejecucionId()) || ejecucion.estado() == null)
                throw new ServicioExternoNoDisponibleException("Respuesta inválida de msvc-ejecucion.", null);
            if (!ordenId.equals(ejecucion.ordenId()))
                throw new IllegalStateException("La ejecución no pertenece a esta orden.");
            return ejecucion.estado();
        } catch (FeignException.NotFound ex) {
            throw new IllegalStateException("La ejecución no existe.");
        } catch (FeignException ex) {
            throw new ServicioExternoNoDisponibleException("No se pudo verificar el estado de la ejecución.", ex);
        }
    }
}
