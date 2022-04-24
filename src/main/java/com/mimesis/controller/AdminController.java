package com.mimesis.controller;

import com.mimesis.entity.Sala;
import com.mimesis.entity.Sede;
import com.mimesis.repository.SalasRepository;
import com.mimesis.repository.SedesRepository;
import com.mimesis.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")

public class AdminController {

    @Autowired
    SedesRepository sedesRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    SalasRepository salasRepository;

    @GetMapping("salas")
    public String paginaSalas( Model model){
        List<Sala> salaList = salasRepository.findAll();
        model.addAttribute("salaList",salaList);
        return "admin/salas";
    }

    @GetMapping("agregarsalas")
    public String paginaAgregarsalas( Model model){
        List<Sede> sedeList=sedesRepository.findAll();
        model.addAttribute("sedeList",sedeList);
        return "admin/agregarsalas";
    }

    @PostMapping("savesalas")
    public String savesalas(Sala sala){
        salasRepository.save(sala);
        return "redirect:/admin/salas";
    }

    @GetMapping("editarsalas")
    public String paginaEditarsalas(@RequestParam("id") Integer id, Model model){
        Optional<Sala> optionalSala = salasRepository.findById(id);
        if(optionalSala.isPresent()){
            Sala sala = optionalSala.get();
            model.addAttribute("sala",sala);
            List<Sede> sedeList=sedesRepository.findAll();
            model.addAttribute("sedeList",sedeList);
            return "admin/editarsalas";
        } else {
            return "redirect:/admin/salas";
        }
    }

    @GetMapping("/borrar")
    public  String borrar(@RequestParam("id") Integer id){
        Optional<Sala> optionalSala = salasRepository.findById(id);
        if(optionalSala.isPresent()){
            salasRepository.deleteById(id);
        }
        return "redirect:/admin/salas";
    }



    @GetMapping("sedes")
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
    public String operadores(Model model){
        model.addAttribute("listaOperadores",usuarioRepository.findByRol("Operador"));
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
    public String clientes(Model model){
        model.addAttribute("listaClientes",usuarioRepository.findByRol("Cliente"));
        return "admin/clientes";
    }
}
