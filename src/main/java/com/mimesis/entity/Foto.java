package com.mimesis.entity;

import javax.persistence.*;

@Entity
@Table(name = "fotos")
public class Foto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idfotos", nullable = false)
    private Integer id;

    @Column(name = "foto", nullable = false)
    private byte[] foto;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idsede")
    private Sede idsede;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idobras", nullable = false)
    private Obra idobras;

    public Obra getIdobras() {
        return idobras;
    }

    public void setIdobras(Obra idobras) {
        this.idobras = idobras;
    }


    public Sede getIdsede() {
        return idsede;
    }

    public void setIdsede(Sede idsede) {
        this.idsede = idsede;
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