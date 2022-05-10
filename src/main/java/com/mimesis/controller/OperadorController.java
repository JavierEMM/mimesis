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

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    @Autowired
    ObrasRepository obrasRepository;
    @Autowired
    GeneroRepository generoRepository;



    @GetMapping(value = {"/",""})
    public String paginaPrincipal(Model model){
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
        model.addAttribute("listaObras",obrasRepository.findAll());
        model.addAttribute("listaActores",actorRepository.findAll());
        model.addAttribute("listaDirectores",directorRepository.findAll());
        model.addAttribute("listaSedes",sedesRepository.findAll());
        model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
        model.addAttribute("listaGeneros",generoRepository.findAll());

        return "operador/funcionFrm";
    }
    @GetMapping("/estadisticas")
    public String estadisticas (){ return "operador/estadisticas";}

    @GetMapping("/edit")
    public String editarOperador (@RequestParam("id") Integer id,@ModelAttribute("funcion") Funcion funcion,Model model){
       Optional<Funcion> optionalFuncion = funcionRepository.findById(id);
        if(optionalFuncion.isPresent()){
            model.addAttribute("funcion",optionalFuncion.get());
            model.addAttribute("listaObras",obrasRepository.findAll());
            model.addAttribute("listaActores",optionalFuncion.get().getActors());
            model.addAttribute("listaDirectores",directorRepository.findAll());
            model.addAttribute("listaSedes",sedesRepository.findAll());
            model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
            model.addAttribute("listaGeneros",generoRepository.findAll());
            return "operador/editarFrm";
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

    @PostMapping("/saveEdit")
    public String guardarFuncionEditada(@ModelAttribute("funcion")@Valid Funcion funcion, BindingResult bindingResult,Model model){
        if(bindingResult.hasErrors() || funcion.getHorainicio().compareTo(funcion.getHorafin())>0 ){
            model.addAttribute("listaActores",funcion.getActors());
            model.addAttribute("listaObras",obrasRepository.findAll());
            model.addAttribute("listaDirectores",directorRepository.findAll());
            model.addAttribute("listaSedes",sedesRepository.findAll());
            model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
            model.addAttribute("listaGeneros",generoRepository.findAll());
            if(funcion.getHorainicio()!=null && funcion.getHorafin()!=null){
                if(funcion.getHorainicio().compareTo(funcion.getHorafin())>0){
                    model.addAttribute("errorTime","Ingrese un rango de horas válido");
                }
            }
            return "operador/editarFrm";
        }else{
            funcionRepository.save(funcion);
            return "redirect:/operador";
        }

    }


    @PostMapping("/save")
    public String newFuncion (@ModelAttribute("funcion")@Valid Funcion funcion, BindingResult bindingResult, Model model, @RequestParam("actoresObra") Optional<List<Actor>> actoresObra,
                               @RequestParam("foto") List<MultipartFile> file ){
        System.out.println("entro al controller");
        System.out.println(bindingResult.getAllErrors());
        if(bindingResult.hasErrors() || !actoresObra.isPresent()|| funcion.getHorainicio().compareTo(funcion.getHorafin())>0 ){
            model.addAttribute("listaActores",actorRepository.findAll());
            model.addAttribute("listaObras",obrasRepository.findAll());
            model.addAttribute("listaDirectores",directorRepository.findAll());
            model.addAttribute("listaSedes",sedesRepository.findAll());
            model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
            model.addAttribute("listaGeneros",generoRepository.findAll());
            System.out.println("entro al primer if");
            if(!actoresObra.isPresent()){
                System.out.println("Error actor");
                model.addAttribute("errorActor", "Debe seleccionar un género");
            }
            if(funcion.getHorainicio()!=null && funcion.getHorafin()!=null){
                if(funcion.getHorainicio().compareTo(funcion.getHorafin())>0){
                    model.addAttribute("errorTime","Ingrese un rango de horas válido");
                }
            }

            if(funcion.getId()==0){
                return "operador/funcionFrm";
            }else{
                return "operador/editarFrm";
            }



        }else{
            System.out.println("Entro al else");
            funcion.setActors((Set<Actor>) actoresObra.get());

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
