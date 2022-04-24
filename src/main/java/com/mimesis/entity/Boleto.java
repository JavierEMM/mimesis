package com.mimesis.entity;

import com.mimesis.entity.Usuario;

import javax.persistence.*;

@Entity
@Table(name = "boleto")
public class Boleto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idboleto", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_idusuario")
    private Usuario usuarioIdusuario;

    @Column(name = "costo", nullable = false)
    private Double costo;

    @Column(name = "estado", nullable = false, length = 45)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "funcion_idfuncion", nullable = false)
    private Funcion funcionIdfuncion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala_idsala", nullable = false)
    private Sala salaIdsala;

    @Column(name = "codigoaleatorio", nullable = false, length = 45)
    private String codigoaleatorio;

    public String getCodigoaleatorio() {
        return codigoaleatorio;
    }

    public void setCodigoaleatorio(String codigoaleatorio) {
        this.codigoaleatorio = codigoaleatorio;
    }

    public Sala getSalaIdsala() {
        return salaIdsala;
    }

    public void setSalaIdsala(Sala salaIdsala) {
        this.salaIdsala = salaIdsala;
    }

    public Funcion getFuncionIdfuncion() {
        return funcionIdfuncion;
    }

    public void setFuncionIdfuncion(Funcion funcionIdfuncion) {
        this.funcionIdfuncion = funcionIdfuncion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
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