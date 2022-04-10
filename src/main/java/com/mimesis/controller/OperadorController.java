package com.mimesis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/operador")
public class OperadorController {

    @RequestMapping("")
    public String paginaPrincipal(){
        return "operador/listafunciones";
    }
}
