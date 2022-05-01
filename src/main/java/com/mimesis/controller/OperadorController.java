package com.mimesis.controller;

import com.mimesis.entity.Actor;
import com.mimesis.entity.Funcion;
import com.mimesis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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

    @PostMapping("/search")
    public String buscarFuncion(Model model,@RequestParam("busqueda") String busqueda,@RequestParam("categoria") String categoria){
        if(categoria.equalsIgnoreCase("Nombre")){
            model.addAttribute("listaFunciones",funcionRepository.listaBuscarFuncionesNombre(busqueda));
        }else{
            model.addAttribute("listaFunciones",funcionRepository.listaBuscarFuncionesGenero(busqueda));
        }

        return "operador/listafunciones";
    }

    @GetMapping("/crearfuncion")
    public String nuevaFuncion (@ModelAttribute("funcion") Funcion funcion, Model model){
        System.out.println(funcion.getCosto());
        model.addAttribute("listaActores",actorRepository.findAll());
        model.addAttribute("listaDirectores",directorRepository.findAll());
        model.addAttribute("listaSedes",sedesRepository.findAll());
        model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
        return "operador/funcionFrm";
    }
    @GetMapping("/estadisticas")
    public String estadisticas (){ return "operador/estadisticas";}
    @GetMapping("/edit")
    public String editarOperador (@RequestParam("id") Integer id,@ModelAttribute("funcion") Funcion funcion,Model model){
        Optional<Funcion> optionalFuncion= funcionRepository.findById(id);
        if(optionalFuncion.isPresent()){
            model.addAttribute("funcion",optionalFuncion.get());
            model.addAttribute("listaActores",optionalFuncion.get().getActoresPorFuncion());
            model.addAttribute("listaDirectores",directorRepository.findAll());
            model.addAttribute("listaSedes",sedesRepository.findAll());
            model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
            return "operador/funcionFrm";
        }

        return "redirect:/operador";
        }

    @PostMapping("/save")
    public String newFuncion (@ModelAttribute("funcion")@Valid Funcion funcion, BindingResult bindingResult, Model model, @RequestParam("actoresObra") Optional<List<Actor>> actoresObra,
                              RedirectAttributes redirectAttributes){
        System.out.println("El costo es " + funcion.getCosto());
        double costoInvalid=0.0;
        if(bindingResult.hasErrors()|| funcion.getGenero().equalsIgnoreCase("no") || !actoresObra.isPresent() ){
            model.addAttribute("listaActores",actorRepository.findAll());
            model.addAttribute("listaDirectores",directorRepository.findAll());
            model.addAttribute("listaSedes",sedesRepository.findAll());
            model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
            if(funcion.getCosto()==costoInvalid){
                System.out.println("entra al error de costo");
                model.addAttribute("errorCosto","Deber ingresar un valor mayor a 0");
            }
            if(funcion.getGenero().equalsIgnoreCase("no")) {
                System.out.println("Error genero");
                model.addAttribute("errorGenero", "Debe seleccionar un género");
            }
            if(!actoresObra.isPresent()){
                System.out.println("Error actor");
                model.addAttribute("errorActor", "Debe seleccionar un género");
            }
            return "operador/funcionFrm";

        }else{
            funcion.setActoresPorFuncion(actoresObra.get());
            funcionRepository.save(funcion);
            return  "redirect:/operador";
        }

    }

}
