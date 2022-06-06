package com.mimesis.controller;

import com.mimesis.entity.Actor;
import com.mimesis.entity.Director;
import com.mimesis.repository.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/directores")
public class DirectoresController {

    @Autowired
    DirectorRepository directorRepository;
    @GetMapping("")
    public String directores(Model model,@RequestParam(value = "search",required = false) String search){
        if(search!=null){
            model.addAttribute("listaDirectores",directorRepository.busquedaDirector(search));
        }else {
            model.addAttribute("listaDirectores",directorRepository.findAll());
        }

        return "usuario/directores";
    }
    @PostMapping("/buscar")
    public String directoresBuscar(Model model,@RequestParam(value = "search",required = false) String search){
        return "redirect:/directores?search="+search;
    }
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> imagenActor(@PathVariable("id") int id){
        Optional<Director> director = directorRepository.findById(id);
        if(director.isPresent()){
            Director director1 = director.get();
            return new ResponseEntity<>(
                    director1.getFoto(), HttpStatus.OK
            );
        }else{
            return null;
        }

    }
}
