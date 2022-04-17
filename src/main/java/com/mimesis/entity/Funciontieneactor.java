package com.mimesis.entity;

import javax.persistence.*;

@Entity
@Table(name = "funciontieneactor")
public class Funciontieneactor {
    @EmbeddedId
    private FunciontieneactorId id;

    @MapsId("funcionIdfuncion")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "funcion_idfuncion", nullable = false)
    private Funcion funcionIdfuncion;

    @MapsId("actorIdactor")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "actor_idactor", nullable = false)
    private Actor actorIdactor;

    public Actor getActorIdactor() {
        return actorIdactor;
    }

    public void setActorIdactor(Actor actorIdactor) {
        this.actorIdactor = actorIdactor;
    }

    public Funcion getFuncionIdfuncion() {
        return funcionIdfuncion;
    }

    public void setFuncionIdfuncion(Funcion funcionIdfuncion) {
        this.funcionIdfuncion = funcionIdfuncion;
    }

    public FunciontieneactorId getId() {
        return id;
    }

    public void setId(FunciontieneactorId id) {
        this.id = id;
    }
}