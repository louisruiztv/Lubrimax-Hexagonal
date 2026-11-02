package org.lubrimax.com.application.port.in;

import org.lubrimax.com.domain.model.Autorizacion;
import org.lubrimax.com.domain.model.OrdenDeServicio;
import java.util.List;
import java.util.Optional;

public interface OrdenDeServicioUseCase {
    List<OrdenDeServicio> listar();
    Optional<OrdenDeServicio> buscarPorId(Long id);
    OrdenDeServicio guardarOrden(OrdenDeServicio orden);
    Optional<OrdenDeServicio> actualizar(Long id, OrdenDeServicio orden);
    boolean borrarOrden(Long id);
    Optional<Boolean> estaAutorizada(Long id);
    Optional<OrdenDeServicio> autorizar(Long id, Autorizacion autorizacion);
    Optional<OrdenDeServicio> sincronizarEjecucion(Long id, Long ejecucionId);
}
