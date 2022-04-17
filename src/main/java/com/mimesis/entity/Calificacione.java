package com.mimesis.entity;

import javax.persistence.*;

@Entity
@Table(name = "calificaciones")
public class Calificacione {
    @Id
    @Column(name = "idcalificaciones", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_idusuario")
    private Usuario usuarioIdusuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_idactor")
    private Actor actorIdactor;

    @Column(name = "calificacion", nullable = false)
    private Integer calificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_iddirector")
    private Director directorIddirector;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "funcion_idfuncion", nullable = false)
    private Funcion funcionIdfuncion;

    public Funcion getFuncionIdfuncion() {
        return funcionIdfuncion;
    }

    public void setFuncionIdfuncion(Funcion funcionIdfuncion) {
        this.funcionIdfuncion = funcionIdfuncion;
    }

    public Director getDirectorIddirector() {
        return directorIddirector;
    }

    public void setDirectorIddirector(Director directorIddirector) {
        this.directorIddirector = directorIddirector;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public Actor getActorIdactor() {
        return actorIdactor;
    }

    public void setActorIdactor(Actor actorIdactor) {
        this.actorIdactor = actorIdactor;
    }

    public Usuario getUsuarioIdusuario() {
        return usuarioIdusuario;
    }

    public void setUsuarioIdusuario(Usuario usuarioIdusuario) {
        this.usuarioIdusuario = usuarioIdusuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //TODO Reverse Engineering! Migrate other columns to the entity
}