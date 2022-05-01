package com.mimesis.controller;

import com.mimesis.repository.ActorRepository;
import com.mimesis.repository.DirectorRepository;
import com.mimesis.repository.FuncionRepository;
import com.mimesis.repository.SedesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class UsuarioController {
    @Autowired
    SedesRepository sedesRepository;
    @Autowired
    ActorRepository actorRepository;
    @Autowired
    DirectorRepository directorRepository;
    @Autowired
    FuncionRepository funcionRepository;


    @GetMapping(value={"","/"})
    public String paginaPrincipal(Model model){
        return "usuario/main";
    }

    @GetMapping("/funciones")
    public String paginaFunciones(Model model){
        model.addAttribute("listaFunciones",funcionRepository.findAll());
        return "usuario/funciones";
    }
    @GetMapping("/detalles")
    public String detallesFunciones(Model model){

        return "usuario/detallesFuncion";
    }

    @GetMapping("/teatros")
    public String teatros(Model model){
        model.addAttribute("listaTeatros",sedesRepository.findAll());
        return "usuario/teatros";
    }

    @GetMapping("/actores")
    public String actores(Model model){
        model.addAttribute("listaActores",actorRepository.findAll());
        return "usuario/actores";
    }

    @GetMapping("/directores")
    public String directores(Model model){
        model.addAttribute("listaDirectores",directorRepository.findAll());
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
    @PostMapping("/carrito/reservar")
    public String carritoReservar(Model model){
        return "usuario/carrito";
    }
    @GetMapping("/carrito/comprar")
    public String carritoComprar(Model model){

        return "redirect:/";
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


    @GetMapping("/cambiarcontrasenia")
    public String cambiarContrasenia(){
        return "login/cambiarcontra";
    }

}