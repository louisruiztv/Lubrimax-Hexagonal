package org.lubrimax.com.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.OrdenDeServicioRepositoryPort;
import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.repository.OrdenDeServicioCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.lubrimax.com.infrastructure.adapter.out.persistence.mapper.OrdenDeServicioPersistenceMapper;

@Repository
@RequiredArgsConstructor
public class OrdenDeServicioCrudAdapter implements OrdenDeServicioRepositoryPort {
    private final OrdenDeServicioCrudRepository repository;
    private final OrdenDeServicioPersistenceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<OrdenDeServicio> listar() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenDeServicio> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional
    public OrdenDeServicio guardarOrden(OrdenDeServicio orden) {
        OrdenDeServicioEntity entity = orden.getOrdenId() == null ? new OrdenDeServicioEntity()
                : repository.findById(orden.getOrdenId())
                    .orElseThrow(() -> new IllegalStateException("La orden ya no existe."));
        mapper.updateEntity(orden, entity);
        return mapper.toModel(repository.save(entity));
    }

    @Override
    @Transactional
    public void borrarOrden(Long id) {
        repository.deleteById(id);
    }

}
