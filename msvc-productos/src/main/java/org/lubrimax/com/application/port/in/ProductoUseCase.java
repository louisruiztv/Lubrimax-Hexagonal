package org.lubrimax.com.application.port.in;

import org.lubrimax.com.domain.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoUseCase {
    List<Producto> listar();
    Optional<Producto> buscarPorId(Long id);
    Producto guardarProducto(Producto producto);
    Optional<Producto> actualizar(Long id, Producto producto);
    boolean borrarProducto(Long id);
}
