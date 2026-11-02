package org.lubrimax.com.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.RequerimientoDeMantenimientoRepositoryPort;
import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.repository.RequerimientoDeMantenimientoCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.mapper.RequerimientoDeMantenimientoPersistenceMapper;

@Repository
@RequiredArgsConstructor
public class RequerimientoDeMantenimientoCrudAdapter implements RequerimientoDeMantenimientoRepositoryPort {
    private final RequerimientoDeMantenimientoCrudRepository repository;
    private final RequerimientoDeMantenimientoPersistenceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<RequerimientoDeMantenimiento> listar() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RequerimientoDeMantenimiento> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional
    public RequerimientoDeMantenimiento guardarRequerimiento(RequerimientoDeMantenimiento requerimiento) {
        RequerimientoDeMantenimientoEntity entity = requerimiento.getRequerimientoId() == null
                ? new RequerimientoDeMantenimientoEntity()
                : repository.findById(requerimiento.getRequerimientoId())
                    .orElseThrow(() -> new IllegalStateException("El requerimiento ya no existe."));
        mapper.updateEntity(requerimiento, entity);
        return mapper.toModel(repository.save(entity));
    }

    @Override
    @Transactional
    public void borrarRequerimiento(Long id) {
        repository.deleteById(id);
    }

}
