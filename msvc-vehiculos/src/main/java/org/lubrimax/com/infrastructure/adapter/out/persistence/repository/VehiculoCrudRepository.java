package org.lubrimax.com.infrastructure.adapter.out.persistence.repository;

import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.VehiculoEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface VehiculoCrudRepository extends CrudRepository<VehiculoEntity, Long> {
    List<VehiculoEntity> findAll();
    boolean existsByPlaca(String placa);
    boolean existsByPlacaAndVehiculoIdNot(String placa, Long vehiculoId);
}
