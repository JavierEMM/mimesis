package com.mimesis.controller;

import com.mimesis.repository.FuncionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/funciones")
public class FuncionController {
    @Autowired
    FuncionRepository funcionRepository;

    @GetMapping("")
    public String paginaFunciones(@RequestParam(value = "search",required = false) String search,Model model){
        if(search!=null){
            model.addAttribute("listaFunciones",funcionRepository.listaBuscarFuncionesNombre(search));
        }else {
            model.addAttribute("listaFunciones",funcionRepository.findAll());
        }
        return "usuario/funciones";
    }

    @PostMapping("/buscar")
    public String funcionesBuscar(@RequestParam(value = "search",required = false) String search){
        return "redirect:/funciones?search="+search;
    }

}
