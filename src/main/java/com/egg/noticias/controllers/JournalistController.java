/*
// Curso Egg FullStack
 */
package com.egg.noticias.controllers;

// @author JulianCVidal
import com.egg.noticias.entities.Account;
import com.egg.noticias.exceptions.NewsException;
import com.egg.noticias.services.AccountService;
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

@Controller
@RequestMapping("/journalist")
public class JournalistController {

    @Autowired
    private AccountService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_JOURNALIST')")

    //The admins can also publish news, I also show them in the table
    public String showJournalists(ModelMap model, HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        model.put("userId", user.getId());
        List<Account> journalists = service.getJournalistsAndAdmins();
        model.put("journalists", journalists);
        return "journalist-table";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteJournalist(@PathVariable String id, ModelMap model) {
        try {
            service.dismissJournalist(id);
        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            return "journalist-table";
        }
        return "redirect:/journalist";
    }
}
