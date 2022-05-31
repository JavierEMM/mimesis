package com.mimesis.controller;

import com.mimesis.dto.DTOActoresMejoresCalificados;
import com.mimesis.dto.DTOBoletosPorFuncion;
import com.mimesis.dto.DTOTotalBoletosPorFuncion;
import com.mimesis.entity.*;
import com.mimesis.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

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
    @Autowired
    FotoRepository fotoRepository;
    @Autowired
    ObrasRepository obrasRepository;
    @Autowired
    GeneroRepository generoRepository;

    @Autowired
    BoletoRepository boletoRepository;

    @GetMapping("/reporte")
    public String generarReporte(Model model, HttpServletResponse response){
        response.setContentType("aplication/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachement;filename=Reporte.xlsx";
        List<DTOBoletosPorFuncion> boletosPorFuncion = boletoRepository.boletosporFuncion();
        List<Integer> ids = new ArrayList<>();
        for (DTOBoletosPorFuncion boleto : boletosPorFuncion){
            System.out.println(boleto.getidsalaBoleto());
            System.out.println(boleto.getSumaBoletos());
            ids.add(boleto.getidsalaBoleto());
        }
        List<Funcion> listaFuncionesReporte = funcionRepository.findAllById(ids);
        String [] titulos = {"Id de Funcion","Nombre","Sede","Dinero recaudado"};
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte");
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short)17);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);

        for (int i = 0;i< titulos.length;i++){
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(titulos[i]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum=1;

        for (int j=0; j< boletosPorFuncion.size();j++ ) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(boletosPorFuncion.get(j).getidsalaBoleto());
            row.createCell(1).setCellValue(listaFuncionesReporte.get(j).getIdobra().getNombre());
            row.createCell(2).setCellValue(listaFuncionesReporte.get(j).getIdsala().getIdsede().getNombre());
            row.createCell(3).setCellValue(boletosPorFuncion.get(j).getSumaBoletos()*listaFuncionesReporte.get(j).getCosto());

        }

        for (int i = 0;i< titulos.length;i++){
            sheet.autoSizeColumn(i);
        }
        response.setHeader(headerKey,headerValue);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        model.addAttribute("listaFunciones",funcionRepository.findAll());
        return "operador/listafunciones";
    }




    @GetMapping(value = {"/",""})
    public String paginaPrincipal(Model model){
        model.addAttribute("listaFunciones",funcionRepository.findAll());
        return "operador/listafunciones";
    }

    @PostMapping("/search")
    public String buscarFuncion(Model model,@RequestParam("busqueda") String busqueda,@RequestParam("categoria") String categoria){
        if(categoria.equalsIgnoreCase("Nombre")){
            model.addAttribute("listaFunciones",funcionRepository.listaBuscarFuncionesNombre(busqueda));
        }else{
            model.addAttribute("listaFunciones",funcionRepository.listaBuscarFuncionesGenero(busqueda));
        }

        return "operador/listafunciones";
    }

    @GetMapping(value = {"/obras"})
    public String paginaPrincipalObras(Model model){
        model.addAttribute("listaObras",obrasRepository.findAll());
        return "operador/listaobras";
    }

    @PostMapping("/searchObra")
    public String buscarObra(Model model,@RequestParam("busqueda") String busqueda,@RequestParam("categoria") String categoria){
        if(categoria.equalsIgnoreCase("Nombre")){
            model.addAttribute("listaObras",obrasRepository.listaBuscarObrasNombre(busqueda));
        }else{
            model.addAttribute("listaObras",obrasRepository.listaBuscarObrasGenero(busqueda));
        }
        return "operador/listaobras";
    }

    @GetMapping("/crearobra")
    public String nuevaObra(@ModelAttribute("obra") Obra obra, Model model){
        model.addAttribute("listaGeneros",generoRepository.findAll());
        return "operador/obraFrm";
    }

    @PostMapping("saveobra")
    public String newObra (@ModelAttribute("obra")@Valid Obra obra, BindingResult bindingResult,Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("listaGeneros",generoRepository.findAll());
            return "operador/obraFrm";
        }else {
            obrasRepository.save(obra);
            return "redirect:/operador/obras";
        }


    }
    @GetMapping("/deleteObra")
    public String deleteObra(@RequestParam("id") Integer id){
        Optional<Obra> optionalObra = obrasRepository.findById(id);

        if(optionalObra.isPresent()){
            Obra obraMod= optionalObra.get();
            obraMod.setValido(false);
            obrasRepository.save(obraMod);
        }
        return "redirect:/operador/obras";
    }



    @GetMapping("/crearfuncion")
    public String nuevaFuncion (@ModelAttribute("funcion") Funcion funcion, Model model){
        model.addAttribute("listaObras",obrasRepository.findAll());
        model.addAttribute("listaActores",actorRepository.findAll());
        model.addAttribute("listaDirectores",directorRepository.findAll());
        model.addAttribute("listaSedes",sedesRepository.findAll());
        model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
        model.addAttribute("listaGeneros",generoRepository.findAll());

        return "operador/funcionFrm";
    }
    @GetMapping("/estadisticas")
    public String estadisticas (Model model){

        DTOTotalBoletosPorFuncion info = funcionRepository.boletosbyFuncion(1);
        List<DTOTotalBoletosPorFuncion> listaDto = funcionRepository.boletosTotal();
        List <Integer> idFunciones = new ArrayList<>();
        for (DTOTotalBoletosPorFuncion a : listaDto){
            idFunciones.add(a.getIdFuncionTotal());
            System.out.println(a.getIdFuncionTotal());
        }

        model.addAttribute("noAsistentes",info.getCantidadboletostotal()- info.getCantidadasistentes());
        model.addAttribute("asistentes",info.getCantidadasistentes());
        model.addAttribute("nombre",funcionRepository.findById(info.getIdFuncionTotal()).get().getIdobra().getNombre());
        model.addAttribute("listaFunciones",funcionRepository.findAllById(idFunciones));
        model.addAttribute("listaActores",actorRepository.findAll());
        model.addAttribute("listaDirectores",directorRepository.findAll());
        model.addAttribute("listaObras",obrasRepository.findAll());
        model.addAttribute("listaActoresMejoresCalificados",actorRepository.obtenerActoresMejoresCalificados());
        model.addAttribute("listaDirectoresMejoresCalificados",directorRepository.obtenerDirectoresMejoresCalificados());
        List<DTOActoresMejoresCalificados> listaActoresMejoresCalificados = actorRepository.obtenerActoresMejoresCalificados();
        for(DTOActoresMejoresCalificados actor:listaActoresMejoresCalificados){
            System.out.println(actor.getNombre_actor());
        }
        return "operador/estadisticas";
    }
    @PostMapping("/estadisticaFuncion")
    public String estadisticaPorFuncion(Model model, @RequestParam("opcion")Optional<Integer> optOpcion, @RequestParam("FechaInicio")Optional<String> optFechaInicio,@RequestParam("FechaFin")Optional<String> optFechaFin){
        int id;
        if(optOpcion.isPresent()){
            id = optOpcion.get();
        }else{
            id=1;
        }

        if(optFechaInicio.isPresent()){
            System.out.println(optFechaInicio.get());
        }
        if(optFechaFin.isPresent()){
            System.out.println(optFechaFin.get());
        }
        String b = String.valueOf(LocalDate.now());
        System.out.println(b);

        DTOTotalBoletosPorFuncion info = funcionRepository.boletosbyFuncion(id);

        List<DTOTotalBoletosPorFuncion> listaDto = funcionRepository.boletosTotal();
        List <Integer> idFunciones = new ArrayList<>();
        for (DTOTotalBoletosPorFuncion a : listaDto){
            idFunciones.add(a.getIdFuncionTotal());
            System.out.println(a.getIdFuncionTotal());
        }

        model.addAttribute("noAsistentes",info.getCantidadboletostotal()- info.getCantidadasistentes());
        model.addAttribute("asistentes",info.getCantidadasistentes());
        model.addAttribute("nombre",funcionRepository.findById(info.getIdFuncionTotal()).get().getIdobra().getNombre());
        model.addAttribute("listaFunciones",funcionRepository.findAllById(idFunciones));
        model.addAttribute("listaActores",actorRepository.findAll());
        model.addAttribute("listaDirectores",directorRepository.findAll());
        model.addAttribute("listaObras",obrasRepository.findAll());
        model.addAttribute("listaActoresMejoresCalificados",actorRepository.obtenerActoresMejoresCalificados());
        model.addAttribute("listaDirectoresMejoresCalificados",directorRepository.obtenerDirectoresMejoresCalificados());
        List<DTOActoresMejoresCalificados> listaActoresMejoresCalificados = actorRepository.obtenerActoresMejoresCalificados();
        for(DTOActoresMejoresCalificados actor:listaActoresMejoresCalificados){
            System.out.println(actor.getNombre_actor());
        }
        return "operador/estadisticas";
    }


    @GetMapping("/edit")
    public String editarOperador (@RequestParam("id") Integer id,@ModelAttribute("funcion") Funcion funcion,Model model){
       Optional<Funcion> optionalFuncion = funcionRepository.findById(id);
        if(optionalFuncion.isPresent()){
            model.addAttribute("funcion",optionalFuncion.get());
            model.addAttribute("listaObras",obrasRepository.findAll());
            model.addAttribute("listaActores",optionalFuncion.get().getActors());
            model.addAttribute("listaDirectores",directorRepository.findAll());
            model.addAttribute("listaSedes",sedesRepository.findAll());
            model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
            model.addAttribute("listaGeneros",generoRepository.findAll());
            model.addAttribute("listaFotos",optionalFuncion.get().getFotosporfuncion());
            System.out.println("id funcion" + funcion.getId());
            System.out.println("Tamaño de lista de fotos" + optionalFuncion.get().getFotosporfuncion().size());
            return "operador/editarFrm";
        }

        return "redirect:/operador";
        }

    @GetMapping("/delete")
    public String delteFuncion(@RequestParam("id") Integer id){
        Optional<Funcion> optionalFuncion = funcionRepository.findById(id);

        if(optionalFuncion.isPresent()){
            Funcion funcionMod= optionalFuncion.get();
            funcionMod.setValido(false);
            funcionRepository.save(funcionMod);
        }
        return "redirect:/operador";
    }

    @GetMapping("/image/{id}/{index}")
    public ResponseEntity<byte[]> mostrarimagen(@PathVariable("id") int id,@PathVariable("index") int index){
        System.out.println("index = " + index);
        Optional<Funcion> opt = funcionRepository.findById(id);
        if (opt.isPresent()) {
            Funcion funcion = opt.get();
            byte[] imagenComoBytes = funcion.getFotosporfuncion().get(index).getFoto();
            return new ResponseEntity<>(
                    imagenComoBytes,
                    HttpStatus.OK);
        }else{
            return null;
        }
    }


    @PostMapping("/saveEdit")
    public String guardarFuncionEditada(@ModelAttribute("funcion")@Valid Funcion funcion, BindingResult bindingResult,Model model){
        if(bindingResult.hasErrors() || funcion.getHorainicio().compareTo(funcion.getHorafin())>0 ){
            model.addAttribute("listaActores",funcion.getActors());
            model.addAttribute("listaObras",obrasRepository.findAll());
            model.addAttribute("listaDirectores",directorRepository.findAll());
            model.addAttribute("listaSedes",sedesRepository.findAll());
            model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
            model.addAttribute("listaGeneros",generoRepository.findAll());
            model.addAttribute("listaFotos",funcion.getFotosporfuncion());
            if(funcion.getHorainicio()!=null && funcion.getHorafin()!=null){
                if(funcion.getHorainicio().compareTo(funcion.getHorafin())>0){
                    model.addAttribute("errorTime","Ingrese un rango de horas válido");
                }
            }
            return "operador/editarFrm";
        }else{
            funcionRepository.save(funcion);
            return "redirect:/operador";
        }

    }






    @PostMapping("/save")
    public String newFuncion (@ModelAttribute("funcion")@Valid Funcion funcion, BindingResult bindingResult, Model model, @RequestParam("actoresObra") Optional<List<Actor>> actoresObra,
                               @RequestParam("files[]") List<MultipartFile> file ) throws IOException {
        System.out.println("entro al controller");
        System.out.println(bindingResult.getAllErrors());
        if(bindingResult.hasErrors() || !actoresObra.isPresent()|| funcion.getHorainicio().compareTo(funcion.getHorafin())>0 || file.get(0).getBytes().length == 0){
            model.addAttribute("listaActores",actorRepository.findAll());
            model.addAttribute("listaObras",obrasRepository.findAll());
            model.addAttribute("listaDirectores",directorRepository.findAll());
            model.addAttribute("listaSedes",sedesRepository.findAll());
            model.addAttribute("listaSalas",salasRepository.findAll(Sort.by("idsede")));
            model.addAttribute("listaGeneros",generoRepository.findAll());
            System.out.println("entro al primer if");
            if (file.get(0).getBytes().length == 0) {
                model.addAttribute("errorFoto", "Debe adjuntar al menos una foto");
            }
            if(!actoresObra.isPresent()){
                System.out.println("Error actor");
                model.addAttribute("errorActor", "Debe seleccionar un género");
            }
            if(funcion.getHorainicio()!=null && funcion.getHorafin()!=null){
                if(funcion.getHorainicio().compareTo(funcion.getHorafin())>0){
                    model.addAttribute("errorTime","Ingrese un rango de horas válido");
                }
            }
            if(funcion.getAforo()!=null && funcion.getIdsala()!=null){
                if(funcion.getAforo()>funcion.getIdsala().getAforo()){
                    System.out.println("Error aforo");
                    model.addAttribute("errorAforo","Debe elegir un aforo menor o igual al de la sala seleccionada: "+funcion.getIdsala().getAforo());
                }
            }


            if(funcion.getId()==0){
                return "operador/funcionFrm";
            }else{
                return "operador/editarFrm";
            }



        }else{
            if(funcion.getAforo()>funcion.getIdsala().getAforo()){
                System.out.println("Error aforo");
                model.addAttribute("errorAforo","Debe elegir un aforo menor o igual al de la sala seleccionada: "+funcion.getIdsala().getAforo());
                return "operador/funcionFrm";
            }
            System.out.println("Entro al else");
            funcion.setActors(actoresObra.get());
            Collections.reverse(file);
            funcionRepository.save(funcion);
            try{
                System.out.println("entro al try");
                for (MultipartFile file1: file ) {
                    Foto foto= new Foto();
                    foto.setFoto(file1.getBytes());
                    foto.setIdfuncion(funcion);
                    fotoRepository.save(foto);
                }

            } catch (IOException e){
                e.printStackTrace();
            }
            return "redirect:/operador";
        }

    }




}
