package com.mimesis.entity;

import com.mimesis.entity.Director;
import com.mimesis.entity.Sala;
import com.mimesis.entity.Sede;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "funcion")
public class Funcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idfuncion", nullable = false)
    private int id;

    @ManyToMany
    @JoinTable(name="funciontieneactor",
    joinColumns = @JoinColumn(name = "idfuncion"),
    inverseJoinColumns = @JoinColumn(name = "idactor"))
    @NotNull(message = "Debe seleccionar al menos un actor por funcion")
    private List<Actor> actoresPorFuncion;

    @Column(name = "valido", nullable = false)
    private Boolean valido = true;

    @Column(name = "nombre", nullable = false, length = 255)
    @Size(max = 255,min=1,message = "Debe ingresar un nombre para la función")
    private String nombre;

    @Column(name = "genero", nullable = false, length = 255)
    @Size(max = 255,min=1,message = "Debe ingresar un genero para la función")
    private String genero;

    @Column(name = "restricciondeedad", nullable = false, length = 255)
    @Min(value = 0,message = "Debe escoger una restricción de edad")
    private Integer restricciondeedad;

    @Column(name = "fecha", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @Column(name = "aforo", nullable = false)
    @Min(value = 0,message = "Debe escoger un aforo mayor a 0")
    @NotNull(message = "Debe ingresar un valor")
    private Integer aforo;

    @Column(name = "horainicio", nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horainicio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iddirector", nullable = false)
    @NotNull(message = "Debe escoger un director")
    private Director iddirector;

    @Column(name = "horafin", nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horafin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idsede", nullable = false)
    @NotNull(message = "Debe seleccionar una sede")
    private Sede idsede;

    @Column(name = "costo", nullable = false, length = 45)
    @Min(value =0 ,message = "El valor debe ser mayor que 0")
    @Max(value = 500,message = "El valor debe ser menor a 500")
    @NotNull(message = "Debe ingresar un valor")
    private double costo;

    @Column(name = "descripcion", nullable = false, length = 45)
    @NotBlank(message = "Debe de escribir una descripcion")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idsala", nullable = false)
    @NotNull(message = "Debe seleccionar una sala")
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


    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
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

    public Integer getRestricciondeedad() {
        return restricciondeedad;
    }

    public void setRestricciondeedad(Integer restricciondeedad) {
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