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

    @GetMapping("/perfil")
    public String perfil(Model model){
        return "usuario/perfil";
    }
    @PostMapping("/perfil/save")
    public String guardarPerfil(Model model){
        return "redirect:/perfil";
    }


    @GetMapping("/historial")
    public String historialCompra(){
        return "usuario/historial";
    }

    @GetMapping("/calificacion")
    public String calificacion(){
        return "usuario/calificacion";
    }


}