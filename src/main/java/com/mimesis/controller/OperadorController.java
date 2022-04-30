package com.mimesis.controller;

import com.mimesis.entity.Actor;
import com.mimesis.entity.Funcion;
import com.mimesis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/operador")
public class OperadorController {
    @Autowired
    FuncionRepository funcionRepository;
    @Autowired
    ActorRepository actorRepository;
    @Autowired
    DirectorRepository directorRepository;
    @Autowired
    SedesRepository sedesRepository;
    @Autowired
    SalasRepository salasRepository;

    @GetMapping(value = {"/",""})
    public String paginaPrincipal(Model model){
        List<Funcion> lista = funcionRepository.findAll();
        model.addAttribute("listaFunciones",funcionRepository.findAll());

        return "operador/listafunciones";
    }
    @GetMapping("/crearfuncion")
    public String nuevaFuncion (@ModelAttribute("funcion") Funcion funcion, Model model){
        model.addAttribute("listaActores",actorRepository.findAll());
        model.addAttribute("listaDirectores",directorRepository.findAll());
        model.addAttribute("listaSedes",sedesRepository.findAll());
        model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
        return "operador/crearfuncion";
    }
    @GetMapping("/estadisticas")
    public String estadisticas (){ return "operador/estadisticas";}
    @GetMapping("/edit")
    public String editarOperador (@RequestParam("id") Integer id,@ModelAttribute("funcion") Funcion funcion,Model model){
        Optional<Funcion> optionalFuncion= funcionRepository.findById(id);
        if(optionalFuncion.isPresent()){
            model.addAttribute("funcion",optionalFuncion.get());
            model.addAttribute("listaActores",optionalFuncion.get().getActoresPorFuncion());
            model.addAttribute("listaDirectores",directorRepository.findAll());
            model.addAttribute("listaSedes",sedesRepository.findAll());
            model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
            return "operador/crearfuncion";
        }

        return "redirect:/operador";
        }

    @PostMapping("/save")
    public String newFuncion (@ModelAttribute("funcion") Funcion funcion, Model model,@RequestParam("actoresObra") List<Actor> actoresObra){
        ArrayList<Actor> listaActSelect = new ArrayList<>();
        listaActSelect.addAll(actoresObra);
        funcion.setActoresPorFuncion(actoresObra);
        System.out.println(funcion.getNombre());
        System.out.println(funcion.getAforo());
        System.out.println(funcion.getCosto());
        System.out.println(funcion.getGenero());
        System.out.println(funcion.getIdsala().getNombre());
        System.out.println(funcion.getIddirector().getNombre());
        for(Actor act : listaActSelect){
            System.out.println("Actors size :"+act.getNombre());
        }
        System.out.println(funcion.getHorainicio());
        System.out.println(funcion.getHorafin() );
        System.out.println(funcion.getActoresPorFuncion().get(0).getNombre());
        funcionRepository.save(funcion);
        return  "redirect:/operador";
    }

   /* @GetMapping("/edit")
    public String editarFuncion(Model model, @RequestParam("id") int id, @ModelAttribute("employees") Employees employees, RedirectAttributes redirectAttributes) {
        Optional<Employees> employeesOptional = employeesRepository.findById(id);
        if (employeesOptional.isPresent()) {
            employees = employeesOptional.get();
            Employees emplo = new Employees();
            if((emplo = employees.getManagerid()) == null){
                redirectAttributes.addFlashAttribute("nohayjefe", "No puedes editar a este usuario. MANAGER_ID = NULL");
                return "redirect:/employee";
            }
            model.addAttribute("employees", employees);
            model.addAttribute("listaDepartaments", departmentsRepository.findAll());
            List<Departments> departmentOpt = departmentsRepository.findAll();
            List<Departments> departamentosFinales = new ArrayList<Departments>();
            for (Departments i : departmentOpt){
                if(i.getManagerid() != null){
                    departamentosFinales.add(i);
                }
            }
            model.addAttribute("listaJobs", jobsRepository.findAll());
            model.addAttribute("listaDepartamentosconJefes", departamentosFinales);
            return "employee/Frm";
        } else {
            return "redirect:/employee";
        }

    }*/


}
