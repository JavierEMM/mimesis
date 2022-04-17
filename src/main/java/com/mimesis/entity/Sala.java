package com.mimesis.entity;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "sala")
public class Sala {
    @Id
    @Column(name = "idsala", nullable = false)
    private Integer id;

    @Column(name = "aforo", nullable = false)
    private Integer aforo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sede_idsede", nullable = false)
    private Sede sedeIdsede;

    @OneToMany(mappedBy = "salaIdsala")
    private Set<Boleto> boletos = new LinkedHashSet<>();

    public Set<Boleto> getBoletos() {
        return boletos;
    }

    public void setBoletos(Set<Boleto> boletos) {
        this.boletos = boletos;
    }

    public Sede getSedeIdsede() {
        return sedeIdsede;
    }

    public void setSedeIdsede(Sede sedeIdsede) {
        this.sedeIdsede = sedeIdsede;
    }

    public Integer getAforo() {
        return aforo;
    }

    public void setAforo(Integer aforo) {
        this.aforo = aforo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //TODO Reverse Engineering! Migrate other columns to the entity
}