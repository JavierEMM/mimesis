package com.mimesis.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "calificaciones")
public class Calificacion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcalificaciones", nullable = false)
    private Integer id;

    @Column(name = "calificacion", nullable = false)
    private Integer calificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idactor")
    private Actor idactor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddirector")
    private Director iddirector;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idfuncion", nullable = false)
    private Funcion idfuncion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario idusuario;

    public Usuario getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Usuario idusuario) {
        this.idusuario = idusuario;
    }

    public Funcion getIdfuncion() {
        return idfuncion;
    }

    public void setIdfuncion(Funcion idfuncion) {
        this.idfuncion = idfuncion;
    }

    public Director getIddirector() {
        return iddirector;
    }

    public void setIddirector(Director iddirector) {
        this.iddirector = iddirector;
    }

    public Actor getIdactor() {
        return idactor;
    }

    public void setIdactor(Actor idactor) {
        this.idactor = idactor;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}