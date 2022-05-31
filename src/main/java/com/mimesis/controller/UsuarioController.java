package com.mimesis.controller;

import com.mimesis.dto.DTOcarrito;
import com.mimesis.entity.Usuario;
import com.mimesis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

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
    public String perfil(){
        return "usuario/perfil";
    }
    @GetMapping("/perfil1")
    public String perfil1(Model model, @RequestParam(value = "id",required = false) Integer id){
        System.out.println(id);
        model.addAttribute("lista",usuarioRepository.findById(id));
        return "usuario/perfil";
    }


    @PostMapping("/perfil/save")
    public String guardarPerfil(@RequestParam("archivo")MultipartFile file, Usuario usuario,
                                @RequestParam("direccion")String direccion,
                                @RequestParam("numerotelefonico")String tel,
                                Model model, HttpSession session){

        if(file.isEmpty()){
            model.addAttribute("msg", "Debe subir una imagen");
            return "usuario/editarperfil";
        }


        try{
            usuario.setFotoperfil(file.getBytes());
            usuario.setDireccion(direccion);
           // usuario.setFechanacimiento(fechanacimiento);
            usuario.setNumerotelefonico(tel);

            usuarioRepository.save(usuario);
            session.setAttribute("usuario",usuario);
            Integer usuario1 = usuario.getId();

            return "redirect:/perfil1?id="+usuario1;
        } catch (IOException e){
            e.printStackTrace();
            model.addAttribute("msg", "ocurrio un error al subir el archivo");
            return "usuario/perfilprimeravez";
        }
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(@RequestParam("id")Integer id, Model model){

        Optional<Usuario> user = usuarioRepository.findById(id);
        model.addAttribute("usuario", user.get());
        return "usuario/editarperfil";
    }

    @GetMapping("image/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") int id,HttpSession session){
        Optional<Usuario> opt = usuarioRepository.findById(id);
        System.out.println(opt);
        System.out.println("aqui");
        System.out.println(id);
        if(opt.isPresent()){
            Usuario u = opt.get();

            byte[] imagenComoBytes = u.getFotoperfil();
            System.out.println(imagenComoBytes);
            System.out.println(id);
            return new ResponseEntity<>(imagenComoBytes, HttpStatus.OK);
        }else{
            return null;
        }
    }

    @GetMapping("perfil/google")
    public String perfilGoogle(){


        return "usuario/perfilprimeravez";
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