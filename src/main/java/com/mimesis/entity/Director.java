package com.mimesis.entity;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "director")
public class Director {
    @Id
    @Column(name = "iddirector", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 45)
    private String apellido;

    @OneToMany(mappedBy = "directorIddirector")
    private Set<Calificacione> calificaciones = new LinkedHashSet<>();

    @OneToMany(mappedBy = "directorIddirector1")
    private Set<Funcion> funcions = new LinkedHashSet<>();

    public Set<Funcion> getFuncions() {
        return funcions;
    }

    public void setFuncions(Set<Funcion> funcions) {
        this.funcions = funcions;
    }

    public Set<Calificacione> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(Set<Calificacione> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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