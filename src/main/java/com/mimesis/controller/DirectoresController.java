package com.mimesis.controller;

import com.mimesis.repository.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/directores")
public class DirectoresController {

    @Autowired
    DirectorRepository directorRepository;
    @GetMapping("")
    public String directores(Model model,@RequestParam(value = "search",required = false) String search){
        if(search!=null){
            model.addAttribute("listaDirectores",directorRepository.busquedaDirector(search));
        }else {
            model.addAttribute("listaDirectores",directorRepository.findAll());
        }

        return "usuario/directores";
    }
    @PostMapping("/buscar")
    public String directoresBuscar(Model model,@RequestParam(value = "search",required = false) String search){
        return "redirect:/directores?search="+search;
    }
}
