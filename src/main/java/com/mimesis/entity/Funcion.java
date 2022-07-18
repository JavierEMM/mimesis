package com.mimesis.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "funcion")
public class Funcion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idfuncion", nullable = false)
    private int id;

    @Column(name = "aforo", nullable = false)
    @NotNull(message = "Debe ingresar un valor")
    private Integer aforo;

    @Column(name = "horainicio", nullable = false)
    @NotNull(message = "Debe ingresar una hora válida")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horainicio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iddirector", nullable = false)
    @NotNull(message = "Debe escoger un director")
    private Director iddirector;

    @Column(name = "horafin", nullable = false)
    @NotNull(message = "Debe ingresar una hora válida")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horafin;

    @Column(name = "costo", nullable = false)
    @Min(value =0 ,message = "El valor debe ser mayor que 0")
    @Max(value = 500,message = "El valor debe ser menor a 500")
    @NotNull(message = "Debe ingresar un valor")
    private Double costo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idsala", nullable = false)
    @NotNull(message = "Debe seleccionar una sala")
    private Sala idsala;

    @Column(name = "valido", nullable = false)
    private Boolean valido;



    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "obras_idobras", nullable = false)
    @NotNull(message = "Debe seleccionar una obra")
    private Obra idobra;

    @Column(name = "fecha", nullable = false)
    @NotNull(message = "Debe ingresar una fecha válida")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @ManyToMany
    @JoinTable(name = "funciontieneactor",joinColumns = @JoinColumn(name = "idfuncion"),
    inverseJoinColumns = @JoinColumn(name = "idactor"))
    private List<Actor> actors;

    public void setId(int id) {
        this.id = id;
    }

    public Obra getIdobra() {
        return idobra;
    }

    public void setIdobra(Obra idobra) {
        this.idobra = idobra;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }

    public Sala getIdsala() {
        return idsala;
    }

    public void setIdsala(Sala idsala) {
        this.idsala = idsala;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public LocalTime getHorafin() {
        return horafin;
    }

    public void setHorafin(LocalTime horafin) {
        this.horafin = horafin;
    }

    public Director getIddirector() {
        return iddirector;
    }

    public void setIddirector(Director iddirector) {
        this.iddirector = iddirector;
    }

    public LocalTime getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(LocalTime horainicio) {
        this.horainicio = horainicio;
    }

    public Integer getAforo() {
        return aforo;
    }

    public void setAforo(Integer aforo) {
        this.aforo = aforo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}