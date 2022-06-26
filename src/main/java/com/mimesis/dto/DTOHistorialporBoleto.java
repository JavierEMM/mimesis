package com.mimesis.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DTOHistorialporBoleto {

    private Integer idObras;
    private Integer directorId;
    private String nombreObra;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer cantidad;
    private String nombreSede;
    private String nombreSala;
    private String estado;
    private String costoTotal;
    private Integer funcionId;
    private List<Integer> listadeIdBoleto = new ArrayList<Integer>();

    public DTOHistorialporBoleto(Integer idObras, Integer directorId, String nombreObra, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Integer cantidad, String nombreSede, String nombreSala, String estado, String costoTotal, Integer funcionId) {
        this.idObras = idObras;
        this.directorId = directorId;
        this.nombreObra = nombreObra;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.cantidad = cantidad;
        this.nombreSede = nombreSede;
        this.nombreSala = nombreSala;
        this.estado = estado;
        this.costoTotal = costoTotal;
        this.funcionId = funcionId;
    }

    public DTOHistorialporBoleto() {
    }

    public Integer getIdObras() {
        return idObras;
    }

    public void setIdObras(Integer idObras) {
        this.idObras = idObras;
    }

    public Integer getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Integer directorId) {
        this.directorId = directorId;
    }

    public String getNombreObra() {
        return nombreObra;
    }

    public void setNombreObra(String nombreObra) {
        this.nombreObra = nombreObra;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(String costoTotal) {
        this.costoTotal = costoTotal;
    }

    public Integer getFuncionId() {
        return funcionId;
    }

    public void setFuncionId(Integer funcionId) {
        this.funcionId = funcionId;
    }

    public List<Integer> getListadeIdBoleto() {
        return listadeIdBoleto;
    }

    public void setListadeIdBoleto(List<Integer> listadeIdBoleto) {
        this.listadeIdBoleto = listadeIdBoleto;
    }
}
