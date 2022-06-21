package com.mimesis.controller;

import com.mimesis.dto.DTOCalificacionActor;
import com.mimesis.dto.DTOCalificacionDirector;
import com.mimesis.dto.DTOCalificacionObra;
import com.mimesis.dto.DTOHistorial;
import com.mimesis.entity.*;
import com.mimesis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("")
public class UsuarioController {
    @Autowired
    FotoRepository fotoRepository;
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

    @Autowired
    BoletoRepository boletoRepository;


    @GetMapping(value={"","/"})
    public String paginaPrincipal(@ModelAttribute("usuario") Usuario usuario, Model model, HttpSession session, Authentication auth, RedirectAttributes attr) {
        Usuario usuario1 = (Usuario) session.getAttribute("usuario");
        if (usuario1 != null) {
            if(usuario1.getAuthprovider().equals("GOOGLE")){
               if(usuario1.getRol().equals("Admin") || usuario1.getRol().equals("Operador")){
                   auth.setAuthenticated(false);
                   session.invalidate();
                   attr.addFlashAttribute("mensaje","Ingrese por el usuario y contraseña establecido por la empresa");
                   return "redirect:/login";
               }
            }
            model.addAttribute("cliente", usuario1);
            if (usuario1.getId() == null) {
                return "login/registrogoogle";

            } else {
                model.addAttribute("cliente", usuario1);
                return "usuario/main";
            }
        }
        return "usuario/main";
    }

    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session){
        Usuario usuario2 = (Usuario) session.getAttribute("usuario");
        model.addAttribute("Cliente", usuario2);
        if(usuario2.getFotoperfil()==null){
            return "usuario/perfil";
        }
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
           //usuario.setFechanacimiento(fechanacimiento);
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

    @GetMapping("perfil/google")
    public String perfilGoogle(){


        return "usuario/perfilprimeravez";
    }



    @GetMapping("/historial")
    public String historialCompra(Model model, HttpSession session){
        Usuario usuario2 = (Usuario) session.getAttribute("usuario");
        System.out.println(usuario2.getId());
        model.addAttribute("listaHistorial", usuarioRepository.ObtenerHistorial(usuario2.getId()));

        return "usuario/historial";
    }
    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> imagesMostrar(@PathVariable("id") int id){
        List<Foto> fotos = fotoRepository.listaFotos(id);
        if(fotos != null){
            return new ResponseEntity<>(
                    fotos.get(0).getFoto(),HttpStatus.OK
            );
        }else{
            return null;
        }
    }

    @GetMapping("/calificacion")
    public String calificacion(Model model, HttpSession session, @RequestParam(value = "idfuncion",required = false) Integer idfuncion){
        Usuario usuario2 = (Usuario) session.getAttribute("usuario");

        System.out.println(usuario2.getId());
        System.out.println(idfuncion);
        List<DTOCalificacionObra> listaobras = usuarioRepository.ObtenerCalificacionObra(usuario2.getId(),idfuncion);

        List<DTOCalificacionDirector> listadirector = usuarioRepository.ObtenerCalificacionDirector(usuario2.getId(), idfuncion);
        List<DTOCalificacionActor> listaactor = usuarioRepository.ObtenerCalificacionActor(usuario2.getId(), idfuncion);
        System.out.println("hola");
        System.out.println(listadirector.size());
        System.out.println("adios");
        model.addAttribute("listaobras",listaobras);
        model.addAttribute("listadirector", listadirector);
        model.addAttribute("listaactor", listaactor);

        return "usuario/calificacion";
    }



}