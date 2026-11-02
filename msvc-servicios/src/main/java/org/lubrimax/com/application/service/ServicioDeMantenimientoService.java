package org.lubrimax.com.application.service;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.ServicioDeMantenimientoRepositoryPort;
import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.application.port.in.ServicioDeMantenimientoUseCase;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ServicioDeMantenimientoService implements ServicioDeMantenimientoUseCase {
    private final ServicioDeMantenimientoRepositoryPort servicioRepositoryPort;

    @Override
    public List<ServicioDeMantenimiento> listar() {
        return servicioRepositoryPort.listar();
    }

    @Override
    public Optional<ServicioDeMantenimiento> buscarPorId(Long id) {
        return servicioRepositoryPort.buscarPorId(id);
    }


    @Override
    public ServicioDeMantenimiento guardarServicio(ServicioDeMantenimiento servicio) {
        if (servicio.getServicioId() != null) {
            throw new IllegalArgumentException("El ID del servicio se genera automáticamente.");
        }
        servicio.validarDatos();
        return servicioRepositoryPort.guardarServicio(servicio);
    }

    @Override
    public Optional<ServicioDeMantenimiento> actualizar(Long id, ServicioDeMantenimiento servicio) {
        return servicioRepositoryPort.buscarPorId(id).map(existente -> {
            servicio.validarDatos();
            existente.setNombre(servicio.getNombre());
            existente.setTipoServicio(servicio.getTipoServicio());
            existente.setDescripcion(servicio.getDescripcion());
            existente.setEstado(servicio.getEstado());
            existente.definirTiposRequeridos(servicio.getRequiere());
            return servicioRepositoryPort.guardarServicio(existente);
        });
    }

    @Override
    public boolean borrarServicio(Long id) {
        if (servicioRepositoryPort.buscarPorId(id).isEmpty()) {
            return false;
        }
        servicioRepositoryPort.borrarServicio(id);
        return true;
    }

}
