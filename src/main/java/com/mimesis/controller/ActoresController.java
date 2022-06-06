package com.mimesis.controller;

import com.mimesis.entity.Actor;
import com.mimesis.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/actores")
public class ActoresController {
    @Autowired
    ActorRepository actorRepository;
    @GetMapping("")
    public String actores(Model model,@RequestParam(value = "search",required = false) String search){
        if(search!=null){
            model.addAttribute("listaActores",actorRepository.busquedaActor(search));
        }else {
            model.addAttribute("listaActores",actorRepository.findAll());
        }
        return "usuario/actores";
    }
    @PostMapping("/buscar")
    public String actoresBuscar(Model model,@RequestParam(value = "search",required = false) String search){
        return "redirect:/actores?search="+search;
    }
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> imagenActor(@PathVariable("id") int id){
        Optional<Actor> actor = actorRepository.findById(id);
        if(actor.isPresent()){
            Actor actor1 = actor.get();
            return new ResponseEntity<>(
              actor1.getFoto(), HttpStatus.OK
            );
        }else{
            return null;
        }

    }
}

