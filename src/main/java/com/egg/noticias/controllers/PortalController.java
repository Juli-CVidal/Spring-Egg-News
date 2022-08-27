/*
// Curso Egg FullStack
 */
package com.egg.noticias.controllers;

// @author JulianCVidal
import com.egg.noticias.entities.NewsUser;
import com.egg.noticias.enums.Roles;
import com.egg.noticias.exceptions.NewsException;
import com.egg.noticias.services.NewsUserService;
import javax.servlet.http.HttpSession;
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

    @GetMapping("/sign_in")
    public String signInForm(@RequestParam(required = false) String error, ModelMap model) {
        if (null != error) {
            model.put("error", "Incorrect username or password");
        }
        model.put("action", "sign_in");
        model.put("roles", Roles.values());
        return "logs.html";
    }

    @GetMapping("/sign_up")
    public String signUpForm(ModelMap model) {
        model.put("roles", Roles.values());
        model.put("action", "sign_up");
        return "logs.html";
    }

    @PostMapping("/signup")
    public String signUpUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            String confirm,
            @RequestParam Roles role,
            ModelMap model) {

        try {
            userService.signup(name, email, password, confirm, role);
            model.put("action", "sign_in");
        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            model.put("name", name);
            model.put("action", "sign_up");
        } finally {
            model.put("roles", Roles.values());
            model.put("email", email);
        }
        return "logs.html";
    }
    

    @GetMapping("/")
    public String index( //            @RequestParam(required = false) String logged, ModelMap model
            HttpSession session) {
        NewsUser user = (NewsUser) session.getAttribute("userSession");
        return "index.html";
    }
}
