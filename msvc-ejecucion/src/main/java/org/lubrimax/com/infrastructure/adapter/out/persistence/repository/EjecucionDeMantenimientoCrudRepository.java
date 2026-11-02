package org.lubrimax.com.infrastructure.adapter.out.persistence.repository;

import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.EjecucionDeMantenimientoEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface EjecucionDeMantenimientoCrudRepository extends CrudRepository<EjecucionDeMantenimientoEntity, Long> {
    List<EjecucionDeMantenimientoEntity> findAll();
    boolean existsByOrdenId(Long ordenId);
    boolean existsByOrdenIdAndEjecucionIdNot(Long ordenId, Long ejecucionId);
}
