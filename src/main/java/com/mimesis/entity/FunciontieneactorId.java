package com.mimesis.entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FunciontieneactorId implements Serializable {
    private static final long serialVersionUID = 4330715234819000669L;
    @Column(name = "funcion_idfuncion", nullable = false)
    private Integer funcionIdfuncion;
    @Column(name = "actor_idactor", nullable = false)
    private Integer actorIdactor;

    public Integer getActorIdactor() {
        return actorIdactor;
    }

    public void setActorIdactor(Integer actorIdactor) {
        this.actorIdactor = actorIdactor;
    }

    public Integer getFuncionIdfuncion() {
        return funcionIdfuncion;
    }

    public void setFuncionIdfuncion(Integer funcionIdfuncion) {
        this.funcionIdfuncion = funcionIdfuncion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(funcionIdfuncion, actorIdactor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FunciontieneactorId entity = (FunciontieneactorId) o;
        return Objects.equals(this.funcionIdfuncion, entity.funcionIdfuncion) &&
                Objects.equals(this.actorIdactor, entity.actorIdactor);
    }
}