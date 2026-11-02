package org.lubrimax.com.application.service;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.VehiculoRepositoryPort;
import org.lubrimax.com.domain.model.Vehiculo;
import org.lubrimax.com.application.port.in.VehiculoUseCase;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class VehiculoService implements VehiculoUseCase {
    private final VehiculoRepositoryPort vehiculoRepositoryPort;

    @Override
    public List<Vehiculo> listar() {
        return vehiculoRepositoryPort.listar();
    }

    @Override
    public Optional<Vehiculo> buscarPorId(Long id) {
        return vehiculoRepositoryPort.buscarPorId(id);
    }

    @Override
    public Vehiculo guardarVehiculo(Vehiculo vehiculo) {
        if (vehiculo.getVehiculoId() != null) {
            throw new IllegalArgumentException("El ID se genera automáticamente al crear el vehículo.");
        }
        vehiculo.validarDatos();
        validarPlaca(vehiculo.getPlaca(), null);
        return vehiculoRepositoryPort.guardarVehiculo(vehiculo);
    }

    @Override
    public Optional<Vehiculo> actualizar(Long id, Vehiculo vehiculo) {
        return vehiculoRepositoryPort.buscarPorId(id).map(existente -> {
            vehiculo.validarDatos();
            existente.actualizarKilometraje(vehiculo.getKilometrajeActual());
            existente.setEstado(vehiculo.getEstado());
            existente.actualizarFichaTecnica(vehiculo.getFichaTecnica());
            return vehiculoRepositoryPort.guardarVehiculo(existente);
        });
    }

    @Override
    public boolean borrarVehiculo(Long id) {
        if (vehiculoRepositoryPort.buscarPorId(id).isEmpty()) {
            return false;
        }
        vehiculoRepositoryPort.borrarVehiculo(id);
        return true;
    }

    private void validarPlaca(String placa, Long id) {
        if (vehiculoRepositoryPort.existePorPlaca(placa, id)) {
            throw new IllegalStateException("Ya existe un vehículo con esa placa.");
        }
    }
    
}
