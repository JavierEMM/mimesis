package com.mimesis.controller;

import com.mimesis.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/actores")
public class ActoresController {
    @Autowired
    ActorRepository actorRepository;
    @GetMapping("")
    public String actores(Model model,@RequestParam(value = "search",required = false) String search){
        if(search!=null){
            model.addAttribute("listaActores",actorRepository.busquedaActor(search));
        }else {
            model.addAttribute("listaActores",actorRepository.findAll());
        }
        return "usuario/actores";
    }
    @PostMapping("/buscar")
    public String actoresBuscar(Model model,@RequestParam(value = "search",required = false) String search){
        return "redirect:/actores?search="+search;
    }
}
