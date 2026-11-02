package org.lubrimax.com.infrastructure.adapter.out.persistence.repository;

import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.ServicioDeMantenimientoEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ServicioDeMantenimientoCrudRepository extends CrudRepository<ServicioDeMantenimientoEntity, Long> {
    List<ServicioDeMantenimientoEntity> findAll();
}
