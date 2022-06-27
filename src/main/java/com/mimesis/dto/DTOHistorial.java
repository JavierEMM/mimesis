package com.mimesis.dto;

import com.mimesis.entity.Funcion;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DTOHistorial {

    Integer getIdfuncion();
    Integer getCantidad();
    String getCostototal();
    Integer getEstado();

}
