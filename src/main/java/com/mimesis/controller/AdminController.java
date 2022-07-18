package com.mimesis.controller;

import com.mimesis.dao.DniDao;
import com.mimesis.dto.DTOCompararID;
import com.mimesis.entity.*;
import com.mimesis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    DniDao dniDao;

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
                    if(i.getValido()){
                        attr.addFlashAttribute("msg","La sala ya ha sido creada previamente");
                        attr.addFlashAttribute("opcion","alert-danger");
                        return "redirect:/admin/salas";
                    }

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
    public String savesedes(@ModelAttribute("sede") @Valid Sede sede,BindingResult bindingResult, @RequestParam("files[]") List<MultipartFile> file,Model model, RedirectAttributes attr) throws IOException {

        System.out.println(bindingResult.getAllErrors());

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
            List<Sede> listaSede =sedesRepository.sedesvalidas();
            String result = sede.getNombre().replace(" ", "");
            String resultUbicacion =sede.getUbicacion().replace(" ","").replace(".","");
            for(Sede i: listaSede){
                String result2 = i.getNombre().replace(" ", "");
                String resultUbicacion2 =i.getUbicacion().replace(" ","").replace(".","");
                if (result.equalsIgnoreCase(result2) && resultUbicacion.equalsIgnoreCase(resultUbicacion2)) {
                    if(i.getValido()){
                        if(sede.getId() == null){
                            attr.addFlashAttribute("msg","La sede ya ha sido creada previamente");
                            attr.addFlashAttribute("opcion","alert-danger");
                            return "redirect:/admin/sedes";
                        }
                    }

                }
            }
            try {
                for (MultipartFile file1 : file) {
                    System.out.println(file1.getClass().getSimpleName());
                    Foto foto = new Foto();
                    foto.setFoto(file1.getBytes());
                    foto.setIdsede(sede);
                    String msg = "sede " + (sede.getId() == null ? "creada " : "actualizada  ") + "exitosamente";
                    attr.addFlashAttribute("msg", msg);
                    attr.addFlashAttribute("opcion", "alert-success");
                    sedesRepository.save(sede);
                    fotoRepository.save(foto);
                }
                return "redirect:/admin/sedes";

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
            Integer tamanio =(opt.get().getFotosporsede().size() -1 );
            System.out.println("JOSEEE");
            System.out.println(tamanio);
            byte[] imagenComoBytes = sedefoto.getFotosporsede().get(tamanio).getFoto();
            return new ResponseEntity<>(
                    imagenComoBytes,
                    HttpStatus.OK);
        }else{
            return null;
        }
    }

    @GetMapping("/editarsedes")
    public String paginaEditarsedes(@RequestParam("id") Integer id,Model model){
        Optional<Sede> optionalSede = sedesRepository.findById(id);
        if(optionalSede.isPresent()){
            Sede sede = optionalSede.get();
            model.addAttribute("sede",sede);
            List<Foto> fotoList = fotoRepository.listaFotosxSede(id);
            model.addAttribute("files",fotoList);
            return "admin/editarsedes";
        } else {
            return "redirect:/admin/sedes";
        }
    }

    @GetMapping("/borrarsede")
    public  String borrarsede(@RequestParam("id") Integer id, RedirectAttributes attr){
        Optional<Sede> optionalSede = sedesRepository.findById(id);
        if(optionalSede.isPresent()){
            List<Integer> listaSedes = salasRepository.sedesconsalas();
            for(int i : listaSedes){
                if(i==id){
                    List<Integer> listaSalas1 = sedesRepository.compararIds(i);
                    for( int k : listaSalas1){
                        Optional<Sala> optionalSala = salasRepository.findById(k);
                        if(optionalSala.isPresent()){
                            List<Integer> listaSalas2 = salasRepository.obtenerIdFuncion();
                            System.out.println(listaSalas2);
                            for(int p: listaSalas2){
                                if(p == k){
                                    attr.addFlashAttribute("msg","La sede presenta salas con funciones pendientes");
                                    attr.addFlashAttribute("opcion","alert-danger");
                                    return "redirect:/admin/sedes";
                                }
                            }
                        }
                    }
                }
            }
            Sede sedeModo = optionalSede.get();
            sedeModo.setValido(false);
            sedesRepository.save(sedeModo);
            attr.addFlashAttribute("msg","Sede borrada exitosamente");
            attr.addFlashAttribute("opcion","alert-danger");
        }
        return "redirect:/admin/sedes";
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/actores")
    public String paginaActores(Model model,@RequestParam(value="search",required = false) String search) {
        if(search!=null){
            model.addAttribute("listaActores",actorRepository.busquedaActorporNombre(search));
        }else{
            model.addAttribute("listaActores", actorRepository.findAll());
        }
        return "admin/actores";
    }

    @PostMapping("/saveactor")
    public String saveactor(@ModelAttribute("actor") @Valid Actor actor,BindingResult bindingResult,@RequestParam("files[]") MultipartFile file,Model model, RedirectAttributes attr) throws IOException {
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            if (actor.getId() == null) {
                return "admin/agregaractor";
            } else {
                return "admin/editaractor";
            }
        }else {
            if(file.isEmpty()){
                model.addAttribute("msg","Debe subir un archivo");
                return "admin/agregaractor";
            }
            else{
                    actor.setFoto(file.getBytes());
                    String msg = "Actor " + "creado " + "exitosamente";
                    attr.addFlashAttribute("msg", msg);
                    attr.addFlashAttribute("opcion", "alert-success");
                    actorRepository.save(actor);
                    return "redirect:/admin/actores";
            }
        }
    }

    @GetMapping("/imageactores/{id}")
    public ResponseEntity<byte[]> mostrarimagenactor(@PathVariable("id") int id){
        Optional<Actor> opt = actorRepository.findById(id);
        if (opt.isPresent()) {
            Actor actor = opt.get();
            byte[] imagenComoBytes = actor.getFoto();
            return new ResponseEntity<>(
                    imagenComoBytes,
                    HttpStatus.OK);
        }else{
            return null;
        }
    }

    @PostMapping("/searchactores")
    public String actoresBuscar(Model model,@RequestParam(value = "search",required = false) String search){
        return "redirect:/admin/actores?search="+search;
    }

    @GetMapping("/agregaractor")
    public String paginaAgregaractor(@ModelAttribute("actor") Actor actor,Model model){

        return "admin/agregaractor";
    }

    @GetMapping("/editaractor")
    public String paginaEditaractor(@RequestParam("id") Integer id,@ModelAttribute("actor") Actor actor,Model model){
        Optional<Actor> optionalActor = actorRepository.findById(id);
        if(optionalActor.isPresent()){
            model.addAttribute("actor",optionalActor.get());
            return "admin/editaractor";
        }
        return "redirect:/admin/editaractor";
    }

    @PostMapping("/editactor")
    public String editactor(@ModelAttribute("actor") @Valid Actor actor, BindingResult bindingResult,Model model,RedirectAttributes attr){
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            return "admin/editaractor";
        }else{
            actorRepository.save(actor);
            String msg = "Actor " +  "actualizado " + "exitosamente";
            attr.addFlashAttribute("msg", msg);
            attr.addFlashAttribute("opcion", "alert-success");
            return "redirect:/admin/actores";
        }
    }

    @PostMapping("/borraractor")
    public String borraractor(@RequestParam("id") Integer id, RedirectAttributes attr){
        Optional<Actor> optionalActor = actorRepository.findById(id);
        if(optionalActor.isPresent()){
            List<Integer> listaCalificaciones = actorRepository.obtenerIdCalificacion();
            if(listaCalificaciones.size() != 0){
                for(int i: listaCalificaciones){
                    if(i == id){
                        attr.addFlashAttribute("msg","El actor presenta calificaciones");
                        attr.addFlashAttribute("opcion","alert-danger");
                        return "redirect:/admin/actores";
                    }
                }
            }
            Actor actor = optionalActor.get();
            actor.setValido(false);
            actorRepository.save(actor);
            attr.addFlashAttribute("msg","Actor borrado exitosamente");
            attr.addFlashAttribute("opcion","alert-danger");
        }
        return "redirect:/admin/actores";
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/directores")
    public String paginaDirectores(Model model,@RequestParam(value="search",required = false) String search) {
        if(search!=null){
            model.addAttribute("listaDirectores",directorRepository.busquedaDirectorporNombre(search));
        }else{
            model.addAttribute("listaDirectores", directorRepository.findAll());
        }
        return "admin/directores";
    }

    @GetMapping("/agregardirector")
    public String paginaAgregardirector(@ModelAttribute("director") Director director,Model model){

        return "admin/agregardirector";
    }

    @PostMapping("/savedirector")
    public String savedirector(@ModelAttribute("director") @Valid Director director,BindingResult bindingResult,@RequestParam("files[]") MultipartFile file,Model model, RedirectAttributes attr) throws IOException {
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            if (director.getId() == null) {
                return "admin/agregardirector";
            } else {
                return "admin/editardirector";
            }
        }else {
            if(file.isEmpty()){
                model.addAttribute("msg","Debe subir un archivo");
                return "admin/agregardirector";
            }
            else{
                List<Director> listadirectores = directorRepository.findAll();
                String result = director.getNombre().replace(" ", "");
                String resultapellido = director.getApellido().replace(" ", "");
                String resultcorreo = director.getCorreo().replace(" ","");
                for(Director i : listadirectores){
                    String result2 = i.getNombre().replace(" ","");
                    String resultapellido2 = i.getApellido().replace(" ","");
                    String resultcorreo2 = i.getCorreo().replace(" ","");
                    if(result.equalsIgnoreCase(result2) && resultapellido.equalsIgnoreCase(resultapellido2)){
                        if(i.getValido()){
                            attr.addFlashAttribute("msg","El director ya ha sido creado previamente");
                            attr.addFlashAttribute("opcion","alert-danger");
                            return "redirect:/admin/directores";
                        }

                    }if(resultcorreo.equalsIgnoreCase(resultcorreo2)){
                        model.addAttribute("emailerror", "Credenciales ya registradas");
                        return "admin/agregardirector";
                    }
                }
                director.setFoto(file.getBytes());
                String msg = "Director " + (director.getId()== null ? "creado " : "actualizado") + "exitosamente";
                attr.addFlashAttribute("msg", msg);
                attr.addFlashAttribute("opcion", "alert-success");
                directorRepository.save(director);
                return "redirect:/admin/directores";
            }
        }
    }

    @GetMapping("/imagedirectores/{id}")
    public ResponseEntity<byte[]> mostrarimagendirector(@PathVariable("id") int id){
        Optional<Director> opt = directorRepository.findById(id);
        if (opt.isPresent()) {
            Director director = opt.get();
            byte[] imagenComoBytes = director.getFoto();
            return new ResponseEntity<>(
                    imagenComoBytes,
                    HttpStatus.OK);
        }else{
            return null;
        }
    }

    @PostMapping("/searchdirectores")
    public String directoresBuscar(Model model,@RequestParam(value = "search",required = false) String search){
        return "redirect:/admin/directores?search="+search;
    }

    @GetMapping("/editardirector")
    public String paginaEditardirector(@RequestParam("id") Integer id,@ModelAttribute("director") Director director,Model model){
        Optional<Director> optionalDirector = directorRepository.findById(id);
        if(optionalDirector.isPresent()){
            model.addAttribute("director",optionalDirector.get());
            return "admin/editardirector";
        }
        return "redirect:/admin/editardirector";
    }

    @PostMapping("/editdirector")
    public String editdirector(@ModelAttribute("director") @Valid Director director, BindingResult bindingResult,Model model,RedirectAttributes attr){
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            return "admin/editardirector";
        }else{
            directorRepository.save(director);
            String msg = "Director " +  "actualizado " + "exitosamente";
            attr.addFlashAttribute("msg", msg);
            attr.addFlashAttribute("opcion", "alert-success");
            return "redirect:/admin/directores";
        }
    }

    @PostMapping("/borrardirector")
    public String borrardirector(@RequestParam("id") Integer id, RedirectAttributes attr){
        Optional<Director> optionalDirector = directorRepository.findById(id);
        if(optionalDirector.isPresent()){
            List<Integer> listaCalificaciones = directorRepository.obtenerIdCalificacion();
            if(listaCalificaciones.size()!=0){
                for(int i: listaCalificaciones){
                    if(i == id){
                        attr.addFlashAttribute("msg","El director presenta calificaciones");
                        attr.addFlashAttribute("opcion","alert-danger");
                        return "redirect:/admin/directores";
                    }
                }
            }
            Director director = optionalDirector.get();
            director.setValido(false);
            directorRepository.save(director);
            attr.addFlashAttribute("msg","Director borrado exitosamente");
            attr.addFlashAttribute("opcion","alert-danger");
        }
        return "redirect:/admin/directores";
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/clientes")
    public String clientes(Model model){
        model.addAttribute("listaClientes",usuarioRepository.findByRol("Cliente"));
        model.addAttribute("listasedes",sedesRepository.findAll());
        return "admin/clientes";
    }

    @PostMapping("/searchclientes")
    public String buscarcliente(Model model,@RequestParam("sedes") Sede sedes){
        model.addAttribute("listasedes",sedesRepository.findAll());
        model.addAttribute("salaList",sedes.getListasalas());
        return "admin/salas";
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
    public String savesedes(@ModelAttribute("usuario") @Valid Usuario usuario,BindingResult bindingResult, RedirectAttributes attr, Model model) {

        System.out.println(bindingResult.getAllErrors());

        if (bindingResult.hasErrors()) {
            return "admin/agregaroperador";
        } else {
            if (dniDao.ConsultarDNI(usuario.getDni())) {
                Usuario usuarioconfirm = usuarioRepository.findByCorreo(usuario.getCorreo());
                if (usuarioconfirm != null) {
                    model.addAttribute("emailerror", "Credenciales ya registradas");
                    return "admin/agregaroperador";
                } else {
                    String contra = usuario.getContrasena();
                    usuario.setContrasena(new BCryptPasswordEncoder().encode(contra));
                    usuarioRepository.save(usuario);
                    attr.addFlashAttribute("msg", "Operador creado exitosamente");
                    attr.addFlashAttribute("opcion", "alert-success");
                    return "redirect:/admin/operadores";

                }
            }
            model.addAttribute("dnierror", "Dni no valido");
        }
        return "admin/agregaroperador";
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
