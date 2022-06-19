package com.mimesis.dto;

import com.mimesis.entity.Funcion;

public class DTOFuncionesDisponibles {
    private Integer boletos;
    private Funcion funcion;

    public Integer getBoletos() {
        return boletos;
    }

    public void setBoletos(Integer boletos) {
        this.boletos = boletos;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public DTOFuncionesDisponibles(Integer boletos, Funcion funcion) {
        this.boletos = boletos;
        this.funcion = funcion;
    }
}
