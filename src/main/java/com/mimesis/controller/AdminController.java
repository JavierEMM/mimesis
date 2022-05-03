package com.mimesis.controller;

import com.mimesis.entity.Foto;
import com.mimesis.entity.Sala;
import com.mimesis.entity.Sede;
import com.mimesis.entity.Usuario;
import com.mimesis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")

public class AdminController {

    @Autowired
    SedesRepository sedesRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    SalasRepository salasRepository;

    @Autowired
    ActorRepository actorRepository;

    @Autowired
    DirectorRepository directorRepository;

    @Autowired
    FotoRepository fotoRepository;

    @GetMapping("/salas")
    public String paginaSalas(Model model){
        List<Sala> salaList = salasRepository.findAll();
        model.addAttribute("salaList",salaList);
        model.addAttribute("listasedes",sedesRepository.findAll());
        return "admin/salas";
    }
    @PostMapping("/searchsalas")
    public String buscarsala(Model model,@RequestParam("sedes") Sede sedes){
        model.addAttribute("listasedes",sedesRepository.findAll());
        model.addAttribute("salaList",sedes.getListasalas());
        return "admin/salas";
    }

    @GetMapping("/agregarsalas")
    public String paginaAgregarsalas( Model model){
        List<Sede> sedeList=sedesRepository.findAll();
        model.addAttribute("sedeList",sedeList);
        return "admin/agregarsalas";
    }

    @PostMapping("/savesalas")
    public String savesalas(Sala sala){
        salasRepository.save(sala);
        return "redirect:/admin/salas";
    }

    @GetMapping("/editarsalas")
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
   /////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/sedes")
    public String paginaSedes( Model model, @RequestParam(value="search",required = false) String search){
        if(search!=null){
            model.addAttribute("listaSedes",sedesRepository.busquedaTeatro(search));
        }else{
            model.addAttribute("listaSedes",sedesRepository.findAll());
        }

        return "admin/sedes";
    }
    @PostMapping("/searchsedes")
    public String sedesBuscar(Model model,@RequestParam(value = "search",required = false) String search){
        return "redirect:/admin/sedes?search="+search;
    }

    @GetMapping("/actoresydirectores")
    public String paginaActoresydirectores(Model model) {
        model.addAttribute("listaActores", actorRepository.findAll());
        model.addAttribute("listaDirectores", directorRepository.findAll());
        return "admin/actoresydirectores";
    }

    @RequestMapping("/agregarsedes")
    public String paginaAgregarsedes(Sede sede){
        return "admin/agregarsedes";
    }

    @PostMapping("/savesedes")
    public String savesedes(Sede sede, @RequestParam("archivo") List<MultipartFile> file  ){
        sedesRepository.save(sede);
        Collections.reverse(file);
        try{
            for (MultipartFile file1: file ) {
                Foto foto= new Foto();
                foto.setFoto(file1.getBytes());
                foto.setIdsede(sede);
                fotoRepository.save(foto);
            }
            return "redirect:/admin/sedes";
        } catch (IOException e){
            e.printStackTrace();
        }
        return "admin/agregarsedes";

    }

    @GetMapping("/imagesedes/{id}")
    public ResponseEntity<byte[]> mostrarimagen(@PathVariable("id") int id){
        Optional<Sede> opt = sedesRepository.findById(id);
        if (opt.isPresent()) {
            Sede sedefoto = opt.get();
            byte[] imagenComoBytes = sedefoto.getFotosporsede().get(0).getFoto();
            return new ResponseEntity<>(
                    imagenComoBytes,
                    HttpStatus.OK);
        }else{
            return null;
        }
    }



    @GetMapping("/editarsedes")
    public String paginaEditarsedes(@RequestParam("id") Integer id, Model model){
        Optional<Sede> optionalSede = sedesRepository.findById(id);
        if(optionalSede.isPresent()){
            Sede sede = optionalSede.get();
            model.addAttribute("sede",sede);
            //List<Sede> sedeList=sedesRepository.findAll();
            //model.addAttribute("sedeList",sedeList);
            return "admin/editarsedes";
        } else {
            return "redirect:/admin/sedes";
        }
    }

    @GetMapping("/borrarsede")
    public  String borrarsede(@RequestParam("id") Integer id){
        Optional<Sede> optionalSede = sedesRepository.findById(id);
        if(optionalSede.isPresent()){
            sedesRepository.deleteById(id);
        }
        return "redirect:/admin/sedes";
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////

    @RequestMapping("actoresydirectores")
    public String paginaActoresydirectores(){
        return "admin/actoresydirectores";
    }



    @RequestMapping("agregaractoresydirectores")
    public String paginaAgregaractoresydirectores(){
        return "admin/agregaractoresydirectores";
    }
    @RequestMapping("editarsedes")
    public String paginaEditarsedes(){
        return "admin/editarsedes";
    }


    @RequestMapping("editaractoresydirectores")
    public String paginaEditaractoresydirectores(){
        return "admin/editaractoresydirectores";
    }

    @GetMapping("operadores")
    public String operadores(Model model){
        model.addAttribute("listaOperadores",usuarioRepository.findByRol("Operador"));
        return "admin/operadores";
    }

    @RequestMapping("editaroperador")
    public String editarOperador(){
        return "admin/editaroperador";
    }

    @GetMapping("agregaroperador")
    public String agregarOperador(Model model){
        return "admin/agregaroperador";
    }

    @RequestMapping("clientes")
    public String clientes(Model model){
        model.addAttribute("listaClientes",usuarioRepository.findByRol("Cliente"));
        return "admin/clientes";
    }

    @PostMapping("/saveoperador")
    public String savesedes(Usuario usuario){
        usuarioRepository.save(usuario);
        return "redirect:/admin/operadores";
    }

    @GetMapping("/borraroperador")
    public String borraroperador(@RequestParam("id") Integer id){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if(optionalUsuario.isPresent()){
            usuarioRepository.deleteById(id);
        }
        return "redirect:/admin/operadores";
    }

}
