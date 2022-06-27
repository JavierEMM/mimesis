package com.mimesis.controller;

import com.mimesis.dao.TarjetaDao;
import com.mimesis.dto.DTOBoletosValidos;
import com.mimesis.dto.DTOFuncionesDisponibles;
import com.mimesis.dto.DTOTarjeta;
import com.mimesis.dto.DTOcarrito;
import com.mimesis.entity.*;
import com.mimesis.repository.*;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/carrito")
public class CarritoController {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    TarjetaDao tarjetaDao;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    ObrasRepository obrasRepository;
    @Autowired
    FuncionRepository funcionRepository;
    @Autowired
    SedesRepository sedesRepository;
    @Autowired
    BoletoRepository boletoRepository;

    @GetMapping("")
    public String carrito(Model model,HttpSession session){
        ArrayList<DTOcarrito> funcions = (ArrayList) session.getAttribute("carrito");
        model.addAttribute("carrito",funcions);
        return "usuario/carrito";
    }

    @PostMapping("/funcion")
    public String seleccionarFuncion(Model model,@RequestParam(value = "cantidad",required = false) Integer cantidad, @RequestParam(value = "funcion", required = false) Integer funcion
            ,@RequestParam("boletos") Integer boletos, HttpSession session,RedirectAttributes attributes) throws UnsupportedEncodingException {
        Optional<Funcion> funcion2 = funcionRepository.findById(funcion);
        if(cantidad == null || funcion == null){
            attributes.addFlashAttribute("alerta","alert-danger");
            attributes.addFlashAttribute("reserva","Por favor, al seleccionar funcion seleccione una cantidad valida");
            return "redirect:/funciones/detalles?obra=" + URLEncoder.encode(funcion2.get().getIdobra().getNombre(),"UTF-8");
        }else{
            if(boletos>=cantidad){
                Sede sede = sedesRepository.sedePorFuncion(funcion);
                ArrayList<DTOcarrito> funcions = (ArrayList) session.getAttribute("carrito");
                DTOcarrito dtOcarrito = new DTOcarrito();
                dtOcarrito.setFuncion(funcion2.get());
                dtOcarrito.setCantidad(cantidad);
                dtOcarrito.setCostoTotal(funcion2.get().getCosto()*cantidad);
                dtOcarrito.setSede(sede);
                funcions.add(dtOcarrito);
                session.setAttribute("carrito",funcions);
                session.setAttribute("ncarrito",funcions.size());
                model.addAttribute("carrito",funcions);
                return "usuario/carrito";
            }else{
                attributes.addFlashAttribute("alerta","alert-danger");
                attributes.addFlashAttribute("reserva","Selecciona una cantidad valida");
                return "redirect:/funciones/detalles?obra=" + URLEncoder.encode(funcion2.get().getIdobra().getNombre(),"UTF-8");
            }

        }
    }
    @GetMapping("/borrar")
    public String borrarCarrito(HttpSession session,@RequestParam("num") int id){
        ArrayList<DTOcarrito> carrito =(ArrayList) session.getAttribute("carrito");
        System.out.println("ID BORRAR: "+id);
        carrito.remove(id);
        session.setAttribute("carrito",carrito);
        session.setAttribute("ncarrito",carrito.size());
        return "redirect:/carrito";
    }

    @RequestMapping(value = "/comprar")
    public String carritoComprar(@ModelAttribute("datosTarjeta") DTOTarjeta dtoTarjeta, Model model, HttpSession session){
        ArrayList<DTOcarrito> carrito =(ArrayList) session.getAttribute("carrito");
        double precioPagar = 0;
        for (DTOcarrito ocarrito : carrito){
            precioPagar+=ocarrito.getCostoTotal();
        }
        session.setAttribute("precioTotal",precioPagar);
        return "usuario/compra";
    }

