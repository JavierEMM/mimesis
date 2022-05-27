package com.mimesis.controller;

import com.mimesis.repository.SedesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/teatros")
public class TeatroController {
    @Autowired
    SedesRepository sedesRepository;

    @GetMapping("")
    public String teatros(Model model,@RequestParam(value = "search",required = false) String search){
        if(search!=null){
            model.addAttribute("listaTeatros",sedesRepository.busquedaTeatro(search));
        }else {
            model.addAttribute("listaTeatros",sedesRepository.findAll());
        }
        return "usuario/teatros";
    }
    @PostMapping("/buscar")
    public String teatrosBuscar(Model model,@RequestParam(value = "search",required = false) String search){
        return "redirect:/teatros?search="+search;
    }
}