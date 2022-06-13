package com.mimesis.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DTOBoletosValidos {
    Integer getIdfuncion();
    LocalDate getFecha();
    Integer getAforo();
    LocalTime getHorainicio();
    Integer getIddirector();
    LocalTime getHorafin();
    Double getCosto();
    Integer getIdsala();
    Boolean getValido();
    Integer getIdobras();
    Integer getBoletos();
}
