package com.mimesis.entity;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario", nullable = false)
    private Integer id;


    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "Ingrese su nombre")
    @Size(max = 255, message = "El nombre no puede ser mayor a 255 caracteres")
    private String nombre;

    @Column(name = "apellido", nullable = false)
    @NotBlank(message = "Ingrese su apellido")
    @Size(max = 255, message = "El apellido no puede ser mayor a 255 caracteres")
    private String apellido;

    @Column(name = "correo", nullable = false,unique = true)
    @Email(message = "Debe ser un correo valido")
    @NotBlank(message = "Ingrese su correo")
    @Size(max = 255, message = "El correo no puedo contener más de a 255 caracteres")
    private String correo;

    @Column(name = "contrasena", nullable = false)
    @NotBlank(message = "No deje el parametro en blanco")
    @Size(max = 255, message = "La contraseña no puede contener más de 255 caracteres")
    private String contrasena;

    @Column(name = "numerotelefonico", length = 45)
    @NotBlank(message = "Debe agregar un numero de contacto")
    private String numerotelefonico;

    @Column(name = "fechanacimiento")
    @Past(message = "La fecha de nacimiento debe ser menor a la actual")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "No deje el parametro en blanco")
    private LocalDate fechanacimiento;

    @Column(name = "direccion")
    @Size(max = 45,message = "La direccion no puede pasar de 45 caracteres")
    @NotBlank(message = "No deje el parametro en blanco")
    private String direccion;

    @Column(name = "fotoperfil")
    private byte[] fotoperfil;

    @Column(name = "dni")
    @Positive(message = "Debe ser un numero positivo de 8 digitos")
    @NotNull(message = "Ingrese su DNI")
    @Digits(integer = 8,fraction = 0,message = "Debe ser un numero de 8 digitos")
    private Integer dni;

    @Column(name = "rol", nullable = false, length = 45)
    private String rol;

    @Column(name = "valido", nullable = false)
    private Boolean valido = true;

    @Column(name = "emailconfirm", nullable = false)
    private Boolean emailconfirm = false;

    @Column(name="authprovider")
    private String authprovider = "mimesis";

    @Column(name = "token",nullable = true)
    private String token  = UUID.randomUUID().toString();

    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String correo, String rol, Boolean emailconfirm, String authprovider) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.rol = rol;
        this.emailconfirm = emailconfirm;
        this.authprovider = authprovider;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public LocalDate getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(LocalDate fechanacimiento) {
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

    public String getAuthprovider() {
        return authprovider;
    }

    public void setAuthprovider(String authprovider) {
        this.authprovider = authprovider;
    }
}