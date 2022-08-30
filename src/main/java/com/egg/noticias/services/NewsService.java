/*
// Curso Egg FullStack
 */
package com.egg.noticias.services;

// @author JulianCVidal
import com.egg.noticias.entities.Account;
import com.egg.noticias.entities.News;
import com.egg.noticias.enums.Roles;
import com.egg.noticias.exceptions.NewsException;
import com.egg.noticias.repositories.NewsRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ImageService imageService;

    @Transactional
    public void createNews(String title, String body,
            MultipartFile photo, String journalistId) throws NewsException {

        validateData(title, body, photo);
        if (null == journalistId || journalistId.isEmpty()) {
            throw new NewsException("No valid journalist id entered");
        }
        News newNews = new News();
        newNews.setTitle(title);
        newNews.setBody(body);
        newNews.setReleaseDate(new Date(System.currentTimeMillis()));
        newNews.setImage(imageService.save(photo));
        setCreator(newNews, journalistId);
        newsRepository.save(newNews);
    }

    private void setCreator(News news, String journalistId) throws NewsException {
        Account journalist = accountService.getUserById(journalistId);
        if (null == journalist) {
            throw new NewsException("No account found");
        }
        if (journalist.getAccountType() == Roles.USER) {
            throw new NewsException("The account belongs to a user, it cannot publish news");
        }
        news.setCreator(journalist);
    }

    @Transactional(readOnly = true)
    public News getNewsById(String id) throws NewsException {
        validateId(id);
        Optional<News> optNews = newsRepository.findById(id);
        if (!optNews.isPresent()) {
            throw new NewsException("No news found");
        }
        return optNews.get();
    }

    @Transactional(readOnly = true)
    public List<News> getAllNews() {
        List<News> newsList = newsRepository.getAllNews();
        return newsList;
    }

    @Transactional
    public void modifyNews(String id, String title,
            String body, MultipartFile photo) throws NewsException {
        validateData(title, body, photo);

        News news = getNewsById(id);
        news.setTitle(title);
        news.setBody(body);
        news.setReleaseDate(new Date(System.currentTimeMillis()));
        news.setImage(imageService.save(photo));
        newsRepository.save(news);

    }

    @Transactional
    //Soft delete
    public void deleteNews(String id) throws NewsException {
        News news = getNewsById(id);
        news.setDeleted(true);
        newsRepository.save(news);
    }

    private void validateData(String title, String body,
            MultipartFile photo) throws NewsException {
        if (null == title || title.isEmpty()) {
            throw new NewsException("No valid title entered");
        }
        if (null == body || body.isEmpty()) {
            throw new NewsException("No body title entered");
        }
        if (null == photo || photo.isEmpty()) {
            throw new NewsException("No valid title entered");
        }
    }

    private void validateId(String id) throws NewsException {
        if (null == id || id.isEmpty()) {
            throw new NewsException("No valid id entered");
        }
    }
}
