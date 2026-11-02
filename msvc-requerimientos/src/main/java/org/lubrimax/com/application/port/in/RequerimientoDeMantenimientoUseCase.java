package org.lubrimax.com.application.port.in;

import org.lubrimax.com.domain.model.RequerimientoDeMantenimiento;
import java.util.List;
import java.util.Optional;

public interface RequerimientoDeMantenimientoUseCase {
    List<RequerimientoDeMantenimiento> listar();
    Optional<RequerimientoDeMantenimiento> buscarPorId(Long id);
    RequerimientoDeMantenimiento guardarRequerimiento(RequerimientoDeMantenimiento requerimiento);
    Optional<RequerimientoDeMantenimiento> actualizar(Long id, RequerimientoDeMantenimiento requerimiento);
    boolean borrarRequerimiento(Long id);
}
