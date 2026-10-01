package com.neogiciel.spring_mcp_serveur.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    
    /*
     * Page Index
    */
    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

}
