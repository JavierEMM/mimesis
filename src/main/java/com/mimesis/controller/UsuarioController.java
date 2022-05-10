package com.mimesis.controller;

import com.mimesis.entity.Usuario;
import com.mimesis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("")
public class UsuarioController {
    @Autowired
    SedesRepository sedesRepository;
    @Autowired
    ActorRepository actorRepository;
    @Autowired
    DirectorRepository directorRepository;
    @Autowired
    FuncionRepository funcionRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping(value={"","/"})
    public String paginaPrincipal(Model model){
        return "usuario/main";
    }

    @GetMapping("/perfil")
    public String perfil(Model model){
        return "usuario/perfil";
    }

    @PostMapping("/perfil/save")
    public String guardarPerfil(Usuario usuario, @RequestParam("numerotelefonico")String numerotelefonico,
                    @RequestParam("direccion")String direccion, @RequestParam("dateStr")String fecha){

        usuario.setDireccion(direccion);
        usuario.setNumerotelefonico(numerotelefonico);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            usuario.setFechanacimiento(formatter.parse(fecha));
        }catch (ParseException e){
            e.printStackTrace();
        }
        usuarioRepository.save(usuario);
        return "redirect:/perfil";
    }


    @GetMapping("/historial")
    public String historialCompra(){
        return "usuario/historial";
    }

    @GetMapping("/calificacion")
    public String calificacion(){
        return "usuario/calificacion";
    }


}