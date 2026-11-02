package org.lubrimax.com.infrastructure.adapter.out.persistence.repository;

import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.OrdenDeServicioEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface OrdenDeServicioCrudRepository extends CrudRepository<OrdenDeServicioEntity, Long> {
    List<OrdenDeServicioEntity> findAll();
}
