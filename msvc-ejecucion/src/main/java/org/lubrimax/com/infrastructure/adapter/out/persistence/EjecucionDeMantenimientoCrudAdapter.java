package org.lubrimax.com.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.EjecucionDeMantenimientoRepositoryPort;
import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.repository.EjecucionDeMantenimientoCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.mapper.EjecucionDeMantenimientoPersistenceMapper;

@Repository
@RequiredArgsConstructor
public class EjecucionDeMantenimientoCrudAdapter implements EjecucionDeMantenimientoRepositoryPort {
    private final EjecucionDeMantenimientoCrudRepository repository;
    private final EjecucionDeMantenimientoPersistenceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<EjecucionDeMantenimiento> listar() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EjecucionDeMantenimiento> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional
    public EjecucionDeMantenimiento guardarEjecucion(EjecucionDeMantenimiento ejecucion) {
        var entity = ejecucion.getEjecucionId() == null ? new EjecucionDeMantenimientoEntity()
                : repository.findById(ejecucion.getEjecucionId())
                    .orElseThrow(() -> new IllegalStateException("La ejecución ya no existe."));
        mapper.updateEntity(ejecucion, entity);
        return mapper.toModel(repository.save(entity));
    }

    @Override
    @Transactional
    public void borrarEjecucion(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorOrdenId(Long ordenId, Long ejecucionId) {
        return ejecucionId == null ? repository.existsByOrdenId(ordenId)
                : repository.existsByOrdenIdAndEjecucionIdNot(ordenId, ejecucionId);
    }

}
