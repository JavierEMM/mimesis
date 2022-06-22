package com.mimesis.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DTOhistorialbackup {

    Integer getIdboleto();
    String getNombreobra();
    LocalDate getFecha();
    LocalTime getHorainicio();
    LocalTime getHorafin();
    Integer getCantidad();
    String getNombresede();
    String getNombresala();
    String getEstado();
    String getCostototal();
    Integer getFuncionid();
    Integer getCuentacal();
}
