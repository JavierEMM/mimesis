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
    @RequestMapping("/crearfuncion")
    public String nuevaFuncion (){return "operador/crearFuncion";}
    @RequestMapping("/estadisticas")
    public String estadisticas (){ return "operador/estadisticas";}
}
