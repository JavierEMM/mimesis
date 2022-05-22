package com.mimesis.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "sede")
public class Sede {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsede", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    @NotBlank
    @Size(max = 255, message = "El nombre de la Sede no puede ser mayor a 255 caracteres")
    private String nombre;

    @Column(name = "ubicacion", nullable = false)
    @NotBlank
    @Size(max = 255, message = "La dirección no puede ser contener mas 255 caracteres")
    private String ubicacion;

    @Column(name = "valido", nullable = false)
    private Boolean valido = true;

    @OneToMany(mappedBy = "idsede")
    private List<Foto> fotosporsede;

    @OneToMany(mappedBy = "idsede")
    private List<Sala> listasalas;

    public List<Sala> getListasalas() {
        return listasalas;
    }

    public void setListasalas(List<Sala> listasalas) {
        this.listasalas = listasalas;
    }

    public List<Foto> getFotosporsede() {
        return fotosporsede;
    }

    public void setFotosporsede(List<Foto> fotosporsede) {
        this.fotosporsede = fotosporsede;
    }

    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }
    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
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