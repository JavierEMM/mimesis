package com.mimesis.controller;

import com.mimesis.entity.Director;
import com.mimesis.entity.Foto;
import com.mimesis.entity.Sede;
import com.mimesis.repository.FotoRepository;
import com.mimesis.repository.SedesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/teatros")
public class TeatroController {
    @Autowired
    SedesRepository sedesRepository;
    @Autowired
    FotoRepository fotoRepository;

    @GetMapping("")
    public String teatros(Model model,@RequestParam(value = "search",required = false) String search){
        if(search!=null){
            model.addAttribute("listaTeatros",sedesRepository.busquedaTeatro(search));
        }else {
            model.addAttribute("listaTeatros",sedesRepository.findAll());
        }
        return "usuario/teatros";
    }
    @PostMapping("/buscar")
    public String teatrosBuscar(Model model,@RequestParam(value = "search",required = false) String search){
        return "redirect:/teatros?search="+search;
    }
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> imagenTeatro(@PathVariable("id") int id){
        List<Foto> foto = fotoRepository.listaFotosxSede(id);
        if(foto != null){
            Foto foto1 = foto.get(0);
            return new ResponseEntity<>(
                    foto1.getFoto(), HttpStatus.OK
            );
        }else{
            return null;
        }
    }
}