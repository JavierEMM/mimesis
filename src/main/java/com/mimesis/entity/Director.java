package com.mimesis.entity;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.*;
import java.io.Serializable;

@Entity
@Table(name = "director")
public class Director implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddirector", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 45)
    @NotBlank(message = "Ingrese su nombre")
    @Size(max = 255, message = "El nombre del Director no puede ser mayor a 255 caracteres")
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 45)
    @NotBlank(message = "Ingrese su apellido")
    @Size(max = 255, message = "El apellido del Director no puede ser mayor a 255 caracteres")
    private String apellido;

    @Column(name = "correo", nullable = false, length = 45)
    @Email(message = "Debe ser un correo valido")
    @NotBlank(message = "Ingrese su correo")
    @Size(max = 255, message = "El correo no puedo contener más de a 255 caracteres")
    private String correo;

    @Column(name = "telefono", nullable = false)
    @Digits(integer = 9, fraction=0)
    @Max(value=9)
    @Min(value=8)
    @NotNull(message = "Debe agregar un numero de contacto")
    private Integer telefono;

    @Column(name = "valido", nullable = false)
    private Boolean valido = true;

    @Column(name = "foto", nullable = false)
    private byte[] foto;

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }
    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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