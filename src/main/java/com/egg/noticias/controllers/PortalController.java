/*
// Curso Egg FullStack
 */
package com.egg.noticias.controllers;

// @author JulianCVidal
import com.egg.noticias.exceptions.NewsException;
import com.egg.noticias.services.NewsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalController {

    @Autowired
    private NewsUserService userService;

    @GetMapping("/")
    public String index( //            @RequestParam(required = false) String logged, ModelMap model
            ) {
        return "index.html";
    }

    @GetMapping("/sign_in")
    public String loginForm(@RequestParam(required = false) String error, ModelMap model) {
        if (null != error) {
            model.put("error", "Incorrect username or password");
        }
        model.put("action", "sign_in");
        return "logs.html";
    }

    @PostMapping("/signup")
    public String signupUser(@RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            String confirm,
            ModelMap model) {
        try {
            userService.signup(name, email, password, confirm);
            model.put("action", "sign_in");
        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            model.put("action", "sign_up");
        } finally {
            return "logs.html";
        }
    }

    @GetMapping("/sign_up")
    public String signinForm(ModelMap model) {
        model.put("action", "sign_up");
        return "logs.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_JOURNALIST')")
    @GetMapping("/logged-index")
    public String loggedIndex() {
        return "logged-index.html";
    }
}
