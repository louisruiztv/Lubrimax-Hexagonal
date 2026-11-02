package org.lubrimax.com.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.VehiculoRepositoryPort;
import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.mapper.VehiculoPersistenceMapper;
import org.lubrimax.com.infrastructure.adapter.out.persistence.repository.VehiculoCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VehiculoCrudAdapter implements VehiculoRepositoryPort {
    private final VehiculoCrudRepository vehiculoCrudRepository;
    private final VehiculoPersistenceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> listar() {
        return vehiculoCrudRepository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vehiculo> buscarPorId(Long id) {
        return vehiculoCrudRepository.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional
    public Vehiculo guardarVehiculo(Vehiculo vehiculo) {
        return mapper.toModel(vehiculoCrudRepository.save(mapper.toEntity(vehiculo)));
    }

    @Override
    @Transactional
    public void borrarVehiculo(Long id) {
        vehiculoCrudRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorPlaca(String placa, Long id) {
        return id == null ? vehiculoCrudRepository.existsByPlaca(placa)
                : vehiculoCrudRepository.existsByPlacaAndVehiculoIdNot(placa, id);
    }

}
