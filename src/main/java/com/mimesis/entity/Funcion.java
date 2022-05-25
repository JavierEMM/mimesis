package com.mimesis.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "funcion")
public class Funcion {
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idsala", nullable = false)
    @NotNull(message = "Debe seleccionar una sala")
    private Sala idsala;

    @Column(name = "valido", nullable = false)
    private Boolean valido;

    @OneToMany(mappedBy = "idfuncion")
    private List<Foto> fotosporfuncion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "obras_idobras", nullable = false)
    @NotNull(message = "Debe seleccionar una obra")
    private Obra idobra;

    public List<Foto> getFotosporfuncion() {
        return fotosporfuncion;
    }

    public void setFotosporfuncion(List<Foto> fotosporfuncion) {
        this.fotosporfuncion = fotosporfuncion;
    }

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

    @ManyToMany
    @JoinTable(name = "funciontieneactor",
            joinColumns = @JoinColumn(name = "idfuncion"),
            inverseJoinColumns = @JoinColumn(name = "idactor"))
    private List<Actor> actors = new ArrayList<>();

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