package com.mimesis.controller;

import com.mimesis.repository.SedesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")

public class AdminController {

    @Autowired
    SedesRepository sedesRepository;

    @RequestMapping("salas")
    public String paginaSalas(){

        return "admin/salas";
    }
    @RequestMapping("sedes")
    public String paginaSedes(Model model){
        model.addAttribute("listaSedes",sedesRepository.findAll());
        return "admin/sedes";
    }

    @RequestMapping("actoresydirectores")
    public String paginaActoresydirectores(){
        return "admin/actoresydirectores";
    }
    @RequestMapping("agregarsalas")
    public String paginaAgregarsalas(){
        return "admin/agregarsalas";
    }
    @RequestMapping("agregarsedes")
    public String paginaAgregarsedes(){
        return "admin/agregarsedes";
    }
    @RequestMapping("agregaractoresydirectores")
    public String paginaAgregaractoresydirectores(){
        return "admin/agregaractoresydirectores";
    }
    @RequestMapping("editarsalas")
    public String paginaEditarsalas(){
        return "admin/editarsalas";
    }
    @RequestMapping("editarsedes")
    public String paginaEditarsedes(){
        return "admin/editarsedes";
    }
    @RequestMapping("editaractoresydirectores")
    public String paginaEditaractoresydirectores(){
        return "admin/editaractoresydirectores";
    }

    @RequestMapping("operadores")
    public String operadores(){
        return "admin/operadores";
    }

    @RequestMapping("editaroperador")
    public String editarOperador(){
        return "admin/editaroperador";
    }
    @RequestMapping("agregaroperador")
    public String agregarOperador(){
        return "admin/agregaroperador";
    }

    @RequestMapping("clientes")
    public String clientes(){
        return "admin/clientes";
    }



}
