package com.mimesis.dto;

import com.mimesis.entity.Funcion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DTOHistorialporBoleto {

    private Funcion funcion;
    private Integer cantidad;
    private String costoTotal;
    private Boolean boletoValido;
    private Boolean validar;
    public DTOHistorialporBoleto() {
    }
    public String getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(String costoTotal) {
        this.costoTotal = costoTotal;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Boolean getValidar() {
        return validar;
    }

    public void setValidar(Boolean validar) {
        this.validar = validar;
    }

    public Boolean getBoletoValido() {
        return boletoValido;
    }

    public void setBoletoValido(Boolean boletoValido) {
        this.boletoValido = boletoValido;
    }
}
