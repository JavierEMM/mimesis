package com.mimesis.controller;

import com.mimesis.entity.Actor;
import com.mimesis.entity.Foto;
import com.mimesis.entity.Funcion;
import com.mimesis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
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
    @Autowired
    FotoRepository fotoRepository;




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
        model.addAttribute("listaActores",actorRepository.findAll());
        model.addAttribute("listaDirectores",directorRepository.findAll());
        model.addAttribute("listaSedes",sedesRepository.findAll());
        model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
        ArrayList<String> listaGeneros = new ArrayList<>();
        listaGeneros.add("Drama");
        listaGeneros.add("Comedia");
        listaGeneros.add("Ciencia ficcion");
        listaGeneros.add("Aventura");
        listaGeneros.add("Suspenso");
        listaGeneros.add("Terror");
        listaGeneros.add("Musical");
        model.addAttribute("listaGeneros",listaGeneros);

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
            ArrayList<String> listaGeneros = new ArrayList<>();
            listaGeneros.add("Drama");
            listaGeneros.add("Comedia");
            listaGeneros.add("Ciencia ficcion");
            listaGeneros.add("Aventura");
            listaGeneros.add("Suspenso");
            listaGeneros.add("Terror");
            listaGeneros.add("Musical");
            model.addAttribute("listaGeneros",listaGeneros);
            return "operador/funcionFrm";
        }

        return "redirect:/operador";
        }

    @GetMapping("/delete")
    public String delteFuncion(@RequestParam("id") Integer id){
        Optional<Funcion> optionalFuncion = funcionRepository.findById(id);

        if(optionalFuncion.isPresent()){
            Funcion funcionMod= optionalFuncion.get();
            funcionMod.setValido(false);
            funcionRepository.save(funcionMod);
        }
        return "redirect:/operador";
    }

    @PostMapping("/save")
    public String newFuncion (@ModelAttribute("funcion")@Valid Funcion funcion, BindingResult bindingResult, Model model, @RequestParam("actoresObra") Optional<List<Actor>> actoresObra,
                               @RequestParam("foto") List<MultipartFile> file ){
        double costoInvalid=0.0;
        System.out.println("entro al controller");
        System.out.println(bindingResult.getAllErrors());
        if(bindingResult.hasErrors()|| funcion.getGenero().equalsIgnoreCase("no") || !actoresObra.isPresent()||
                funcion.getIdsede().getId()!=funcion.getIdsala().getIdsede().getId()|| funcion.getHorainicio().compareTo(funcion.getHorafin())>0 ){
            model.addAttribute("listaActores",actorRepository.findAll());
            model.addAttribute("listaDirectores",directorRepository.findAll());
            model.addAttribute("listaSedes",sedesRepository.findAll());
            model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
            ArrayList<String> listaGeneros = new ArrayList<>();
            listaGeneros.add("Drama");
            listaGeneros.add("Comedia");
            listaGeneros.add("Ciencia ficcion");
            listaGeneros.add("Aventura");
            listaGeneros.add("Suspenso");
            listaGeneros.add("Terror");
            listaGeneros.add("Musical");
            model.addAttribute("listaGeneros",listaGeneros);
            System.out.println("entro al primer if");
            if (funcion.getIdsede() != null) {
                if (funcion.getIdsede().getId()!=funcion.getIdsala().getIdsede().getId()){
                    model.addAttribute("errorMatching","La sala debe coincidir con la sede");
                }
            }
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
            if(funcion.getHorainicio()!=null && funcion.getHorafin()!=null){
                if(funcion.getHorainicio().compareTo(funcion.getHorafin())>0){
                    model.addAttribute("errorTime","Ingrese un rango de horas válido");
                }
            }

            return "operador/funcionFrm";

        }else{
            System.out.println("Entro al else");
            funcion.setActoresPorFuncion(actoresObra.get());

            funcionRepository.save(funcion);
            try{
                System.out.println("entro al try");
                for (MultipartFile file1: file ) {
                    Foto foto= new Foto();
                    foto.setFoto(file1.getBytes());
                    foto.setIdfuncion(funcion);
                    fotoRepository.save(foto);
                }

            } catch (IOException e){
                e.printStackTrace();
            }
            return "redirect:/operador";
        }

    }

}
