/*
// Curso Egg FullStack
 */
package com.egg.noticias.services;

// @author JulianCVidal
import com.egg.noticias.entities.Image;
import com.egg.noticias.entities.Journalist;
import com.egg.noticias.exceptions.NewsException;
import com.egg.noticias.repositories.JournalistRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class JournalistService {

    @Autowired
    private JournalistRepository repository;

    @Autowired
    private ImageService imageService;

    @Transactional
    public void createJournalist(String name, MultipartFile photo
    ) throws NewsException {
        validateData(name, photo);

        Journalist newJournalist = new Journalist();

        newJournalist.setName(name);
        newJournalist.setImage(imageService.save(photo));

        repository.save(newJournalist);
    }

    @Transactional
    public void createJournalistFromUser(String id, String name,
            Image image) throws NewsException {
        validateId(id);
        if (null == name || name.isEmpty()) {
            throw new NewsException("No valid name entered");
        }
        if (null == image) {
            throw new NewsException("No image entered");
        }

        Journalist newJournalist = new Journalist();
        newJournalist.setId(id);
        newJournalist.setName(name);
        newJournalist.setImage(image);
        repository.save(newJournalist);
    }

    @Transactional(readOnly = true)
    public Journalist getJournalistById(String id) throws NewsException {
        validateId(id);
        Optional<Journalist> optJournalist = repository.findById(id);
        if (!optJournalist.isPresent()) {
            throw new NewsException("No journalist found");
        }
        return optJournalist.get();
    }

    @Transactional(readOnly = true)
    public List<Journalist> getAllJournalists() {
        return repository.getAllJournalists();
    }

    @Transactional
    public void modifyJournalist(String id, String name, MultipartFile photo) throws NewsException {
        validateId(id);
        validateData(name, photo);

        Journalist journalist = getJournalistById(id);
        journalist.setName(name);
        journalist.setImage(imageService.save(photo));

        repository.save(journalist);
    }

    @Transactional
    public void modifyJournalistFromUser(String id, String name, Image image) throws NewsException {
        validateId(id);
        if (null == name || name.isEmpty()) {
            throw new NewsException("No valid name entered");
        }
        if (null == image || image.getId().isEmpty()) {
            throw new NewsException("No valid image entered");
        }

        Journalist journalist = getJournalistById(id);
        journalist.setName(name);
        journalist.setImage(image);

        repository.save(journalist);
    }

    @Transactional
    public void deleteJournalist(String id) throws NewsException {
        Journalist journalist = getJournalistById(id);
        journalist.setDeleted(true);

        repository.save(journalist);
    }

    private void validateData(String name, MultipartFile image) throws NewsException {
        if (null == name || name.isEmpty()) {
            throw new NewsException("No valid name entered");
        }
        if (null == image || image.isEmpty()) {
            throw new NewsException("No valid image entered");
        }
    }

    private void validateId(String id) throws NewsException {
        if (null == id || id.isEmpty()) {
            throw new NewsException("No valid id entered");
        }
    }
}
