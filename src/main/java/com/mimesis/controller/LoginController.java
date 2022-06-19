package com.mimesis.controller;

import com.mimesis.dao.DniDao;
import com.mimesis.dto.DTOcarrito;
import com.mimesis.entity.Funcion;
import com.mimesis.entity.Usuario;
import com.mimesis.google.CustomOAuth2User;
import com.mimesis.repository.UsuarioRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@Controller
public class LoginController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    DniDao dniDao;

    @GetMapping("/login")
    public String loginForm(){
        return "login/login";
    }
    @GetMapping("")
    public String form(){
        return "login/login";
    }

    @GetMapping("/verification")
    public String accountValidation(@RequestParam(name = "code",required = false) String code, RedirectAttributes attributes){
        Usuario usuario = null;
        if(code == null){
            attributes.addFlashAttribute("alerta","alert-danger");
            attributes.addFlashAttribute("registro","Ha ocurrido un error al validar el correo electronico, ingrese al link enviado a su correo");
        }else{
            usuario = usuarioRepository.findByToken(code);
        }
        if(usuario != null){
            usuario.setEmailconfirm(true);
            usuario.setToken(null);
            usuarioRepository.save(usuario);
            attributes.addFlashAttribute("alerta","alert-success");
            attributes.addFlashAttribute("registro","Se ha verificado correctamente su cuenta correctamente, inicie sesion");
        }else{
            attributes.addFlashAttribute("alerta","alert-danger");
            attributes.addFlashAttribute("registro","Ha ocurrido un error al validar el correo electronico, ingrese al link enviado a su correo");
        }
        return "redirect:/login";
    }

    @GetMapping("/redirectByRole")
    public String redirectByRole(
            Authentication auth, HttpSession session, Model model){
        Usuario usuario;
        try{
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) auth.getPrincipal();
            usuario = usuarioRepository.findByCorreo(customOAuth2User.getEmail());
            if(usuario.getRol().equals("Operador") || usuario.getRol().equals("Admin")){
                auth.setAuthenticated(false);
                session.invalidate();
                model.addAttribute("mensaje","Ingrese por el usuario y contraseña establecido por la empresa");
                return "login/login";
            }
        }catch (Exception exception){
            usuario = usuarioRepository.findByCorreo(auth.getName());
            if(usuario.getEmailconfirm() == false){
                auth.setAuthenticated(false);
                session.invalidate();
                model.addAttribute("mensaje","Valide su cuenta antes de ingresar");
                return "login/login";
            }
        }
        session.setAttribute("usuario",usuario);
        if(usuario.getRol().equals("Cliente")) {
            ArrayList<DTOcarrito> carrito = new ArrayList<>();
            session.setAttribute("carrito",carrito);
            session.setAttribute("ncarrito",carrito.size());
            return "redirect:/";
        } else if(usuario.getRol().equals("Operador")) {
            return "redirect:/operador";
        }else if(usuario.getRol().equals("Admin")){
            return "redirect:/admin/salas";
        }else {
            return "login/login";
        }
    }
    @GetMapping("/registro")
    public String registro(@ModelAttribute("usuario") Usuario usuario){
        return "login/register";
    }

    @PostMapping("/registrar")
    public String registrar(@ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult, RedirectAttributes attributes, HttpServletRequest request, Model model) throws MessagingException, UnsupportedEncodingException {
        if(bindingResult.hasErrors()){
            return "login/register";
        }else{
            if(dniDao.ConsultarDNI(usuario.getDni())){
                Usuario usuarioconfirm = usuarioRepository.findByCorreo(usuario.getCorreo());
                if(usuarioconfirm != null){
                    model.addAttribute("emailerror","Credenciales ya registradas");
                    return "login/register";
                }else{

                    String contrasena= usuario.getContrasena();
                    usuario.setContrasena(new BCryptPasswordEncoder().encode(contrasena));
                    usuario.setRol("Cliente");
                    usuarioRepository.save(usuario);
                    String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                            .replacePath(null)
                            .build()
                            .toUriString();
                    //Tengo que enviar correo electronico
                    sendVerification(usuario,baseUrl);
                    attributes.addFlashAttribute("alerta","alert-success");
                    attributes.addFlashAttribute("registro","Se le ha enviado un correo de confirmacion a su correo electronico");
                }
            }

        }
        return "redirect:/login";
    }
    @GetMapping("/cambiarcontrasenia")
    public String vistaCambio(@ModelAttribute("usuario") Usuario usuario){
        return "login/nuevacontrasenia";
    }

    @PostMapping("/recuperarcontrasenia")
    public String nuevacontrasenia(@RequestParam(value = "correo",required = false) String correo,RedirectAttributes attributes,HttpServletRequest request ) throws MessagingException, UnsupportedEncodingException {
        //Envia el email
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if(usuario != null){
            String token = RandomString.make(255);
            usuario.setResetpwdtoken(token);
            usuarioRepository.save(usuario);
            String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                    .replacePath(null)
                    .build()
                    .toUriString();
            sendRePassword(usuario,baseUrl);
            attributes.addFlashAttribute("alerta","alert-success");
            attributes.addFlashAttribute("registro","Se le ha enviado un correo de confirmacion a su correo electronico");
            return "redirect:/login";
        }else{
            attributes.addFlashAttribute("alerta","alert-danger");
            attributes.addFlashAttribute("registro","Correo no registrado");
        }
        return "redirect:/cambiarcontrasenia";
    }

    @GetMapping("/validarpwd")
    public String cambiarContrasenia(@RequestParam(value = "code",required = false) String token,Model model,RedirectAttributes attributes){
        Usuario usuario;
        if (token != null) {
            usuario = usuarioRepository.findByResetpwdtoken(token);
        }else{
            attributes.addFlashAttribute("mensaje","Solicitud incorrecta");
            return "redirect:/login";
        }
        if(usuario != null) {
            model.addAttribute("usuario", usuario);
            return "login/cambiarcontra";
        }else{
            attributes.addFlashAttribute("mensaje","Solicitud incorrecta");
            return "redirect:/login";
        }
    }
    @PostMapping("/guardarcontra")
    public String guardarContra(@RequestParam(value = "token",required = false) String token, @RequestParam("contra_1") String contra ,@RequestParam("contra_2") String contraRe,RedirectAttributes attributes){
        System.out.println("Contra: "+ contra);
        System.out.println("ContraRe: "+contraRe);
        Usuario usuario;
        if (token != null) {
            usuario = usuarioRepository.findByResetpwdtoken(token);
        }else{
            attributes.addFlashAttribute("mensaje","Solicitud incorrecta");
            return "redirect:/login";
        }
        if((contra != "") && (contra != null) && (contra.equals(contraRe))){
            usuario.setContrasena(new BCryptPasswordEncoder().encode(contra));
            usuario.setResetpwdtoken(null);
            usuarioRepository.save(usuario);
            attributes.addFlashAttribute("alerta","alert-success");
            attributes.addFlashAttribute("registro","Se ha cambiado correctamente su contraseña");
        }else{
            attributes.addFlashAttribute("alerta","alert-danger");
            attributes.addFlashAttribute("registro","Las contraseñas deben ser iguales y no nulas");
            return "redirect:/validarpwd?code="+usuario.getResetpwdtoken();
        }
        return "redirect:/login";
    }


    public void sendVerification(Usuario usuario,String baseUrl) throws MessagingException, UnsupportedEncodingException {
        String subject= "Verificacion de Registro";
        String name="Mimesis App";
        String message2="<p>Estimad@ " + usuario.getNombre() + " " + usuario.getApellido()+ ",</p>";
        message2 += "<p>Presione el link de abajo para verificar su cuenta: </p>";
        String verifyUrl=baseUrl+"/verification?code="+usuario.getToken();
        System.out.println("VerifyURL: "+verifyUrl);
        message2+="<a style=\"color:#fff;background:#004177;text-decoration:none;font-weight:bold;display:inline-block;line-height:inherit\" \n href=" +verifyUrl +">Inicia tu aventura</a>";
        message2+="<p>Gracias por su Registro<br>@Mimesis teatros</p>";
        mimeMessage(usuario, subject, name, message2);
    }
    public void sendRePassword(Usuario usuario,String baseUrl) throws MessagingException, UnsupportedEncodingException {
        String subject= "Cambia tu contraseña";
        String name="Mimesis App";
        String message2="<p>Estimado " + usuario.getNombre() + " " + usuario.getApellido()+ ",</p>";
        message2 += "<p>Haga click al link de abajo para cambiar su contraseña: </p>";
        String verifyUrl=baseUrl+"/validarpwd?code="+usuario.getResetpwdtoken();
        message2+="<a" + " href=" +verifyUrl +">Cambiar Contraseña</a>";
        message2+="<p>@Mimesis teatros</p>";
        mimeMessage(usuario, subject, name, message2);
    }

    private void mimeMessage(Usuario usuario, String subject, String name, String message2) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("mimesisapplication@gmail.com",name);
        mimeMessageHelper.setTo(usuario.getCorreo());
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(message2,true);
        javaMailSender.send(mimeMessage);
    }


    @PostMapping("/registrargoogle")
    public String registrargoogle(@ModelAttribute("cliente") @Valid Usuario usuario, BindingResult bindingResult, RedirectAttributes attributes, HttpServletRequest request, Model model) throws MessagingException, UnsupportedEncodingException {
        if(bindingResult.hasErrors()){
            return "login/registrogoogle";
        }else{
            Usuario usuarioconfirm = usuarioRepository.findByCorreo(usuario.getCorreo());
            System.out.println("tamarindo1");
            if(usuarioconfirm != null){
                model.addAttribute("emailerror","Credenciales ya registradas");
                return "login/registrogoogle";
            }else{
                usuario.setRol("Cliente");
                usuario.setAuthprovider("GOOGLE");
                usuarioRepository.save(usuario);
                return "redirect:/redirectByRole";
            }
        }
    }


}
