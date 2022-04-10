package com.mimesis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class UsuarioController {
    @GetMapping("")
    public String paginaPrincipal(){
        return "usuario/main";
    }
}