package com.mimesis.entity;

import org.aspectj.bridge.Message;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "obras")
public class Obra implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idobras", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    @NotBlank(message="Debe ingresar un nombre")
    private String nombre;

    @Column(name = "descripcion", nullable = false)
    @NotBlank(message="Debe ingresar una descripción")
    private String descripcion;

    @Column(name = "restricciondeedad", nullable = false)
    @Min(value = 0,message = "Debe escoger una restricción de edad")
    private Integer restricciondeedad;


    @Column(name = "valido", nullable = false)
    private Boolean valido;



    @OneToMany(mappedBy = "idobras")
    private List<Foto> fotosporobra;


    @ManyToOne(optional = false)
    @JoinColumn(name = "genero_idgenero", nullable = false)
    @NotNull(message = "Debe seleccionar un género")
    private Genero generoIdgenero;

    public Genero getGeneroIdgenero() {
        return generoIdgenero;
    }

    public void setGeneroIdgenero(Genero generoIdgenero) {
        this.generoIdgenero = generoIdgenero;
    }

    public Boolean getValido() {return valido;}
    public void setValido(Boolean valido) {this.valido = valido;}

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

    public List<Foto> getFotosporobra() {
        return fotosporobra;
    }

    public void setFotosporobra(List<Foto> fotosporobra) {
        this.fotosporobra = fotosporobra;
    }

}