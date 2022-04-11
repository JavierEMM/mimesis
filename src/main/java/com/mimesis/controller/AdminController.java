package com.mimesis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("")
    public String paginaPrincipal(){
        return "admin/salas";
    }
}