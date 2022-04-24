package com.mimesis.entity;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "sede")
public class Sede {
    @Id
    @Column(name = "idsede", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @OneToMany(mappedBy = "sedeIdsede")
    private Set<Sala> salas = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idsede")
    private Set<Funcion> funcions = new LinkedHashSet<>();

    public Set<Funcion> getFuncions() {
        return funcions;
    }

    public void setFuncions(Set<Funcion> funcions) {
        this.funcions = funcions;
    }

    public Set<Sala> getSalas() {
        return salas;
    }

    public void setSalas(Set<Sala> salas) {
        this.salas = salas;
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

    //TODO Reverse Engineering! Migrate other columns to the entity
}