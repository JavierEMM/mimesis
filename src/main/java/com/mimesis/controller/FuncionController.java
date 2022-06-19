package com.mimesis.controller;

import com.mimesis.entity.Foto;
import com.mimesis.entity.Funcion;
import com.mimesis.entity.Obra;
import com.mimesis.entity.Sede;
import com.mimesis.repository.FotoRepository;
import com.mimesis.repository.FuncionRepository;
import com.mimesis.repository.ObrasRepository;
import com.mimesis.repository.SedesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/funciones")
public class FuncionController {
    @Autowired
    SedesRepository sedesRepository;
    @Autowired
    ObrasRepository obrasRepository;
    @Autowired
    FuncionRepository funcionRepository;
    @Autowired
    FotoRepository fotoRepository;

    @GetMapping("")
    public String paginaFunciones(@RequestParam(value = "search",required = false) String search,Model model){

        if(search!=null){
            model.addAttribute("listaObras",obrasRepository.listaBuscarObrasUnicasPorNombre(search));
        }else {
            model.addAttribute("listaObras",obrasRepository.listaBuscarObrasUnicasPorNombre(""));
        }
        return "usuario/funciones";
    }

    @PostMapping("/buscar")
    public String funcionesBuscar(@RequestParam(value = "search",required = false) String search) throws UnsupportedEncodingException {
        return "redirect:/funciones?search="+ URLEncoder.encode(search,"UTF-8");
    }

    @GetMapping(value = "/detalles")
    public String detallesFunciones(Model model,@RequestParam("obra") String obra) throws UnsupportedEncodingException {

        System.out.println("LA OBRA ES: "+ URLDecoder.decode(obra,"UTF-8"));
        Obra obra1= obrasRepository.findByNombre(obra);
        System.out.println("la id de la obra es: "+obra1.getId());
        List<Sede> sedes = sedesRepository.teatrosPorFuncion(obra1.getId());
        model.addAttribute("obra",obra1);
        model.addAttribute("listaTeatros",sedes);
        return "usuario/detallesFuncion";
    }

    @GetMapping("/obras/{id}")
    public ResponseEntity<byte[]> imagesMostrar(@PathVariable("id") int id){
        System.out.println(id);
        List<Foto> fotos = fotoRepository.listaFotosxActor(id);
        if(fotos != null){
            return new ResponseEntity<>(
                    fotos.get(0).getFoto(), HttpStatus.OK
            );
        }else{
            return null;
        }
    }

}
