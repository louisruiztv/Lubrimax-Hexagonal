package org.lubrimax.com.application.port.out;

import org.lubrimax.com.domain.model.RequerimientoDeMantenimiento;
import java.util.List;
import java.util.Optional;

public interface RequerimientoDeMantenimientoRepositoryPort {
    List<RequerimientoDeMantenimiento> listar();
    Optional<RequerimientoDeMantenimiento> buscarPorId(Long id);
    RequerimientoDeMantenimiento guardarRequerimiento(RequerimientoDeMantenimiento requerimiento);
    void borrarRequerimiento(Long id);
}
