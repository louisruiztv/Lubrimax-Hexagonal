package org.lubrimax.com.infrastructure.adapter.out.persistence.mapper;

import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.*;
import org.springframework.stereotype.Component;

@Component
public class VehiculoPersistenceMapper {
    public Vehiculo toModel(VehiculoEntity entity) {
        FichaTecnicaEntity ficha = entity.getFichaTecnica();
        return new Vehiculo(entity.getVehiculoId(), entity.getPlaca(), entity.getMarca(),
                entity.getModelo(), entity.getAnio(), entity.getTipoCombustible(),
                entity.getKilometrajeActual(), entity.getEstado(),
                new FichaTecnica(ficha.getMarca(), ficha.getModelo(), ficha.getAnio(),
                        ficha.getMotor(), ficha.getCilindrada(), ficha.getTipoCombustible()));
    }

    public VehiculoEntity toEntity(Vehiculo vehiculo) {
        FichaTecnica ficha = vehiculo.getFichaTecnica();
        return new VehiculoEntity(vehiculo.getVehiculoId(), vehiculo.getPlaca(), vehiculo.getMarca(),
                vehiculo.getModelo(), vehiculo.getAnio(), vehiculo.getTipoCombustible(),
                vehiculo.getKilometrajeActual(), vehiculo.getEstado(),
                new FichaTecnicaEntity(ficha.getMarca(), ficha.getModelo(), ficha.getAnio(),
                        ficha.getMotor(), ficha.getCilindrada(), ficha.getTipoCombustible()));
    }
}
