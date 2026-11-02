package org.lubrimax.com.application.port.out;

import org.lubrimax.com.domain.model.ServicioDeMantenimiento;
import java.util.List;
import java.util.Optional;

public interface ServicioDeMantenimientoRepositoryPort {
    List<ServicioDeMantenimiento> listar();
    Optional<ServicioDeMantenimiento> buscarPorId(Long id);
    ServicioDeMantenimiento guardarServicio(ServicioDeMantenimiento servicio);
    void borrarServicio(Long id);
}
