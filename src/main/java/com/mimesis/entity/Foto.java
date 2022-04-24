package com.mimesis.entity;

import javax.persistence.*;

@Entity
@Table(name = "fotos")
public class Foto {
    @Id
    @Column(name = "idfotos", nullable = false)
    private Integer id;

    @Column(name = "foto", nullable = false)
    private byte[] foto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcion_idfuncion")
    private Funcion funcionIdfuncion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sede_idsede")
    private Sede sedeIdsede;

    public Sede getSedeIdsede() {
        return sedeIdsede;
    }

    public void setSedeIdsede(Sede sedeIdsede) {
        this.sedeIdsede = sedeIdsede;
    }

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