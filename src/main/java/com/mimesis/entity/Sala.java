package com.mimesis.entity;



import javax.persistence.*;

@Entity
@Table(name = "sala")
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsala", nullable = false)
    private Integer id;

    @Column(name = "aforo", nullable = false)
    private Integer aforo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idsede", nullable = false)
    private Sede idsede;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "valido", nullable = false)
    private Boolean valido;

    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Sede getIdsede() {
        return idsede;
    }

    public void setIdsede(Sede idsede) {
        this.idsede = idsede;
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