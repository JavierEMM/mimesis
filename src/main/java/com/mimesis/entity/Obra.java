package com.mimesis.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "obras")
public class Obra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idobras", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "restricciondeedad", nullable = false)
    @NotBlank
    private Integer restricciondeedad;

    @Column(name = "valido", nullable = false)
    private Integer valido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "genero_idgenero", nullable = false)
    private Genero generoIdgenero;

    public Genero getGeneroIdgenero() {
        return generoIdgenero;
    }

    public void setGeneroIdgenero(Genero generoIdgenero) {
        this.generoIdgenero = generoIdgenero;
    }

    public Integer getValido() {
        return valido;
    }

    public void setValido(Integer valido) {
        this.valido = valido;
    }

    public Integer getRestricciondeedad() {
        return restricciondeedad;
    }

    public void setRestricciondeedad(Integer restricciondeedad) {
        this.restricciondeedad = restricciondeedad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

}