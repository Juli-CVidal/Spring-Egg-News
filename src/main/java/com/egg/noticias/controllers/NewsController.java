/*
// Curso Egg FullStack
 */
package com.egg.noticias.controllers;

// @author JulianCVidal
import com.egg.noticias.entities.News;
import com.egg.noticias.exceptions.NewsException;
import com.egg.noticias.services.NewsService;
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
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService service;

    @GetMapping
    public String showNews(ModelMap model) {
        List<News> newsList = service.getAllNews();
        model.put("news", newsList);
        return "news_table";
    }

    @GetMapping("/create")
    public String getForm() {
        return "news_create";
    }

    @GetMapping("/form/{code}")
    public String getForm(@PathVariable String id, ModelMap model) {
        try {
            News news = service.getNewsById(id);
            model.put("news", news);
        } catch (NewsException ne) {
            return "redirect:/";
        }
        return "news-modify";
    }

    @PostMapping("/create")
    public String createNews(
            @RequestParam String title, @RequestParam String body,
            @RequestParam String photo, @RequestParam String journalistId,
            ModelMap model) {

        try {
            service.createNews(title, body, photo, journalistId);

        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            return "news-create";
        }
        return "redirect:/news";
    }

    @PostMapping("/modify/{code}")
    public String modifyNews(@PathVariable String id, @RequestParam String title,
            @RequestParam String body, @RequestParam String photo,
            @RequestParam String journalistId, ModelMap model) {

        try {
            service.modifyNews(id, title, body, photo, journalistId);

        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            return "news-modify";
        }
        return "redirect:/news";
    }

    @PostMapping("/delete/{code}")
    public String deleteNews(@PathVariable String id, ModelMap model) {
        try {
            service.deleteNews(id);

        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            return "news-delete";
        }
        return "redirect:/news";
    }
}
