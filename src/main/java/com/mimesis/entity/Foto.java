package com.mimesis.entity;

import com.mimesis.entity.Funcion;

import javax.persistence.*;

@Entity
@Table(name = "fotos")
public class Foto {
    @Id
    @Column(name = "idfotos", nullable = false)
    private Integer id;

    @Column(name = "foto", nullable = false)
    private byte[] foto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "funcion_idfuncion", nullable = false)
    private Funcion funcionIdfuncion;

    public Funcion getFuncionIdfuncion() {
        return funcionIdfuncion;
    }

    public void setFuncionIdfuncion(Funcion funcionIdfuncion) {
        this.funcionIdfuncion = funcionIdfuncion;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}