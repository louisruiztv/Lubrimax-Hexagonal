package org.lubrimax.com.application.port.in;

import org.lubrimax.com.domain.model.Vehiculo;
import java.util.List;
import java.util.Optional;

public interface VehiculoUseCase {
    List<Vehiculo> listar();
    Optional<Vehiculo> buscarPorId(Long id);
    Vehiculo guardarVehiculo(Vehiculo vehiculo);
    Optional<Vehiculo> actualizar(Long id, Vehiculo vehiculo);
    boolean borrarVehiculo(Long id);
}
