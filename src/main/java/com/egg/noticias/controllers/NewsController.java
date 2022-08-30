/*
// Curso Egg FullStack
 */
package com.egg.noticias.controllers;

// @author JulianCVidal
import com.egg.noticias.entities.News;
import com.egg.noticias.entities.Account;
import com.egg.noticias.exceptions.NewsException;
import com.egg.noticias.services.AccountService;
import com.egg.noticias.services.NewsService;
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
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private AccountService accountService;

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_JOURNALIST')")
    @GetMapping
    public String showNews(ModelMap model, HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        List<News> newsList = newsService.getAllNews();
        model.put("newsList", newsList);
        return "news-table";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_JOURNALIST')")
    @GetMapping("/create")
    public String getForm(HttpSession session, ModelMap model) {
        Account user = (Account) session.getAttribute("userSession");

        model.addAttribute("journalistId", user.getId());
        return "news-create";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_JOURNALIST')")
    @PostMapping("/add")
    public String createNews(
            @RequestParam String title, @RequestParam String body,
            @RequestParam String journalistId,
            @RequestParam MultipartFile image, ModelMap model) {
        try {
            newsService.createNews(title, body, image, journalistId);

            model.put("newsAdded", "added successfully");
        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            model.put("journalistId", journalistId);
            return "news-create";
        }
        System.out.println("llego hasta acá");
        return "redirect:/news";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_JOURNALIST')")
    @GetMapping("/modify/{id}")
    public String modifyNews(@PathVariable String id, ModelMap model) {

        try {
            News news = newsService.getNewsById(id);
            model.put("news", news);
        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            return "news-table";
        }
        return "news-modify";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/modify/{id}")
    public String modifyNews(@PathVariable String id, @RequestParam String title,
            @RequestParam String body, @RequestParam MultipartFile photo, ModelMap model) {

        try {
            newsService.modifyNews(id, title, body, photo);

        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            return "news-modify";
        }
        return "redirect:/news";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteNews(@PathVariable String id, ModelMap model) {
        try {
            newsService.deleteNews(id);

        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            return "news-table";
        }
        return "redirect:/news";
    }
}
