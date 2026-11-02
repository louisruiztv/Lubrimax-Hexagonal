package org.lubrimax.com.application.service;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.ProductoRepositoryPort;
import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.application.port.in.ProductoUseCase;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProductoService implements ProductoUseCase {
    private final ProductoRepositoryPort productoRepositoryPort;

    @Override
    public List<Producto> listar() {
        return productoRepositoryPort.listar();
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepositoryPort.buscarPorId(id);
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        if (producto.getProductoId() != null) {
            throw new IllegalArgumentException("El ID del producto se genera automáticamente.");
        }
        producto.validarDatos(Set.of());
        validarCodigo(producto.getCodigo(), null);
        return productoRepositoryPort.guardarProducto(producto);
    }

    @Override
    public Optional<Producto> actualizar(Long id, Producto producto) {
        return productoRepositoryPort.buscarPorId(id).map(existente -> {
            Map<Long, PresentacionDeProducto> actuales = existente.getPresentaciones().stream()
                    .collect(Collectors.toMap(PresentacionDeProducto::getPresentacionId, p -> p));
            Set<Long> ids = actuales.keySet();
            producto.validarDatos(ids);
            validarCodigo(producto.getCodigo(), id);
            existente.setCodigo(producto.getCodigo());
            existente.setNombre(producto.getNombre());
            existente.setDescripcion(producto.getDescripcion());
            existente.setMarca(producto.getMarca());
            existente.setCategoria(producto.getCategoria());
            existente.setTipo(producto.getTipo());
            existente.setEstado(producto.getEstado());
            List<PresentacionDeProducto> recibidas = new ArrayList<>(producto.getPresentaciones());
            for (Long presentacionId : ids) {
                existente.eliminarPresentacion(presentacionId);
            }
            for (PresentacionDeProducto recibida : recibidas) {
                PresentacionDeProducto presentacion = recibida.getPresentacionId() == null
                        ? recibida : actuales.get(recibida.getPresentacionId());
                presentacion.actualizarDatos(recibida.getDescripcion(), recibida.getContenido(),
                        recibida.getUnidadMedida(), recibida.getEstado());
                existente.agregarPresentacion(presentacion);
            }
            return productoRepositoryPort.guardarProducto(existente);
        });
    }

    @Override
    public boolean borrarProducto(Long id) {
        if (productoRepositoryPort.buscarPorId(id).isEmpty()) {
            return false;
        }
        productoRepositoryPort.borrarProducto(id);
        return true;
    }

    private void validarCodigo(String codigo, Long id) {
        if (productoRepositoryPort.existePorCodigo(codigo, id)) {
            throw new IllegalStateException("Ya existe un producto con ese código.");
        }
    }

}
