package org.lubrimax.com.application.port.out;

import org.lubrimax.com.domain.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoRepositoryPort {
    List<Producto> listar();
    Optional<Producto> buscarPorId(Long id);
    Producto guardarProducto(Producto producto);
    void borrarProducto(Long id);
    boolean existePorCodigo(String codigo, Long productoId);
}
