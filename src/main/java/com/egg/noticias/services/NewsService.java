/*
// Curso Egg FullStack
 */
package com.egg.noticias.services;

// @author JulianCVidal
import com.egg.noticias.entities.Journalist;
import com.egg.noticias.entities.News;
import com.egg.noticias.exceptions.NewsException;
import com.egg.noticias.repositories.JournalistRepository;
import com.egg.noticias.repositories.NewsRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private JournalistRepository journalistRepository;

    @Transactional
    public void createNews(String title, String body, String photo, String journalistId) throws NewsException {
        validateData(title, body, photo, journalistId);

        Optional<Journalist> journalist = journalistRepository.findById(journalistId);
        if (!journalist.isPresent()) {
            throw new NewsException("No journalist found");
        }

        News newNews = new News();
        newNews.setTitle(title);
        newNews.setBody(body);
        newNews.setPhoto(photo);
        newNews.setRelease(new Date());
        newNews.setJournalist(journalist.get());

        newsRepository.save(newNews);
    }

    @Transactional(readOnly = true)
    public News getNewsById(String id) throws NewsException {
        validateId(id);
        Optional<News> returnedNews = newsRepository.findById(id);
        if (!returnedNews.isPresent()) {
            throw new NewsException("No news found");
        }
        return returnedNews.get();
    }

    @Transactional(readOnly = true)
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    @Transactional
    public void modifyNews(String id, String title, String body, String photo, String journalistId) throws NewsException {
        validateId(id);
        validateData(title, body, photo, journalistId);

        Optional<Journalist> returnedJournalist = journalistRepository.findById(journalistId);
        if (!returnedJournalist.isPresent()) {
            throw new NewsException("No journalist found");
        }

        News newsToModify = getNewsById(id);
        newsToModify.setTitle(title);
        newsToModify.setBody(body);
        newsToModify.setPhoto(photo);
        newsToModify.setJournalist(returnedJournalist.get());

        newsRepository.save(newsToModify);

    }

    @Transactional
    public void deleteNews(String id) throws NewsException {
        validateId(id);
        News newsToDelete = getNewsById(id);
        newsToDelete.setDeleted(true);

        newsRepository.save(newsToDelete);
    }

    private void validateData(String title, String body, String photo, String journalistId) throws NewsException {
        if (null == title || title.isEmpty()) {
            throw new NewsException("No valid title entered");
        }
        if (null == body || body.isEmpty()) {
            throw new NewsException("No body title entered");
        }
        if (null == photo || photo.isEmpty()) {
            throw new NewsException("No valid title entered");
        }
        if (null == journalistId || journalistId.isEmpty()) {
            throw new NewsException("No valid journalist id entered");
        }
    }

    private void validateId(String id) throws NewsException {
        if (null == id || id.isEmpty()) {
            throw new NewsException("No valid id entered");
        }
    }
}
