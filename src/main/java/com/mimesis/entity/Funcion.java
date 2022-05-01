package com.mimesis.entity;

import com.mimesis.entity.Director;
import com.mimesis.entity.Sala;
import com.mimesis.entity.Sede;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "funcion")
public class Funcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idfuncion", nullable = false)
    private Integer id;

    @ManyToMany
    @JoinTable(name="funciontieneactor",
    joinColumns = @JoinColumn(name = "idfuncion"),
    inverseJoinColumns = @JoinColumn(name = "idactor"))
    private List<Actor> actoresPorFuncion;

    @Column(name = "valido", nullable = false)
    private Boolean valido = true;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "genero", nullable = false, length = 45)
    private String genero;

    @Column(name = "restricciondeedad", nullable = false, length = 45)
    private String restricciondeedad;

    @Column(name = "fecha", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @Column(name = "aforo", nullable = false)
    private Integer aforo;

    @Column(name = "horainicio", nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horainicio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iddirector", nullable = false)
    private Director iddirector;

    @Column(name = "horafin", nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horafin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idsede", nullable = false)
    private Sede idsede;

    @Column(name = "costo", nullable = false, length = 45)
    private String costo;

    @Column(name = "descripcion", nullable = false, length = 45)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idsala", nullable = false)
    private Sala idsala;

    public List<Actor> getActoresPorFuncion() {
        return actoresPorFuncion;
    }

    public void setActoresPorFuncion(List<Actor> actoresPorFuncion) {
        this.actoresPorFuncion = actoresPorFuncion;
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


    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public Sede getIdsede() {
        return idsede;
    }

    public void setIdsede(Sede idsede) {
        this.idsede = idsede;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}