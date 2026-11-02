package org.lubrimax.com.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.ProductoRepositoryPort;
import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.mapper.ProductoPersistenceMapper;
import org.lubrimax.com.infrastructure.adapter.out.persistence.repository.ProductoCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class ProductoCrudAdapter implements ProductoRepositoryPort {
    private final ProductoCrudRepository productoCrudRepository;
    private final ProductoPersistenceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listar() {
        return productoCrudRepository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long id) {
        return productoCrudRepository.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional
    public Producto guardarProducto(Producto producto) {
        ProductoEntity entity = producto.getProductoId() == null ? new ProductoEntity()
                : productoCrudRepository.findById(producto.getProductoId())
                    .orElseThrow(() -> new IllegalStateException("El producto ya no existe."));
        mapper.updateEntity(producto, entity);
        return mapper.toModel(productoCrudRepository.save(entity));
    }

    @Override
    @Transactional
    public void borrarProducto(Long id) {
        productoCrudRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(String codigo, Long id) {
        return id == null ? productoCrudRepository.existsByCodigo(codigo)
                : productoCrudRepository.existsByCodigoAndProductoIdNot(codigo, id);
    }

}
