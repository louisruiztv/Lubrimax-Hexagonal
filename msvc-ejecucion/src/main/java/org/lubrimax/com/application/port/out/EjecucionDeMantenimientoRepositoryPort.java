package org.lubrimax.com.application.port.out;

import org.lubrimax.com.domain.model.EjecucionDeMantenimiento;
import java.util.List;
import java.util.Optional;

public interface EjecucionDeMantenimientoRepositoryPort {
    List<EjecucionDeMantenimiento> listar();
    Optional<EjecucionDeMantenimiento> buscarPorId(Long id);
    EjecucionDeMantenimiento guardarEjecucion(EjecucionDeMantenimiento ejecucion);
    void borrarEjecucion(Long id);
    boolean existePorOrdenId(Long ordenId, Long ejecucionId);
}
