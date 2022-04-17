package com.mimesis.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "funcion")
public class Funcion {
    @Id
    @Column(name = "idfuncion", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_iddirector1")
    private Director directorIddirector1;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "`duración`", nullable = false)
    private Double duración;

    @Column(name = "genero", nullable = false, length = 45)
    private String genero;

    @Column(name = "restricciondeedad", nullable = false, length = 45)
    private String restricciondeedad;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "aforo", nullable = false)
    private Integer aforo;

    @Column(name = "horainicio", nullable = false)
    private LocalTime horainicio;

    @Column(name = "horafin", nullable = false)
    private LocalTime horafin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sede_idsede", nullable = false)
    private Sede sedeIdsede;

    @OneToMany(mappedBy = "funcionIdfuncion")
    private Set<Calificacione> calificaciones = new LinkedHashSet<>();

    @OneToMany(mappedBy = "funcionIdfuncion")
    private Set<Foto> fotos = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "funcions")
    private Set<Actor> actors = new LinkedHashSet<>();

    @OneToMany(mappedBy = "funcionIdfuncion")
    private Set<Boleto> boletos = new LinkedHashSet<>();

    public Set<Boleto> getBoletos() {
        return boletos;
    }

    public void setBoletos(Set<Boleto> boletos) {
        this.boletos = boletos;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public Set<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(Set<Foto> fotos) {
        this.fotos = fotos;
    }

    public Set<Calificacione> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(Set<Calificacione> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public Sede getSedeIdsede() {
        return sedeIdsede;
    }

    public void setSedeIdsede(Sede sedeIdsede) {
        this.sedeIdsede = sedeIdsede;
    }

    public LocalTime getHorafin() {
        return horafin;
    }

    public void setHorafin(LocalTime horafin) {
        this.horafin = horafin;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getRestricciondeedad() {
        return restricciondeedad;
    }

    public void setRestricciondeedad(String restricciondeedad) {
        this.restricciondeedad = restricciondeedad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Double getDuración() {
        return duración;
    }

    public void setDuración(Double duración) {
        this.duración = duración;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Director getDirectorIddirector1() {
        return directorIddirector1;
    }

    public void setDirectorIddirector1(Director directorIddirector1) {
        this.directorIddirector1 = directorIddirector1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}