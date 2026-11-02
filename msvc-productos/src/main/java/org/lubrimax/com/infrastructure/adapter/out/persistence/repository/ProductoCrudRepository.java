package org.lubrimax.com.infrastructure.adapter.out.persistence.repository;

import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ProductoCrudRepository extends CrudRepository<ProductoEntity, Long> {
    List<ProductoEntity> findAll();
    boolean existsByCodigo(String codigo);
    boolean existsByCodigoAndProductoIdNot(String codigo, Long productoId);
}
