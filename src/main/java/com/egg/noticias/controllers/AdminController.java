/*
// Curso Egg FullStack
 */

package com.egg.noticias.controllers;

// @author JulianCVidal

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping("/dashboard")
    public String administrativePanel(){
        return "panel";
    }
}
