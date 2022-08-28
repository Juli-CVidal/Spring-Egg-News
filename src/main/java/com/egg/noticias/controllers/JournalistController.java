/*
// Curso Egg FullStack
 */
package com.egg.noticias.controllers;

// @author JulianCVidal
import com.egg.noticias.entities.Journalist;
import com.egg.noticias.exceptions.NewsException;
import com.egg.noticias.services.JournalistService;
import java.util.List;
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
@RequestMapping("/journalist")
public class JournalistController {

    @Autowired
    private JournalistService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_JOURNALIST')")
    public String showJournalists(ModelMap model) {
        List<Journalist> journalists = service.getAllJournalists();
        model.put("journalists", journalists);
        return "journalist-table";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_JOURNALIST','ROLE_ADMIN')")
    public String getForm(HttpSession session, ModelMap model) {
        return "journalist-create";
    }

    @PreAuthorize("hasAnyRole('ROLE_JOURNALIST','ROLE_ADMIN')")
    @PostMapping("/add")
    public String add(@RequestParam String name, @RequestParam MultipartFile photo,
            ModelMap model
    ) {
        try {
            service.createJournalist(name ,photo);
            model.put("journalistAdded", "added successfully");

        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            return "create";
        }
        return "redirect:/journalist";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modify/{id}")
    public String modifyJournalist(@PathVariable String id, ModelMap model) {
        try {
            Journalist journalist = service.getJournalistById(id);
            model.put("journalist", journalist);

        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            return "journalist-table";

        }
        return "journalist-modify";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/modify/{id}")
    public String modifyJournalist(
            @PathVariable String id, @RequestParam String name,
            @RequestParam MultipartFile photo, ModelMap model) {

        try {
            service.modifyJournalist(id, name, photo);

        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            model.put("name", name);
            return "journalist-modify";
        }
        return "redirect:/journalist";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteJournalist(@PathVariable String id, ModelMap model) {
        try {
            service.deleteJournalist(id);
        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            return "journalist-table";
        }
        return "redirect:/journalist";
    }
}
