package com.mimesis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CarritoController {

    @PostMapping("/carrito/comprar")
    public String comprarCarrito(Model model){
        return "redirect:/carrito";
    }
    @GetMapping("/carrito")
    public String carrito(Model model){
        return "usuario/carrito";
    }
    @PostMapping("/carrito/reservar")
    public String carritoReservar(Model model){
        return "usuario/carrito";
    }
    @GetMapping("/carrito/comprar")
    public String carritoComprar(Model model){
        return "redirect:/";
    }
}
