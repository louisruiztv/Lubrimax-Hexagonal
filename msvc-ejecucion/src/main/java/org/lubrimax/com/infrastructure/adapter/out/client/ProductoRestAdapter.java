package org.lubrimax.com.infrastructure.adapter.out.client;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.*;
import org.lubrimax.com.application.exception.ServicioExternoNoDisponibleException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductoRestAdapter implements ProductoClientPort {
    private final ProductoFeignClient client;

    @Override
    public boolean existePresentacion(Long productoId, Long presentacionId) {
        try {
            var producto = client.buscarPorId(productoId);
            if (producto == null || !productoId.equals(producto.productoId()) || producto.presentaciones() == null) {
                throw new ServicioExternoNoDisponibleException("Respuesta inválida de msvc-productos.", null);
            }
            return producto.presentaciones().stream()
                    .anyMatch(p -> p != null && presentacionId.equals(p.presentacionId()));
        } catch (FeignException.NotFound exception) {
            return false;
        } catch (FeignException exception) {
            throw new ServicioExternoNoDisponibleException("No se pudo consultar msvc-productos.", exception);
        }
    }
}
