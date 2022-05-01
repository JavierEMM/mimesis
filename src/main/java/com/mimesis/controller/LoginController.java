package com.mimesis.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class LoginController {
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
}
