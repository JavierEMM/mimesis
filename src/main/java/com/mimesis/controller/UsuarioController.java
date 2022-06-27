package com.mimesis.controller;

import com.mimesis.dto.*;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    @Autowired
    ObrasRepository obrasRepository;

    @Autowired
    CalificacionRepository calificacionRepository;



    @GetMapping(value={"","/"})
    public String paginaPrincipal(Model model, HttpSession session, Authentication auth, RedirectAttributes attr) {
        String correo = (String) session.getAttribute("usuario");
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null) {
            if(usuario.getAuthprovider().equals("GOOGLE")){
               if(usuario.getRol().equals("Admin") || usuario.getRol().equals("Operador")){
                   auth.setAuthenticated(false);
                   session.invalidate();
                   attr.addFlashAttribute("mensaje","Ingrese por el usuario y contraseña establecido por la empresa");
                   return "redirect:/login";
               }
            }
            model.addAttribute("cliente", usuario);
            if (usuario.getId() == null) {
                return "login/registrogoogle";

            } else {
                model.addAttribute("cliente", usuario);
                return "usuario/main";
            }
        }
        return "usuario/main";
    }

    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session){
        String usuario2 = (String) session.getAttribute("usuario");
        Usuario usuario = usuarioRepository.findByCorreo(usuario2);
        model.addAttribute("cliente", usuario);
        if(usuario.getFotoperfil()==null){
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
    public String guardarPerfil(@RequestParam("archivo") MultipartFile file, Usuario usuario,
                                @RequestParam("direccion")String direccion,
                                @RequestParam("numerotelefonico")String tel,
                                Model model){

        if(file.isEmpty()){
            System.out.println("AQUI 1");
            model.addAttribute("msg", "Debe subir una imagen");
            return "usuario/editarperfil";
        }
        try{
            usuario.setFotoperfil(file.getBytes());
            usuario.setDireccion(direccion);
           //usuario.setFechanacimiento(fechanacimiento);
            usuario.setNumerotelefonico(tel);
            usuarioRepository.save(usuario);
            return "redirect:/perfil";
        } catch (IOException e){
            e.printStackTrace();
            model.addAttribute("msg", "ocurrio un error al subir el archivo");
            return "usuario/editarperfil";
        }
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(HttpSession session, Model model){
        String usuario2 = (String) session.getAttribute("usuario");
        Usuario usuario = usuarioRepository.findByCorreo(usuario2);
        model.addAttribute("usuario", usuario);
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
    public String historialCompra(Model model, HttpSession session,@RequestParam(value="search",required = false) String search,
                                  @RequestParam(value="categoria",required = false) String categoria,
                                  @RequestParam(value="FechaInicio",required = false) String optFechaInicio, @RequestParam(value="FechaFin",required = false)String optFechaFin){
        String usuario1 = (String) session.getAttribute("usuario");
        Usuario usuario2 = usuarioRepository.findByCorreo(usuario1);

        if(search != null) {
            if (categoria.equalsIgnoreCase("Nombre")) {
                model.addAttribute("listaHistorial", usuarioRepository.ObtenerHistorialporObra(usuario2.getId(), search));
            }
            if (categoria.equalsIgnoreCase("Sede")) {
                model.addAttribute("listaHistorial", usuarioRepository.ObtenerHistorialporSede(usuario2.getId(), search));
            }
            if(categoria.equalsIgnoreCase("Fecha")){
                model.addAttribute("listaHistorial",usuarioRepository.ObtenerHistorialporFecha(usuario2.getId(),optFechaInicio,optFechaFin));
            }


        }else{
            List<DTOHistorial> listaHistorial = usuarioRepository.ObtenerHistorial(usuario2.getId());
            List<DTOHistorialporBoleto> listaHistorialporBoleto = new ArrayList<>();
            for (DTOHistorial i: listaHistorial){
                Integer idObras = i.getIdobras();
                Integer directorId = i.getDirectorid();
                String nombreObra = i.getNombreobra();
                LocalDate fecha = i.getFecha();
                LocalTime horaInicio = i.getHorainicio();
                LocalTime horaFin = i.getHorafin();
                Integer cantidad = i.getCantidad();
                String nombreSede = i.getNombresede();
                String nombreSala = i.getNombresala();
                String estado = i.getEstado();
                String costoTotal = i.getCostototal();
                Integer funcionId = i.getFuncionid();
                DTOHistorialporBoleto boleto = new DTOHistorialporBoleto(idObras,directorId,nombreObra,fecha,horaInicio,horaFin,cantidad,nombreSede,nombreSala,estado,costoTotal,funcionId);
                List<Integer> listaIdboleto = boletoRepository.boletosPorUsuarioFuncion(usuario2.getId(),funcionId);
                boleto.setListadeIdBoleto(listaIdboleto);
                listaHistorialporBoleto.add(boleto);
            }
            model.addAttribute("listaHistorialporBoleto", listaHistorialporBoleto);
        }
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
    public String calificacion(Model model, HttpSession session, @RequestParam(value = "idfuncion",required = false)Integer idfuncion,
                               @RequestParam(value = "idobras",required = false)Integer idobras,
                               @RequestParam(value = "directorid",required = false)Integer directorid){
        String usuario = (String) session.getAttribute("usuario");
        Usuario usuario2 = usuarioRepository.findByCorreo(usuario);

        DTOCalificacionObra dtoCalificacionObra = new DTOCalificacionObra();
        dtoCalificacionObra.setIdfuncion(idfuncion);
        Obra obra = obrasRepository.getById(idfuncion);
        dtoCalificacionObra.setNombreobra(obra.getNombre());
        dtoCalificacionObra.setIdobra(obra.getId());


        DTOCalificacionDirector dtoCalificacionDirector = new DTOCalificacionDirector();
        dtoCalificacionDirector.setIdfuncion(idfuncion);
        Director director = directorRepository.getById(idfuncion);
        dtoCalificacionDirector.setNombredirector(director.getNombre());
        dtoCalificacionDirector.setApellidodirector(director.getApellido());
        dtoCalificacionDirector.setCorreodirector(director.getCorreo());
        dtoCalificacionDirector.setIddirector(director.getId());

        List<DTOCalificacionActor> listaactor = usuarioRepository.ObtenerCalificacionActor(usuario2.getId(), idfuncion);

        model.addAttribute("obra",dtoCalificacionObra);
        model.addAttribute("director", dtoCalificacionDirector);
        model.addAttribute("listaactor", listaactor);

        return "usuario/calificacion";
    }

    @PostMapping("/guardarcalificacion")
    public String guardarCalificacionObra(@RequestParam("idcalificaciones")Integer calificacionobra,
                              @RequestParam("idusuario")Usuario idusuario,
                              @RequestParam("idfuncion")Funcion idfuncion,
                              Model model){
        System.out.println("llegamos aqui");
        System.out.println(idfuncion);
        System.out.println(idusuario);


        DTOCalificacionObra dtoCalificacionObra= new DTOCalificacionObra();
        System.out.println(dtoCalificacionObra.getCalificacion());
        dtoCalificacionObra.setCalificacion(calificacionobra);


        Calificacion calificacion = new Calificacion();
        calificacion.setCalificacion(dtoCalificacionObra.getCalificacion());

        calificacion.setIdfuncion(idfuncion);
        calificacion.setIdusuario(idusuario);

        calificacionRepository.save(calificacion);

        System.out.println("se guardo");
        return "redirect:/historial";
    }

    @GetMapping("/borrarboleto")
    public  String borrar(@RequestParam("idboleto") String ids, RedirectAttributes attr){

        //for(int i: listaIdBoleto){
            //Optional<Boleto> optionalBoleto = boletoRepository.findById(i);
            //if(optionalBoleto.isPresent()){
                //boletoRepository.deleteById(i);
            //}
            //System.out.println(i);
        //}

        return "redirect:/historial";
    }

}