package org.lubrimax.com.infrastructure.adapter.out.persistence.repository;

import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.RequerimientoDeMantenimientoEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface RequerimientoDeMantenimientoCrudRepository extends CrudRepository<RequerimientoDeMantenimientoEntity, Long> {
    List<RequerimientoDeMantenimientoEntity> findAll();
}