    @PostMapping(value = "/reservar")
    public String carritoReservar(Model model, @RequestParam("obra") String nombreObra,@RequestParam(value = "teatro",required = false) Integer teatro, RedirectAttributes attributes) throws UnsupportedEncodingException {
        Obra obra = obrasRepository.findByNombre(nombreObra);
        if(teatro == null){
            attributes.addFlashAttribute("alerta","alert-danger");
            attributes.addFlashAttribute("reserva","Selecciona un teatro valido");
            return "redirect:/funciones/detalles?obra=" + URLEncoder.encode(nombreObra,"UTF-8");
        }else {
            Optional<Sede> sede = sedesRepository.findById(teatro);
            if (sede.isPresent()) {
                System.out.println("Este print verifica la obra buscada"+obra.getNombre());
                System.out.println("Este print verifica la sede buscada"+sede.get().getNombre());
                List<Funcion> funcionListaDisponible = funcionRepository.listaFuncionesValidosCorregida(teatro,obra.getId());
                List<DTOFuncionesDisponibles> listaValidos = new ArrayList<>();
                for(Funcion f : funcionListaDisponible){
                        Optional<Integer> optBoleto = funcionRepository.obtenerBoletos(f.getId());
                        Integer boleto =f.getAforo();
                        if(optBoleto.isPresent()){
                            boleto= optBoleto.get();
                        }
                        listaValidos.add(new DTOFuncionesDisponibles(boleto,f));
                }

                System.out.println("En esta parte se envian los parametros para seleccionar la funcion");
                System.out.println(obra.getId());
                System.out.println(sede.get().getNombre());
                model.addAttribute("listaFunciones", listaValidos);
                model.addAttribute("obra", obra);
                model.addAttribute("sede", sede.get());
                return "usuario/eligefuncion";
            } else {
                attributes.addFlashAttribute("alerta", "alert-danger");
                attributes.addFlashAttribute("reserva", "Selecciona un teatro valido");
                return "redirect:/funciones/detalles?obra=" + URLEncoder.encode(nombreObra, "UTF-8");
            }
        }
    }
    @PostMapping(value = "/confirmarcompra")
    public String confirmarCompra(@ModelAttribute("datosTarjeta") DTOTarjeta dtoTarjeta, RedirectAttributes attributes,HttpSession session) throws MessagingException, UnsupportedEncodingException {
        System.out.println("Numero de tarjeta: "+dtoTarjeta.getNumero());

        String fecha = dtoTarjeta.getVencimiento();
        System.out.println("fecha: "+fecha);
        dtoTarjeta.setVencimiento(fecha+"-01");
        System.out.println(dtoTarjeta.getVencimiento());

        if(dtoTarjeta.getNumero() == null){
            attributes.addFlashAttribute("msg","Debe ingresar una tarjeta");
            return "redirect:/carrito/comprar";
        }else{
            if(tarjetaDao.consultaTarjeta(dtoTarjeta)){
                ArrayList<DTOcarrito> carrito =(ArrayList) session.getAttribute("carrito");
                String usuario2 = (String) session.getAttribute("usuario");
                Usuario usuario = usuarioRepository.findByCorreo(usuario2);
                for(DTOcarrito dtOcarrito : carrito){
                    String hola="";
                    for (int i = 0; i<dtOcarrito.getCantidad();i++){
                        String url= RandomString.make(50);
                        Boleto boleto = new Boleto(true,dtOcarrito.getFuncion(),usuario, url);
                        boletoRepository.save(boleto);
                        hola+="<img style='margin-right:15px;' src="+"https://api.qrserver.com/v1/create-qr-code/?data="+url+"&size=200x200>";
                    }
                    System.out.println("Funcion: " + dtOcarrito.getFuncion().getIdobra().getNombre());
                    enviarQr(usuario,hola,dtOcarrito.getFuncion(),dtOcarrito.getCantidad());
                }
                ArrayList<DTOcarrito> carrito2 = new ArrayList<>();
                session.setAttribute("carrito",carrito2);
                return "redirect:/historial";
            }else {
                attributes.addFlashAttribute("msg","Debe ingresar una tarjeta");
                return "redirect:/carrito/comprar";
            }
        }
    }
    public void enviarQr(Usuario usuario,String baseUrl,Funcion funcion,Integer cantidad) throws MessagingException, UnsupportedEncodingException {
        String subject= "Gracias por su compra";
        String name="Mimesis App";
        String message2="<p>Estimado " + usuario.getNombre() + " " + usuario.getApellido()+ ",</p>";
        message2 += "<p>Usted ha realizado la compra de "+cantidad+" boletos para la funcion: "+funcion.getIdobra().getNombre()+"</p>";
        message2 += "<p>Sede: "+funcion.getIdsala().getIdsede().getNombre()+"</p>";
        message2 += "<p>Sala: "+funcion.getIdsala().getNombre()+"</p>";
        message2 += "<p>Fecha: "+funcion.getFecha()+" inicia a las "+funcion.getHorainicio()+" termina a las "+funcion.getHorafin()+"</p>";
        message2+=baseUrl;
        message2+="<p>@Mimesis teatros</p>";
        mimeMessage(usuario, subject, name, message2);
    }

    private void mimeMessage(Usuario usuario, String subject, String name, String message2) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("TeatrosMimesis1@gmail.com",name);
        mimeMessageHelper.setTo(usuario.getCorreo());
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(message2,true);
        javaMailSender.send(mimeMessage);
    }
}
