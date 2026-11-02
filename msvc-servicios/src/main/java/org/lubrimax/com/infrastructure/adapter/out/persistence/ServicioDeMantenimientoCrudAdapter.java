package org.lubrimax.com.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.ServicioDeMantenimientoRepositoryPort;
import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.mapper.ServicioDeMantenimientoPersistenceMapper;
import org.lubrimax.com.infrastructure.adapter.out.persistence.repository.ServicioDeMantenimientoCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ServicioDeMantenimientoCrudAdapter implements ServicioDeMantenimientoRepositoryPort {
    private final ServicioDeMantenimientoCrudRepository servicioCrudRepository;
    private final ServicioDeMantenimientoPersistenceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ServicioDeMantenimiento> listar() {
        return servicioCrudRepository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServicioDeMantenimiento> buscarPorId(Long id) {
        return servicioCrudRepository.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional
    public ServicioDeMantenimiento guardarServicio(ServicioDeMantenimiento servicio) {
        ServicioDeMantenimientoEntity entity = servicio.getServicioId() == null
                ? new ServicioDeMantenimientoEntity()
                : servicioCrudRepository.findById(servicio.getServicioId())
                    .orElseThrow(() -> new IllegalStateException("El servicio ya no existe."));
        mapper.updateEntity(servicio, entity);
        return mapper.toModel(servicioCrudRepository.save(entity));
    }

    @Override
    @Transactional
    public void borrarServicio(Long id) {
        servicioCrudRepository.deleteById(id);
    }

}
