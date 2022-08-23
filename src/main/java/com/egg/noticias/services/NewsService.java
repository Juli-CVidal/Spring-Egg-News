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
import org.springframework.web.multipart.MultipartFile;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private JournalistRepository journalistRepository;

    @Transactional
    public void createNews(String title, String body,
            //            MultipartFile photo,
            String journalistId) throws NewsException {
        validateData(title, body,
                //                photo,
                journalistId);

        News newNews = new News();
        newNews.setTitle(title);
        newNews.setBody(body);
//        addPhoto(newNews, photo);
        newNews.setReleaseDate(new Date(System.currentTimeMillis()));

        Journalist journalist = getFromOptional(journalistRepository.findById(journalistId));
        newNews.setJournalist(journalist);

        newsRepository.save(newNews);
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
    public void modifyNews(String id, String title, String body,
            //            MultipartFile photo, 
            String journalistId) throws NewsException {
        validateData(title, body,
                //                photo, 
                journalistId);

        News news = getNewsById(id);
        news.setTitle(title);
        news.setBody(body);
        news.setReleaseDate(new Date(System.currentTimeMillis()));
//        addPhoto(news, photo);
        Journalist journalist = getFromOptional(journalistRepository.findById(journalistId));
        news.setJournalist(journalist);

        newsRepository.save(news);

    }

    @Transactional
    public void deleteNews(String id) throws NewsException {
        News news = getNewsById(id);
        news.setDeleted(true);

        newsRepository.save(news);
    }

    private Journalist getFromOptional(Optional optJournalist) throws NewsException {
        if (!optJournalist.isPresent()) {
            throw new NewsException("No journalist found");
        }
        return (Journalist) optJournalist.get();
    }

//    private void addPhoto(News news, MultipartFile photo) {
//        try {
//            news.setPhoto(Base64.encodeBytes(photo.getBytes()));
//        } catch (IOException ioe) {
//            ioe.printStackTrace(System.out);
//        }
//    }
    private void validateData(String title, String body,
            //            MultipartFile photo, 
            String journalistId) throws NewsException {
        if (null == title || title.isEmpty()) {
            throw new NewsException("No valid title entered");
        }
        if (null == body || body.isEmpty()) {
            throw new NewsException("No body title entered");
        }
//        if (null == photo || photo.isEmpty()) {
//            throw new NewsException("No valid title entered");
//        }
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
