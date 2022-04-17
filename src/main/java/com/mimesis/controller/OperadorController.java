package com.mimesis.controller;

import com.mimesis.entity.Funcion;
import com.mimesis.repository.FuncionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/operador")
public class OperadorController {
    @Autowired
    FuncionRepository funcionRepository;

    @RequestMapping("")
    public String paginaPrincipal(Model model){
        List<Funcion> lista = funcionRepository.findAll();
        System.out.println(lista.get(0).getNombre());
        model.addAttribute("listaFunciones",funcionRepository.findAll());

        return "operador/listafunciones";
    }
    @RequestMapping("/crearfuncion")
    public String nuevaFuncion (){return "operador/crearFuncion";}
    @RequestMapping("/estadisticas")
    public String estadisticas (){ return "operador/estadisticas";}
    @RequestMapping("/edit")
    public String editarOperador (){ return "operador/editoperador";}
}
