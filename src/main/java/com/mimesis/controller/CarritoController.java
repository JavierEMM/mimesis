package com.mimesis.controller;

import com.mimesis.entity.Funcion;
import com.mimesis.entity.Obra;
import com.mimesis.entity.Sede;
import com.mimesis.repository.FuncionRepository;
import com.mimesis.repository.ObrasRepository;
import com.mimesis.repository.SedesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/carrito")
public class CarritoController {
    @Autowired
    ObrasRepository obrasRepository;
    @Autowired
    FuncionRepository funcionRepository;
    @Autowired
    SedesRepository sedesRepository;
    @GetMapping("")
    public String carrito(Model model){
        return "usuario/carrito";
    }

    @RequestMapping(value = "/reservar", method = RequestMethod.POST, params = "comprar")
    public String comprarCarrito(Model model,@RequestParam("obra") String nombreObra){
        Obra obra = obrasRepository.findByNombre(nombreObra);
        model.addAttribute("obra",obra);
        return "redirect:/carrito";
    }

    @RequestMapping(value = "/reservar", method = RequestMethod.POST, params = "reservar")
    public String carritoReservar(Model model, @RequestParam("obra") String nombreObra,@RequestParam("teatro") Integer teatro, RedirectAttributes attributes) throws UnsupportedEncodingException {
        Obra obra = obrasRepository.findByNombre(nombreObra);
        Optional<Sede> sede = sedesRepository.findById(teatro);
        List<Funcion> funcionList = funcionRepository.funcionesPorTeatro(teatro,obra.getId());
        model.addAttribute("listaFunciones", funcionList);
        model.addAttribute("obra", obra);
        model.addAttribute("sede",sede.get());

        return "usuario/eligefuncion";
    }

}
