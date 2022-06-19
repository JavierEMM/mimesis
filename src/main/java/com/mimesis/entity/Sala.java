package com.mimesis.entity;



import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Entity
@Table(name = "sala")
public class Sala implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsala", nullable = false)
    private Integer id;

    @NotNull
    @Positive
    @Digits(integer = 10, fraction=0)
    @Max(value=32767)
    @Min(value=0)
    @Column(name = "aforo", nullable = false)
    private Integer aforo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idsede", nullable = false)
    private Sede idsede;

    @Column(name = "nombre", nullable = false, length = 45)
    @NotBlank
    @Size(max = 45, message = "El nombre de la sala no puede ser mayor a 45 caracteres")
    private String nombre;

    @Column(name = "valido", nullable = false)
    private Boolean valido = true;

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