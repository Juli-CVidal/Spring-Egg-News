/*
// Curso Egg FullStack
 */
package com.egg.noticias.controllers;

// @author JulianCVidal
import com.egg.noticias.entities.Journalist;
import com.egg.noticias.entities.News;
import com.egg.noticias.exceptions.NewsException;
import com.egg.noticias.services.JournalistService;
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
//import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private JournalistService journalistService;

    @GetMapping
    public String showNews(ModelMap model) {
        List<News> newsList = newsService.getAllNews();
        System.out.println("Hay " + newsList.size() + " noticias");
        model.put("newsList", newsList);
        return "news-table.html";
    }

    @GetMapping("/create")
    public String getForm(ModelMap model) {
        List<Journalist> journalists = journalistService.getAllJournalists();
        model.addAttribute("journalists", journalists);
        return "news-create.html";
    }

    @PostMapping("/add")
    public String createNews(
            @RequestParam String title, @RequestParam String body,
            @RequestParam String journalistId,
            //            @RequestParam("file") MultipartFile photo,
            ModelMap model) {

        try {
            newsService.createNews(title, body,
                    //                    photo, 
                    journalistId);

            model.put("newsAdded", "added successfully");
        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            return "create";
        }
        return "redirect:/news";
    }

    
    
    
    @GetMapping("/modify/{id}")
    public String modifyNews(@PathVariable String id, ModelMap model) {

        try {
            News news = newsService.getNewsById(id);
            model.put("news", news);
            List<Journalist> journalists = journalistService.getAllJournalists();
            model.put("journalists", journalists);
        } catch (NewsException ne) {
            model.put("error", ne.getMessage());
            return "/";
        }
        return "news-modify";
    }
    
    @PostMapping("/modify/{id}")
    public String modifyNews(@PathVariable String id, @RequestParam String title,
            @RequestParam String body,
//            @RequestParam String photo,
            @RequestParam String journalistId, ModelMap model){
        
        try{
            newsService.modifyNews(id, title, body, journalistId);
            
        }catch(NewsException ne){
            model.put("error", ne.toString());
            return "news-modify";
        }
        return "redirect:/news";
    }
//
//    @PostMapping("/delete/{code}")
//    public String deleteNews(@PathVariable String id, ModelMap model) {
//        try {
//            service.deleteNews(id);
//
//        } catch (NewsException ne) {
//            model.put("error", ne.getMessage());
//            return "news-delete";
//        }
//        return "redirect:/news";
//    }
}
