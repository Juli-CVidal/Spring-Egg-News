/*
// Curso Egg FullStack
 */
package com.egg.noticias.controllers;

// @author JulianCVidal
import com.egg.noticias.entities.Journalist;
import com.egg.noticias.exceptions.NewsException;
import com.egg.noticias.services.JournalistService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/journalist")
public class JournalistController {

    @Autowired
    private JournalistService service;

    @GetMapping
    public String showJournalists(ModelMap model) {
        List<Journalist> journalists = service.getAllJournalists();
        model.put("journalists", journalists);
        return "journalist-table";
    }

    @GetMapping("/create")
    public String getForm() {
        return "journalist-create.html";
    }

    @PostMapping("/add")
    public String add(@RequestParam String name, @RequestParam String lastName,
            //     @RequestParam String photo,
            ModelMap model
    ) {
        try {
            service.createJournalist(name, lastName
            //            ,photo
            );
            model.put("journalistAdded", "added successfully");

        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            return "create";
        }
        return "redirect:/journalist";
    }

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

    @PostMapping("/modify/{id}")
    public String modifyJournalist(
            @PathVariable String id, @RequestParam String name,
            @RequestParam String lastName,
            //            @RequestParam String photo,
            ModelMap model) {

        try {
            service.modifyJournalist(id, name, lastName
            //                    , photo
            );

        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            return "journalist-modify";
        }
        return "redirect:/journalist";

    }

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
