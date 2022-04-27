package com.mimesis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class UsuarioController {
    @GetMapping(value={"","/"})
    public String paginaPrincipal(Model model){
        return "usuario/main";
    }

    @GetMapping("/funciones")
    public String paginaFunciones(Model model){
        return "usuario/funciones";
    }
    @GetMapping("/detalles")
    public String detallesFunciones(Model model){

        return "usuario/detallesFuncion";
    }

    @GetMapping("/teatros")
    public String teatros(Model model){

        return "usuario/teatros";
    }

    @GetMapping("/actores")
    public String actores(Model model){

        return "usuario/actores";
    }

    @GetMapping("/directores")
    public String directores(Model model){

        return "usuario/directores";
    }
    @GetMapping("/perfil")
    public String perfil(Model model){

        return "usuario/perfil";
    }
    @PostMapping("/perfil/save")
    public String guardarPerfil(Model model){

        return "redirect:/perfil";
    }
    @PostMapping("/carrito/comprar")
    public String comprarCarrito(Model model){

        return "redirect:/carrito";
    }
    @GetMapping("/carrito")
    public String carrito(Model model){

        return "usuario/carrito";
    }


    @GetMapping("/iniciarsesion")
    public String login(){
        return "login/login";
    }

    @GetMapping("/recuperarcontrasenia")
    public String nuevacontrasenia(){
        return "login/nuevacontrasenia";
    }
    @GetMapping("/historial")
    public String historialCompra(){
        return "usuario/historial";
    }
    @GetMapping("/calificacion")
    public String calificacion(){
        return "usuario/calificacion";
    }
    @GetMapping("/registro")
    public String registro(){
        return "login/register";
    }

    @GetMapping("/cambiarcontrasenia")
    public String cambiarContrasenia(){
        return "login/cambiarcontra";
    }

}