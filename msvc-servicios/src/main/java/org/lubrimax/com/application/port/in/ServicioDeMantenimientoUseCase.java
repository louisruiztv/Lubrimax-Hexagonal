package org.lubrimax.com.application.port.in;

import org.lubrimax.com.domain.model.ServicioDeMantenimiento;
import java.util.List;
import java.util.Optional;

public interface ServicioDeMantenimientoUseCase {
    List<ServicioDeMantenimiento> listar();
    Optional<ServicioDeMantenimiento> buscarPorId(Long id);
    ServicioDeMantenimiento guardarServicio(ServicioDeMantenimiento servicio);
    Optional<ServicioDeMantenimiento> actualizar(Long id, ServicioDeMantenimiento servicio);
    boolean borrarServicio(Long id);
}
