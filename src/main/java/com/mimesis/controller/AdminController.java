package com.mimesis.controller;

import com.mimesis.entity.*;
import com.mimesis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

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
    public String paginaAgregarsalas( @ModelAttribute("sala") Sala sala, Model model){
        List<Sede> sedeList=sedesRepository.findAll();
        model.addAttribute("sedeList",sedeList);
        return "admin/agregarsalas";
    }

    @PostMapping("/savesalas")
    public String savesalas(@ModelAttribute("sala") @Valid Sala sala, BindingResult bindingResult,Model model, RedirectAttributes attr){

        if(bindingResult.hasErrors()) {
            if(sala.getId()==null){
                List<Sede> sedeList = sedesRepository.findAll();
                model.addAttribute("sedeList", sedeList);
                return "admin/agregarsalas";
            }else{
                List<Sede> sedeList = sedesRepository.findAll();
                model.addAttribute("sedeList", sedeList);
                return "admin/editarsalas";
            }

        }else {
            List<Sala> listaSala = salasRepository.findAll();
            String result = sala.getNombre().replace(" ", "");
            for(Sala i : listaSala) {
                String result2 = i.getNombre().replace(" ", "");
                if (result.equalsIgnoreCase(result2) && sala.getIdsede() == i.getIdsede()) {
                    attr.addFlashAttribute("msg","La sala ya ha sido creada previamente");
                    attr.addFlashAttribute("opcion","alert-danger");
                    return "redirect:/admin/salas";
                }
            }
            String msg ="sala " + (sala.getId()== null ? "creada " : "actualizada ") + "exitosamente";
            attr.addFlashAttribute("msg", msg);
            attr.addFlashAttribute("opcion","alert-success");
            salasRepository.save(sala);
            return "redirect:/admin/salas";
        }

    }

    @GetMapping("/editarsalas")
    public String paginaEditarsalas(@ModelAttribute("sala") Sala sala, @RequestParam("id") Integer id, Model model){
        Optional<Sala> optionalSala = salasRepository.findById(id);
        if(optionalSala.isPresent()){
            sala = optionalSala.get();
            model.addAttribute("sala",sala);
            List<Sede> sedeList=sedesRepository.findAll();
            model.addAttribute("sedeList",sedeList);
            return "admin/editarsalas";
        } else {
            return "redirect:/admin/salas";
        }
    }

    @GetMapping("/borrar")
    public  String borrar(@RequestParam("id") Integer id, RedirectAttributes attr){
        Optional<Sala> optionalSala = salasRepository.findById(id);
        if(optionalSala.isPresent()){
            List<Integer> listaSalas = salasRepository.obtenerIdFuncion();
            for(int i: listaSalas){
                if(i == id){
                    attr.addFlashAttribute("msg","La sala presenta funciones pendientes");
                    attr.addFlashAttribute("opcion","alert-danger");
                    return "redirect:/admin/salas";
                }
            }
            Sala salaModo = optionalSala.get();
            salaModo.setValido(false);
            salasRepository.save(salaModo);
            attr.addFlashAttribute("msg","Sala borrada exitosamente");
            attr.addFlashAttribute("opcion","alert-danger");
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

    @RequestMapping("/agregarsedes")
    public String paginaAgregarsedes(@ModelAttribute("sede") Sede sede,@ModelAttribute("foto") Foto foto,Model model){
        List<Sede> sedeList=sedesRepository.findAll();
        model.addAttribute("sedeList",sedeList);
        List<Foto> fotoList=fotoRepository.findAll();
        model.addAttribute("fotoList",fotoList);
        return "admin/agregarsedes";
    }

    @PostMapping("/savesedes")
    public String savesedes(@ModelAttribute("sede") @Valid Sede sede,BindingResult bindingResult, @RequestParam("archivo") List<MultipartFile> file,Model model, RedirectAttributes attr) throws IOException {

        if (bindingResult.hasErrors() || file.get(0).getBytes().length == 0){
            if (file.get(0).getBytes().length == 0) {
                model.addAttribute("errorFoto", "Debe adjuntar al menos una foto");
            }
            if (sede.getId() == null) {
                return "admin/agregarsedes";
            } else {

                return "admin/editarsedes";
            }
        }else {
            sedesRepository.save(sede);
            Collections.reverse(file);
            try {
                for (MultipartFile file1 : file) {
                    Foto foto = new Foto();
                    foto.setFoto(file1.getBytes());
                    foto.setIdsede(sede);
                    String msg = "sede " + (foto.getId() == null ? "creada " : "actualizada  ") + "exitosamente";
                    attr.addFlashAttribute("msg", msg);
                    attr.addFlashAttribute("opcion", "alert-success");
                    fotoRepository.save(foto);
                    return "redirect:/admin/sedes";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "admin/agregarsedes";
        }
    }

    @PostMapping("/savesedesNoImagen")
    public String saveship(@ModelAttribute("sede") @Valid Sede sede,BindingResult bindingResult, RedirectAttributes attr){
        if(bindingResult.hasErrors()){
            return "admin/editarsedes";
        }
        String msg = "sede " + (sede.getId() == null ? "creada " : "actualizada  ") + "exitosamente";
        attr.addFlashAttribute("msg", msg);
        attr.addFlashAttribute("opcion", "alert-success");
        sedesRepository.save(sede);
        return "redirect:/admin/sedes";
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

    @GetMapping("/actoresydirectores")
    public String paginaActoresydirectores(Model model) {
        String rol ="Actores" ;
        model.addAttribute("rol",rol);
        model.addAttribute("listaActores", actorRepository.findAll());
        model.addAttribute("listaDirectores", directorRepository.findAll());

        //Diccionario
        //Dictionary<Integer, String> obj = new Hashtable<Integer,String>();
        //obj.put(0,"Actores");
        //obj.put(1,"Directores");
        //model.addAttribute("obj",obj);

        //Lista
        //ArrayList<String> listaRoles = new ArrayList<>();
        //listaRoles.add("Actores");
        //listaRoles.add("Directores");
        //model.addAttribute("listaRoles",listaRoles);

        return "admin/actoresydirectores";
    }

    @PostMapping("/searchroles")
    public String buscarporRoles(Model model,@RequestParam(value = "rol", required = false) String rol){

        if(rol.equalsIgnoreCase("Actores")){
            model.addAttribute("rol",rol);
            model.addAttribute("listaActores", actorRepository.findAll());
        }else{
            String rol1 ="Directores" ;
            model.addAttribute("rol",rol1);
            model.addAttribute("listaDirectores", directorRepository.findAll());
        }


        return "admin/actoresydirectores";
    }

    @RequestMapping("agregaractoresydirectores")
    public String paginaAgregaractoresydirectores(){
        return "admin/agregaractoresydirectores";
    }

    @RequestMapping("editaractoresydirectores")
    public String paginaEditaractoresydirectores(){
        return "admin/editaractoresydirectores";
    }


    //////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping("clientes")
    public String clientes(Model model){
        model.addAttribute("listaClientes",usuarioRepository.findByRol("Cliente"));
        model.addAttribute("listasedes",sedesRepository.findAll());
        return "admin/clientes";
    }

    @PostMapping("/searchclientes")
    public String buscarcliente(Model model,@RequestParam("sedes") Sede sedes){
        model.addAttribute("listasedes",sedesRepository.findAll());
        model.addAttribute("salaList",sedes.getListasalas());
        return "admin/clientes";
    }
    /////////////////////////////////////////////////////////////////////////////////////////

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
    public String agregarOperador(@ModelAttribute("usuario") Usuario usuario, Model model){

        return "admin/agregaroperador";
    }

    @PostMapping("/saveoperador")
    public String savesedes(@ModelAttribute("usuario") @Valid Usuario usuario,BindingResult bindingResult, RedirectAttributes attr){
        if(bindingResult.hasErrors()){
            return "admin/agregaroperador";
        }
        usuarioRepository.save(usuario);
        attr.addFlashAttribute("msg","Operador creado exitosamente");
        attr.addFlashAttribute("opcion","alert-success");
        return "redirect:/admin/operadores";
    }

    @GetMapping("/borraroperador")
    public String borraroperador(@RequestParam("id") Integer id, RedirectAttributes attr){
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if(optionalUsuario.isPresent()){
            usuarioRepository.deleteById(id);
            attr.addFlashAttribute("msg","Operador borrado exitosamente");
            attr.addFlashAttribute("opcion","alert-danger");
        }
        return "redirect:/admin/operadores";
    }

}
