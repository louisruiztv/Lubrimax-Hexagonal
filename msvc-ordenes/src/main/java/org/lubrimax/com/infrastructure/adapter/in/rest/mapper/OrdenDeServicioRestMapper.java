package org.lubrimax.com.infrastructure.adapter.in.rest.mapper;

import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.in.rest.dto.*;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrdenDeServicioRestMapper {
    public OrdenDeServicio toDomain(OrdenDeServicioDto dto) {
        var datos = dto.getDatosCliente(); var propuesta = dto.getPropuestaTecnica();
        return new OrdenDeServicio(dto.getOrdenId(), dto.getVehiculoId(), dto.getKilometraje(), dto.getMantenimientoSolicitado(),
                dto.getEstado(), dto.getFecha(), copy(dto.getServicioIds()),
                datos == null ? null : new DatosDelCliente(datos.nombre(), datos.telefono(), datos.documento()),
                propuesta == null ? null : new PropuestaTecnica(propuesta.getPropuestaId(), propuesta.getEstado(), propuesta.getObservaciones(), copy(propuesta.getServicioIds())),
                toDomain(dto.getAutorizacion()));
    }

    public Autorizacion toDomain(AutorizacionDto dto) {
        return dto == null ? null : new Autorizacion(dto.getAutorizacionId(), dto.getMedio(), dto.getAlcance(), dto.getFecha(), copy(dto.getServicioIds()));
    }

    public OrdenDeServicioDto toDto(OrdenDeServicio model) {
        var datos = model.getDatosCliente(); var propuesta = model.getPropuestaTecnica(); var autorizacion = model.getAutorizacion();
        return new OrdenDeServicioDto(model.getOrdenId(), model.getVehiculoId(), model.getKilometraje(), model.getMantenimientoSolicitado(),
                model.getEstado(), model.getFecha(), copy(model.getServicioIds()),
                datos == null ? null : new DatosDelClienteDto(datos.nombre(), datos.telefono(), datos.documento()),
                propuesta == null ? null : new PropuestaTecnicaDto(propuesta.getPropuestaId(), propuesta.getEstado(), propuesta.getObservaciones(), copy(propuesta.getServicioIds())),
                autorizacion == null ? null : new AutorizacionDto(autorizacion.getAutorizacionId(), autorizacion.getMedio(), autorizacion.getAlcance(), autorizacion.getFecha(), copy(autorizacion.getServicioIds())));
    }

    // Copy without rejecting null entries: the original domain validation owns that decision.
    private List<Long> copy(List<Long> ids) {
        return ids == null ? null : new ArrayList<>(ids);
    }
}
