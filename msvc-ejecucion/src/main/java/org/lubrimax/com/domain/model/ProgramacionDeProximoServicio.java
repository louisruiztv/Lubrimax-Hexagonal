package org.lubrimax.com.domain.model;

import java.time.LocalDate;

public record ProgramacionDeProximoServicio(LocalDate fechaSugerida, Integer kilometrajeSugerido) {
}
