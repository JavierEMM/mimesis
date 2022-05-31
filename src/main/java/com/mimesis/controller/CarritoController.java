package com.mimesis.controller;

import com.mimesis.dto.DTOcarrito;
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

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
    public String carrito(Model model,HttpSession session){
        ArrayList<DTOcarrito> funcions = (ArrayList) session.getAttribute("carrito");
        model.addAttribute("carrito",funcions);
        return "usuario/carrito";
    }

    @PostMapping("/funcion")
    public String seleccionarFuncion(Model model,@RequestParam(value = "cantidad",required = false) Integer cantidad, @RequestParam(value = "funcion", required = false) Integer funcion, HttpSession session){
        if(cantidad == null || funcion == null){
            return "redirect:/";
        }else{
            Optional<Funcion> funcion2 = funcionRepository.findById(funcion);
            Sede sede = sedesRepository.sedePorFuncion(funcion);
            ArrayList<DTOcarrito> funcions = (ArrayList) session.getAttribute("carrito");
            DTOcarrito dtOcarrito = new DTOcarrito();
            dtOcarrito.setFuncion(funcion2.get());
            dtOcarrito.setCantidad(cantidad);
            dtOcarrito.setCostoTotal(funcion2.get().getCosto()*cantidad);
            dtOcarrito.setSede(sede);
            funcions.add(dtOcarrito);
            session.setAttribute("carrito",funcions);
            session.setAttribute("ncarrito",funcions.size());
            model.addAttribute("carrito",funcions);
            return "usuario/carrito";
        }

    }
    @GetMapping("/borrar")
    public String borrarCarrito(HttpSession session,@RequestParam("num") int id){
        ArrayList<DTOcarrito> carrito =(ArrayList) session.getAttribute("carrito");
        System.out.println("ID BORRAR: "+id);
        carrito.remove(id);
        session.setAttribute("carrito",carrito);
        session.setAttribute("ncarrito",carrito.size());
        return "redirect:/carrito";
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
