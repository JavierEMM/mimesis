package com.mimesis.controller;

import com.mimesis.entity.Actor;
import com.mimesis.entity.Funcion;
import com.mimesis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping("")
    public String paginaPrincipal(Model model){
        List<Funcion> lista = funcionRepository.findAll();
        model.addAttribute("listaFunciones",funcionRepository.findAll());

        return "operador/listafunciones";
    }
    @RequestMapping("/crearfuncion")
    public String nuevaFuncion (Model model){
        Funcion funcionNueva = new Funcion();
        funcionNueva.setId(0);
        model.addAttribute("funcionNueva",funcionNueva);
        model.addAttribute("listaActores",actorRepository.findAll());
        model.addAttribute("listaDirectores",directorRepository.findAll());
        model.addAttribute("listaSedes",sedesRepository.findAll());
        model.addAttribute("listaSalas",salasRepository.findAll());
        return "operador/crearfuncion";
    }
    @RequestMapping("/estadisticas")
    public String estadisticas (){ return "operador/estadisticas";}
    @RequestMapping("/edit")
    public String editarOperador (){ return "operador/editoperador";}

    @PostMapping("/new")
    public String newFuncion (Funcion funcion, @RequestParam("horai") String horainicio, @RequestParam("horaf") String horafin,
                              @RequestParam("fechai") String fechainicio){
        ArrayList<Actor> listaActSelect = new ArrayList<>();
        //listaActSelect.addAll(funcion.getActors());
        System.out.println(funcion.getNombre());
        System.out.println(funcion.getAforo());
        System.out.println(funcion.getCosto());
        System.out.println(funcion.getGenero());
        funcion.setId(0);
        funcion.setHorainicio(LocalTime.parse(horainicio));
        funcion.setHorafin(LocalTime.parse(horafin));
        funcion.setFecha(LocalDate.parse(fechainicio));
        System.out.println(funcion.getIddirector());
        for(Actor act : listaActSelect){
            System.out.println("Actors size :"+act.getNombre());
        }
        funcionRepository.save(funcion);
        return  "redirect:/operador";
    }


}
