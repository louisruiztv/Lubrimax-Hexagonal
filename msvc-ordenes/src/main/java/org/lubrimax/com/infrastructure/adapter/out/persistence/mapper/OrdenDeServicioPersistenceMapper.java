package org.lubrimax.com.infrastructure.adapter.out.persistence.mapper;

import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.*;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class OrdenDeServicioPersistenceMapper {
    // Preserve managed child identities and ordered collections.
    public void updateEntity(OrdenDeServicio orden, OrdenDeServicioEntity entity) {
        entity.setVehiculoId(orden.getVehiculoId());
        entity.setKilometraje(orden.getKilometraje());
        entity.setMantenimientoSolicitado(orden.getMantenimientoSolicitado());
        entity.setEstado(orden.getEstado());
        entity.setFecha(orden.getFecha());
        entity.getServicioIds().clear();
        entity.getServicioIds().addAll(orden.getServicioIds());
        DatosDelCliente datos = orden.getDatosCliente();
        entity.setDatosCliente(new DatosDelClienteEmbeddable(datos.nombre(), datos.telefono(), datos.documento()));

        PropuestaTecnica propuesta = orden.getPropuestaTecnica();
        if (propuesta == null) {
            entity.setPropuestaTecnica(null);
        } else {
            if (entity.getPropuestaTecnica() == null) {
                entity.setPropuestaTecnica(new PropuestaTecnicaEntity());
            }
            PropuestaTecnicaEntity interna = entity.getPropuestaTecnica();
            interna.setEstado(propuesta.getEstado());
            interna.setObservaciones(propuesta.getObservaciones());
            interna.getServicioIds().clear();
            interna.getServicioIds().addAll(propuesta.getServicioIds());
        }
        Autorizacion autorizacion = orden.getAutorizacion();
        if (autorizacion == null) {
            entity.setAutorizacion(null);
        } else {
            if (entity.getAutorizacion() == null) {
                entity.setAutorizacion(new AutorizacionEntity());
            }
            AutorizacionEntity interna = entity.getAutorizacion();
            interna.setMedio(autorizacion.getMedio());
            interna.setAlcance(autorizacion.getAlcance());
            interna.setFecha(autorizacion.getFecha());
            interna.getServicioIds().clear();
            interna.getServicioIds().addAll(autorizacion.getServicioIds());
        }
    }

    public OrdenDeServicio toModel(OrdenDeServicioEntity entity) {
        DatosDelClienteEmbeddable datos = entity.getDatosCliente();
        PropuestaTecnicaEntity propuesta = entity.getPropuestaTecnica();
        AutorizacionEntity autorizacion = entity.getAutorizacion();
        return new OrdenDeServicio(entity.getOrdenId(), entity.getVehiculoId(), entity.getKilometraje(),
                entity.getMantenimientoSolicitado(), entity.getEstado(), entity.getFecha(),
                List.copyOf(entity.getServicioIds()),
                new DatosDelCliente(datos.getNombre(), datos.getTelefono(), datos.getDocumento()),
                propuesta == null ? null : new PropuestaTecnica(propuesta.getPropuestaId(),
                        propuesta.getEstado(), propuesta.getObservaciones(), List.copyOf(propuesta.getServicioIds())),
                autorizacion == null ? null : new Autorizacion(autorizacion.getAutorizacionId(),
                        autorizacion.getMedio(), autorizacion.getAlcance(), autorizacion.getFecha(),
                        List.copyOf(autorizacion.getServicioIds())));
    }
}
