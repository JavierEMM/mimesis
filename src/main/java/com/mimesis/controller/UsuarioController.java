package com.mimesis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/carrito")
    public String carrito(Model model){

        return "usuario/carrito";
    }


    @GetMapping("/iniciarsesion")
    public String login(){
        return "login/login";
    }
}