package com.mimesis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class UsuarioController {
    @GetMapping("")
    public String paginaPrincipal(Model model){
        return "usuario/main";
    }
}