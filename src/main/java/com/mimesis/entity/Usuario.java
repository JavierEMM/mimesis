package com.mimesis.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario", nullable = false)
    private Integer id;


    @Column(name = "nombre", nullable = false)
    @NotBlank
    @Size(max = 255, message = "El nombre del operador no puede ser mayor a 255 caracteres")
    private String nombre;

    @Column(name = "apellido", nullable = false)
    @NotBlank
    @Size(max = 255, message = "El apellido del operador no puede ser mayor a 255 caracteres")
    private String apellido;

    @Column(name = "correo", nullable = false)
    @NotBlank
    @Size(max = 255, message = "El correo no puedo contener más de a 255 caracteres")
    private String correo;

    @Column(name = "contrasena", nullable = false)
    @NotBlank
    @Size(max = 255, message = "La contraseña no puede contener más de 255 caracteres")
    private String contrasena;

    @Column(name = "numerotelefonico", length = 45)
    private String numerotelefonico;

    @Column(name = "fechanacimiento")
    private Date fechanacimiento;

    @Column(name = "direccion", length = 45)
    private String direccion;

    @Column(name = "fotoperfil")
    private byte[] fotoperfil;

    @Column(name = "dni")
    private Integer dni;

    @Column(name = "rol", nullable = false, length = 45)
    private String rol;

    @Column(name = "valido", nullable = false)
    private Boolean valido = true;

    @Column(name = "emailconfirm", nullable = false)
    private Boolean emailconfirm = false;

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public byte[] getFotoperfil() {
        return fotoperfil;
    }

    public void setFotoperfil(byte[] fotoperfil) {
        this.fotoperfil = fotoperfil;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(Date fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getNumerotelefonico() {
        return numerotelefonico;
    }

    public void setNumerotelefonico(String numerotelefonico) {
        this.numerotelefonico = numerotelefonico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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

    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }

    public Boolean getEmailconfirm() {
      return emailconfirm;
    }

    public void setEmailconfirm(Boolean emailconfirm) {
        this.emailconfirm = emailconfirm;
    }
}