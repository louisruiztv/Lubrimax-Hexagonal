package org.lubrimax.com.application.port.out;

import org.lubrimax.com.domain.model.OrdenDeServicio;
import java.util.List;
import java.util.Optional;

public interface OrdenDeServicioRepositoryPort {
    List<OrdenDeServicio> listar();
    Optional<OrdenDeServicio> buscarPorId(Long id);
    OrdenDeServicio guardarOrden(OrdenDeServicio orden);
    void borrarOrden(Long id);
}
