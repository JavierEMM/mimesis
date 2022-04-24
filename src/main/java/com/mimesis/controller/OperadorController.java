package com.mimesis.controller;

import com.mimesis.entity.Actor;
import com.mimesis.entity.Funcion;
import com.mimesis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/operador")
public class OperadorController {
    @Autowired
    FuncionRepository funcionRepository;
    @Autowired
    ActorRepository actorRepository;
    @Autowired
    DirectorRepository directorRepository;
    @Autowired
    SedesRepository sedesRepository;
    @Autowired
    SalasRepository salasRepository;

    @GetMapping(value = {"/",""})
    public String paginaPrincipal(Model model){
        List<Funcion> lista = funcionRepository.findAll();
        model.addAttribute("listaFunciones",funcionRepository.findAll());

        return "operador/listafunciones";
    }
    @GetMapping("/crearfuncion")
    public String nuevaFuncion (@ModelAttribute("funcion") Funcion funcion, Model model){
        model.addAttribute("listaActores",actorRepository.findAll());
        model.addAttribute("listaDirectores",directorRepository.findAll());
        model.addAttribute("listaSedes",sedesRepository.findAll());
        model.addAttribute("listaSalas",salasRepository.findAll());
        return "operador/crearfuncion";
    }
    @GetMapping("/estadisticas")
    public String estadisticas (){ return "operador/estadisticas";}
    @GetMapping("/edit")
    public String editarOperador (){ return "operador/editoperador";}

    @PostMapping("/new")
    public String newFuncion (@ModelAttribute("funcion") Funcion funcion, Model model,@RequestParam("actoresObra") List<Actor> actoresObra){
        ArrayList<Actor> listaActSelect = new ArrayList<>();
        listaActSelect.addAll(actoresObra);
        funcion.setActoresPorFuncion(actoresObra);
        System.out.println(funcion.getNombre());
        System.out.println(funcion.getAforo());
        System.out.println(funcion.getCosto());
        System.out.println(funcion.getGenero());
        System.out.println(funcion.getIdsala().getNombre());
        System.out.println(funcion.getIddirector().getNombre());
        for(Actor act : listaActSelect){
            System.out.println("Actors size :"+act.getNombre());
        }
        System.out.println(funcion.getHorainicio());
        System.out.println(funcion.getHorafin() );
        System.out.println(funcion.getActoresPorFuncion().get(0).getNombre());
        funcionRepository.save(funcion);
        return  "redirect:/operador";
    }


}
