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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam(required = false) MultipartFile photo,
            ModelMap model) {

        try {
            userService.signup(name, email, password, confirm, role, photo);
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
    public String index(HttpSession session) {
        NewsUser user = (NewsUser) session.getAttribute("userSession");
        return "index.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_JOURNALIST')")
    @GetMapping("/profile")
    public String profile(ModelMap model, HttpSession session) {
        NewsUser user = (NewsUser) session.getAttribute("userSession");
        model.put("user", user);
        return "profile-modify";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_JOURNALIST')")
    @PostMapping("/profile/{id}")
    public String update(MultipartFile photo, @PathVariable String id,
            @RequestParam String name, @RequestParam String email,
            @RequestParam String password, @RequestParam String confirm,
            ModelMap model, HttpSession session) {
            NewsUser user = (NewsUser) session.getAttribute("userSession");
            
        try {
            userService.update(id, name, email, password, confirm, photo);
            return user.getRole() == Roles.JOURNALIST ? "journalist-table" : "index";
        } catch (NewsException ne) {
            model.put("user", user);
            model.put("error", ne.getMessage());
            return "profile-modify";
        }
    }

}
