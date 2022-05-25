package com.mimesis.controller;

import com.mimesis.entity.Usuario;
import com.mimesis.google.CustomOAuth2User;
import com.mimesis.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
public class LoginController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String loginForm(){
        return "login/login";
    }
    @GetMapping("/redirectByRole")
    public String redirectByRole(
            Authentication auth, HttpSession session, Model model){
        Usuario usuario;
        try{
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) auth.getPrincipal();
            usuario = usuarioRepository.findByCorreo(customOAuth2User.getEmail());
            System.out.println("ROL: "+usuario.getRol());
            if(usuario.getRol().equals("Operador") || usuario.getRol().equals("Admin")){
                auth.setAuthenticated(false);
                session.invalidate();
                model.addAttribute("mensaje","Ingrese por el usuario y contraseña establecido por la empresa");
                return "login/login";
            }
        }catch (Exception exception){
            usuario = usuarioRepository.findByCorreo(auth.getName());
            System.out.println("ROL: "+usuario.getRol());
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
    public String registrar(@ModelAttribute("usuario") Usuario usuario, RedirectAttributes attributes){
        String contrasena= usuario.getContrasena();
        usuario.setContrasena(new BCryptPasswordEncoder().encode(contrasena));
        usuario.setRol("Cliente");
        usuarioRepository.save(usuario);
        //Tengo que enviar correo electronico
        attributes.addFlashAttribute("registro","Se le ha enviado un correo de confirmacion a su correo electronico");
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
}
