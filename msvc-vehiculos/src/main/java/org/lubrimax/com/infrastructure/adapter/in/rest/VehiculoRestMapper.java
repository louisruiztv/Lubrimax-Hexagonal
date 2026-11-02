package org.lubrimax.com.infrastructure.adapter.in.rest;

import org.lubrimax.com.domain.model.FichaTecnica;
import org.lubrimax.com.domain.model.Vehiculo;
import org.lubrimax.com.infrastructure.adapter.in.rest.dto.*;
import org.springframework.stereotype.Component;

@Component
public class VehiculoRestMapper {
    public Vehiculo toDomain(VehiculoDto dto) {
        FichaTecnicaDto ficha = dto.getFichaTecnica();
        return new Vehiculo(dto.getVehiculoId(), dto.getPlaca(), dto.getMarca(), dto.getModelo(),
                dto.getAnio(), dto.getTipoCombustible(), dto.getKilometrajeActual(), dto.getEstado(),
                ficha == null ? null : new FichaTecnica(ficha.getMarca(), ficha.getModelo(), ficha.getAnio(),
                        ficha.getMotor(), ficha.getCilindrada(), ficha.getTipoCombustible()));
    }

    public VehiculoDto toDto(Vehiculo model) {
        FichaTecnica ficha = model.getFichaTecnica();
        return new VehiculoDto(model.getVehiculoId(), model.getPlaca(), model.getMarca(), model.getModelo(),
                model.getAnio(), model.getTipoCombustible(), model.getKilometrajeActual(), model.getEstado(),
                ficha == null ? null : new FichaTecnicaDto(ficha.getMarca(), ficha.getModelo(), ficha.getAnio(),
                        ficha.getMotor(), ficha.getCilindrada(), ficha.getTipoCombustible()));
    }
}
