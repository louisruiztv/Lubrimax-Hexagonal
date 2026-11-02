package org.lubrimax.com.application.port.out;

import org.lubrimax.com.domain.model.Vehiculo;
import java.util.List;
import java.util.Optional;

public interface VehiculoRepositoryPort {
    List<Vehiculo> listar();
    Optional<Vehiculo> buscarPorId(Long id);
    Vehiculo guardarVehiculo(Vehiculo vehiculo);
    void borrarVehiculo(Long id);
    boolean existePorPlaca(String placa, Long vehiculoId);
}
