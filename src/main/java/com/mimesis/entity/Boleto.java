package com.mimesis.entity;

import javax.persistence.*;

@Entity
@Table(name = "boleto")
public class Boleto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idboleto", nullable = false)
    private Integer id;

    @Column(name = "estado", nullable = false, length = 45)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idfuncion", nullable = false)
    private Funcion idfuncion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario idusuario;

    @Column(name = "codigoaleatorio", nullable = false, length = 45)
    private String codigoaleatorio;

    public String getCodigoaleatorio() {
        return codigoaleatorio;
    }

    public void setCodigoaleatorio(String codigoaleatorio) {
        this.codigoaleatorio = codigoaleatorio;
    }

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}