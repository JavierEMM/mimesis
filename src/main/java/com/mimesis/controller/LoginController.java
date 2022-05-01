package com.mimesis.controller;

import com.mimesis.entity.Usuario;
import com.mimesis.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String redirectByRole(Authentication auth){
        String rol="";
        for (GrantedAuthority role : auth.getAuthorities()) {
            rol = role.getAuthority();
            break;
        }
        if (rol.equals("Cliente")) {
            return "redirect:/";
        } else if(rol.equals("Operador")) {
            return "redirect:/operador";
        }else if(rol.equals("Admin")){
            return "redirect:/admin";
        }else {
            return "/loginForm";
        }
    }
    @GetMapping("/registro")
    public String registro(@ModelAttribute("usuario") Usuario usuario){
        return "login/register";
    }

    @PostMapping("/registrar")
    public String registrar(@ModelAttribute("usuario") Usuario usuario, @RequestParam("dateStr") String fecha){
        String contrasena= usuario.getContrasena();
        usuario.setContrasena(new BCryptPasswordEncoder().encode(contrasena));
        usuario.setRol("Cliente");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            usuario.setFechanacimiento(formatter.parse(fecha));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        usuarioRepository.save(usuario);
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
