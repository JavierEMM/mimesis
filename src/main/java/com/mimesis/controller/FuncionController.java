package com.mimesis.controller;

import com.mimesis.entity.Funcion;
import com.mimesis.entity.Obra;
import com.mimesis.entity.Sede;
import com.mimesis.repository.FuncionRepository;
import com.mimesis.repository.ObrasRepository;
import com.mimesis.repository.SedesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}
