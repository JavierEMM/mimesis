package com.mimesis.controller;

import com.mimesis.entity.Usuario;
import com.mimesis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
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

    @PostMapping("/perfil/save")
    public String guardarPerfil(@RequestParam("archivo")MultipartFile file, Usuario usuario,
                                @RequestParam("direccion")String direccion,
                                @RequestParam("dni")Integer dni,
                                Model model){

        if(file.isEmpty()){
            model.addAttribute("msg", "Debe subir una imagen");
            return "usuario/perfil";
        }

        try{
            usuario.setFotoperfil(file.getBytes());
            usuario.setDireccion(direccion);
           // usuario.setFechanacimiento(fechanacimiento);
            usuario.setDni(dni);

            usuarioRepository.save(usuario);
            return "usuario/perfil";
        } catch (IOException e){
            e.printStackTrace();
            model.addAttribute("msg", "ocurrio un error al subir el archivo");
            return "usuario/perfilprimeravez";
        }
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(@RequestParam("id")Integer id, Model model, RedirectAttributes attributes){

        Optional<Usuario> user = usuarioRepository.findById(id);
        model.addAttribute("usuario", user.get());
        return "usuario/editarperfil";
    }

    @GetMapping("image/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") int id){
        Optional<Usuario> opt = usuarioRepository.findById(id);
        if(opt.isPresent()){
            Usuario u = opt.get();
            byte[] imagenComoBytes = u.getFotoperfil();
            return new ResponseEntity<>(imagenComoBytes, HttpStatus.OK);
        }else{
            return null;
        }
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