package com.mimesis.controller;

import com.mimesis.dto.*;
import com.mimesis.entity.*;
import com.mimesis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.thymeleaf.util.StringUtils.length;

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


    @GetMapping(value = {"", "/"})
    public String paginaPrincipal(Model model, HttpSession session, Authentication auth, RedirectAttributes attr) {
        String correo = (String) session.getAttribute("usuario");
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null) {
            if (usuario.getAuthprovider().equals("GOOGLE")) {
                if (usuario.getRol().equals("Admin") || usuario.getRol().equals("Operador")) {
                    auth.setAuthenticated(false);
                    session.invalidate();
                    attr.addFlashAttribute("mensaje", "Ingrese por el usuario y contraseña establecido por la empresa");
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
    public String perfil(Model model, HttpSession session) {
        String usuario2 = (String) session.getAttribute("usuario");
        Usuario usuario = usuarioRepository.findByCorreo(usuario2);
        model.addAttribute("cliente", usuario);
        if (usuario.getFotoperfil() == null) {
            return "usuario/perfil";
        }
        return "usuario/perfil";
    }

    @GetMapping("/perfil1")
    public String perfil1(Model model, @RequestParam(value = "id", required = false) Integer id) {
        System.out.println(id);
        model.addAttribute("lista", usuarioRepository.findById(id));
        return "usuario/perfil";
    }


    @PostMapping("/perfil/save")
    public String guardarPerfil(@ModelAttribute("usuario") @Valid Usuario usuario3, BindingResult bindingResult , Model model , @RequestParam("archivo") MultipartFile file,
                            HttpSession session, RedirectAttributes attr) {

        //String usuario2 = (String) session.getAttribute("usuario");
        Usuario usuario = usuarioRepository.findByCorreo(usuario3.getCorreo());
        System.out.println(usuario3.getNumerotelefonico());
        System.out.println("Jose");
        if(bindingResult.hasErrors()){
            System.out.println(usuario3.getNumerotelefonico());
            System.out.println("Jose");
            if(usuario3.getDireccion().equals("") || usuario3.getNumerotelefonico().equals("")) {
                if (usuario3.getNumerotelefonico().equals("")) {
                    attr.addFlashAttribute("msg", "Debe ingresar un número telefónico");
                    attr.addFlashAttribute("opcion", "alert-danger");
                    return "redirect:/perfil/editar";
                } else {
                    attr.addFlashAttribute("msg", "Debe ingresar una dirección");
                    attr.addFlashAttribute("opcion", "alert-danger");
                    return "redirect:/perfil/editar";
                }
            }

            return "redirect:/perfil/editar";
        }
        if(usuario3.getNumerotelefonico().length()!=9){
            attr.addFlashAttribute("msg","Debe ingresar un número de 9 dígitos");
            attr.addFlashAttribute("opcion","alert-danger");
            return "redirect:/perfil/editar";
        }

        try {
            if(file.getOriginalFilename().equals("") || file.getOriginalFilename().equalsIgnoreCase(null) ){
                System.out.println("AQUI 1");
            }else{
                System.out.println("AQUI 2");
                usuario.setFotoperfil(file.getBytes());
            }
            usuario.setDireccion(usuario3.getDireccion());
            usuario.setFechanacimiento(usuario3.getFechanacimiento());
            usuario.setNumerotelefonico(usuario3.getNumerotelefonico());
            usuarioRepository.save(usuario);
            return "redirect:/perfil";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("msg", "ocurrio un error al subir el archivo");
            return "usuario/editarperfil";
        }
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(HttpSession session, Model model) {
        String usuario2 = (String) session.getAttribute("usuario");
        Usuario usuario = usuarioRepository.findByCorreo(usuario2);
        model.addAttribute("usuario", usuario);
        return "usuario/editarperfil";
    }

    @GetMapping("image/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") int id) {
        Optional<Usuario> opt = usuarioRepository.findById(id);

        if (opt.isPresent()) {
            Usuario u = opt.get();

            byte[] imagenComoBytes = u.getFotoperfil();

            return new ResponseEntity<>(imagenComoBytes, HttpStatus.OK);
        } else {
            return null;
        }
    }

    @GetMapping("perfil/google")
    public String perfilGoogle() {
        return "usuario/perfilprimeravez";
    }



    @GetMapping("/historial")
    public String historialCompra(Model model, HttpSession session, @RequestParam(value = "search", required = false) String search,
                                  @RequestParam(value = "categoria", required = false) String categoria,
                                  @RequestParam(value = "FechaInicio", required = false) String optFechaInicio, @RequestParam(value = "FechaFin", required = false) String optFechaFin) {
        String usuario1 = (String) session.getAttribute("usuario");
        Usuario usuario2 = usuarioRepository.findByCorreo(usuario1);
        List<DTOHistorial> listaHistorial = new ArrayList<>();
        if (search != null) {
            if (categoria.equalsIgnoreCase("Nombre")) {
                listaHistorial = usuarioRepository.ObtenerHistorialporObra(usuario2.getId(),search);
            }
            if (categoria.equalsIgnoreCase("Sede")) {
                listaHistorial= usuarioRepository.ObtenerHistorialporSede(usuario2.getId(),search);
            }
            if (categoria.equalsIgnoreCase("Fecha")) {
                listaHistorial = usuarioRepository.ObtenerHistorialporFecha(usuario2.getId(), optFechaInicio, optFechaFin);
            }
            if(categoria.equalsIgnoreCase("Asistido")){
                listaHistorial = usuarioRepository.ObtenerHistorialporEstado(usuario2.getId(),0);
                System.out.println(listaHistorial.size());
            }
            if(categoria.equalsIgnoreCase("Pendiente")){

                listaHistorial = usuarioRepository.ObtenerHistorialporEstado(usuario2.getId(),1);
            }
        } else {
            listaHistorial = usuarioRepository.ObtenerHistorial(usuario2.getId());
        }
        List<DTOHistorialporBoleto> list = new ArrayList<>();
        for (DTOHistorial i :listaHistorial){
            DTOHistorialporBoleto dtoHistorialporBoleto =  new DTOHistorialporBoleto();
            Optional<Funcion> funcion =  funcionRepository.findById(i.getIdfuncion());
            if(funcion.isPresent()){
                dtoHistorialporBoleto.setFuncion(funcion.get());
                dtoHistorialporBoleto.setCantidad(i.getCantidad());
                dtoHistorialporBoleto.setCostoTotal(i.getCostototal());
                dtoHistorialporBoleto.setBoletoValido((i.getEstado() == 1 ? true : false));
                dtoHistorialporBoleto.setValidar(restarFechas(funcion.get().getFecha(),funcion.get().getHorainicio(),2));
                list.add(dtoHistorialporBoleto);
            }else{
                model.addAttribute("error","error");
                return "usuario/historial";
            }
            model.addAttribute("listaHistorialporBoleto", dtoHistorialporBoleto);
        }

        model.addAttribute("listaHistorial2",list);
        return"usuario/historial";
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
        Optional<Obra> obra = obrasRepository.findById(idobras);
        if(obra.isPresent()){
            dtoCalificacionObra.setNombreobra(obra.get().getNombre());
            dtoCalificacionObra.setIdobra(obra.get().getId());
        }else{

            return "redirect:/historial";
        }

        DTOCalificacionDirector dtoCalificacionDirector = new DTOCalificacionDirector();
        dtoCalificacionDirector.setIdfuncion(idfuncion);
        Optional<Director> director = directorRepository.findById(directorid);
        if(director.isPresent()){
            dtoCalificacionDirector.setNombredirector(director.get().getNombre());
            dtoCalificacionDirector.setApellidodirector(director.get().getApellido());
            dtoCalificacionDirector.setCorreodirector(director.get().getCorreo());
            dtoCalificacionDirector.setIddirector(director.get().getId());
        }

        List<DTOCalificacionActor> listaactor = usuarioRepository.ObtenerCalificacionActor(usuario2.getId(), idfuncion);

        model.addAttribute("obra",dtoCalificacionObra);
        model.addAttribute("director", dtoCalificacionDirector);
        model.addAttribute("listaactor", listaactor);

        return "usuario/calificacion";
    }

    @PostMapping("/guardarcalificacion")
    public String guardarCalificacionObra(@RequestParam("idfuncion") Integer idfuncion,
                                          @RequestParam("idcalificaciones") Integer calificacionobra,
                                          @RequestParam("director") Integer iddirector,
                                          @RequestParam("califidirector") Integer calificaciondirector,
                                          @RequestParam("actor") List<Integer> idactor,
                                          @RequestParam(value = "caliactores") List<Integer> listaCalificaciones,
                                        Model model, HttpSession session, RedirectAttributes attributes){

        String usuario = (String) session.getAttribute("usuario");
        Usuario usuario2 = usuarioRepository.findByCorreo(usuario);

        Optional<Funcion> funcion = funcionRepository.findById(idfuncion);
        List<Calificacion> calificacions = calificacionRepository.findUsuario(usuario2.getId());
        if(calificacions.isEmpty() || calificacions == null){
            if(funcion.isPresent()){
                DTOCalificacionObra dtoCalificacionObra= new DTOCalificacionObra();
                System.out.println(dtoCalificacionObra.getCalificacion());
                dtoCalificacionObra.setCalificacion(calificacionobra);

                Calificacion calificacion = new Calificacion();
                calificacion.setCalificacion(dtoCalificacionObra.getCalificacion());

                calificacion.setIdfuncion(funcion.get());
                calificacion.setIdusuario(usuario2);

                calificacionRepository.save(calificacion);
            }
            Optional<Director> director = directorRepository.findById(iddirector);
            if(director.isPresent()){
                Calificacion calificacion = new Calificacion();
                calificacion.setCalificacion(calificaciondirector);
                calificacion.setIddirector(director.get());
                calificacion.setIdfuncion(funcion.get());
                calificacion.setIdusuario(usuario2);
                calificacionRepository.save(calificacion);
            }else {
                return "redirect:/historial";
            }
            List<Actor> actores= actorRepository.findAllById(idactor);
            int i = 0;
            for(Actor actor : actores){
                Calificacion calificacion =  new Calificacion();
                calificacion.setCalificacion(listaCalificaciones.get(i));
                calificacion.setIdactor(actor);
                calificacion.setIdfuncion(funcion.get());
                calificacion.setIdusuario(usuario2);
                calificacionRepository.save(calificacion);
                i++;
            }
        }else{
            System.out.println("llego:");
            System.out.println(calificacions.size());
            model.addAttribute("listacalificaciones",calificacions);
            attributes.addFlashAttribute("alerta","alert-danger");
            attributes.addFlashAttribute("reserva","No se puede calificar de nuevo");
        }
        return "redirect:/historial";
    }

    @PostMapping("/borrarboleto")
    public  String borrar(@RequestParam("idfuncion") Integer ids, RedirectAttributes attr, HttpSession session){
        String correo = (String)  session.getAttribute("usuario");
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        List<Boleto> boletos = boletoRepository.boletoFuncionyUsuario(ids,usuario.getId());
        boletoRepository.deleteAll(boletos);
        return "redirect:/historial";
    }

    public Boolean restarFechas(LocalDate fecha, LocalTime hora,Integer rango){
        Integer anho = fecha.getYear();
        Integer mes = fecha.getMonth().getValue();
        Integer day = fecha.getDayOfMonth();
        Integer horas = hora.getHour();
        LocalDateTime fecha_actual = LocalDateTime.now();
        Boolean respuesta = true;
        if(anho-fecha_actual.getYear() >= 0){
            if (anho-fecha_actual.getYear() > 0){
                respuesta = true;
            }else if(anho-fecha_actual.getYear() == 0){
                if((mes-fecha_actual.getMonth().getValue()) >= 0){
                    if(mes-fecha_actual.getMonth().getValue() == 0){
                        if(day-fecha_actual.getDayOfMonth() >= rango){
                            if(day-fecha_actual.getDayOfMonth() == rango){
                                if(horas - fecha_actual.getHour()>=0){
                                    if(horas-fecha_actual.getHour() == 0){
                                        respuesta = false;
                                    }else{
                                        respuesta = true;
                                    }
                                }else{
                                    respuesta = false;
                                }
                            }else{
                                respuesta = true;
                            }
                        }else{
                            respuesta = false;
                        }
                    }else{
                        respuesta = true;
                    }
                }else{
                    respuesta =false;
                }
            }
        }
        return respuesta;
    }
}