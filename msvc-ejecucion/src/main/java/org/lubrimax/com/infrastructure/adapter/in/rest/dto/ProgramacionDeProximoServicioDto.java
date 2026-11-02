package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import java.time.LocalDate;

public record ProgramacionDeProximoServicioDto(LocalDate fechaSugerida, Integer kilometrajeSugerido) {
}
