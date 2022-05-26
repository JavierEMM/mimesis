package com.mimesis.controller;

import com.mimesis.entity.Usuario;
import com.mimesis.google.CustomOAuth2User;
import com.mimesis.repository.UsuarioRepository;
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

@Controller
public class LoginController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/login")
    public String loginForm(){
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
        if (usuario.getRol().equals("Cliente")) {
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
        return "redirect:/login";
    }
    @GetMapping("/recuperarcontrasenia")
    public String nuevacontrasenia(){
        return "login/nuevacontrasenia";
    }

    @GetMapping("/cambiarcontrasenia")
    public String cambiarContrasenia(){
        return "login/cambiarcontra";
    }



    public void sendVerification(Usuario usuario,String baseUrl) throws MessagingException, UnsupportedEncodingException {
        String subject= "Por favor verifica tu registro";
        String name="Mimesis App";
        String message2="<p>Estimado " + usuario.getNombre() + " " + usuario.getApellido()+ ",</p>";
        message2 += "<p>Haga click al link de abajo para verificar su cuenta: </p>";
        String verifyUrl=baseUrl+"/verification?code="+usuario.getToken();
        System.out.println("VerifyURL: "+verifyUrl);
        message2+="<a" + " href=" +verifyUrl +">verificar</a>";
        message2+="<p>Gracias por su Registro<br>@Mimesis teatros</p>";
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("mimesisapplication@gmail.com",name);
        mimeMessageHelper.setTo(usuario.getCorreo());
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(message2,true);
        javaMailSender.send(mimeMessage);
    }
}
