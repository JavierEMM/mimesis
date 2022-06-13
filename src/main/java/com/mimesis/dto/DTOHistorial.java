package com.mimesis.dto;

import java.time.LocalDate;
import com.mimesis.entity.*;

import java.time.LocalTime;
import java.util.Date;

public class DTOHistorial {


    private String  nombrefuncion;
    private LocalTime horainicio;
    private Integer cantTickets;
    private String nombresede;

    private Integer estado;


    public String getNombrefuncion() {
        return nombrefuncion;
    }

    public void setNombrefuncion(String nombrefuncion) {
        this.nombrefuncion = nombrefuncion;
    }

    public LocalTime getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(LocalTime horainicio) {
        this.horainicio = horainicio;
    }

    public Integer getCantTickets() {
        return cantTickets;
    }

    public void setCantTickets(Integer cantTickets) {
        this.cantTickets = cantTickets;
    }

    public String getNombresede() {
        return nombresede;
    }

    public void setNombresede(String nombresede) {
        this.nombresede = nombresede;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
