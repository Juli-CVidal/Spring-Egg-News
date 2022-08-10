/*
// Curso Egg FullStack
 */
package com.egg.noticias.services;

// @author JulianCVidal
import com.egg.noticias.entities.Journalist;
import com.egg.noticias.exceptions.NewsException;
import com.egg.noticias.repositories.JournalistRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JournalistService {

    @Autowired
    private JournalistRepository repository;

    @Transactional
    public void createJournalist(String name, String lastName, String photo) throws NewsException {
        validateData(name, lastName, photo);

        Journalist newJournalist = new Journalist();

        newJournalist.setName(name);
        newJournalist.setLastName(lastName);
        newJournalist.setPhoto(photo);

        repository.save(newJournalist);
    }

    @Transactional(readOnly = true)
    public Journalist getJournalistById(String id) throws NewsException {
        validateId(id);
        Optional<Journalist> returnedJournalist = repository.findById(id);
        if (!returnedJournalist.isPresent()) {
            throw new NewsException("No journalist found");
        }
        return returnedJournalist.get();
    }

    @Transactional(readOnly = true)
    public List<Journalist> getAllJournalists() {
        return repository.findAll();
    }

    @Transactional
    public void modifyJournalist(String id, String name, String lastName, String photo) throws NewsException {
        validateId(id);
        validateData(name, lastName, photo);

        Journalist journalistToModify = getJournalistById(id);
        journalistToModify.setName(name);
        journalistToModify.setLastName(lastName);
        journalistToModify.setPhoto(photo);

        repository.save(journalistToModify);
    }

    @Transactional
    public void deleteJournalist(String id) throws NewsException {
        validateId(id);
        Journalist journalistToDelete = getJournalistById(id);
        journalistToDelete.setDeleted(true);
        
        repository.save(journalistToDelete);
    }

    private void validateData(String name, String lastName, String photo) throws NewsException {
        if (null == name || name.isEmpty()) {
            throw new NewsException("No valid name entered");
        }
        if (null == lastName || lastName.isEmpty()) {
            throw new NewsException("No valid lastname entered");
        }
        if (null == photo || photo.isEmpty()) {
            throw new NewsException("No valid photo entered");
        }
    }

    private void validateId(String id) throws NewsException {
        if (null == id || id.isEmpty()) {
            throw new NewsException("No valid id entered");
        }
    }
}
