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
    public void createJournalist(String name, String lastName
    //            , String photo
    ) throws NewsException {
        validateData(name, lastName
        //                , photo
        );

        Journalist newJournalist = new Journalist();

        newJournalist.setName(name);
        newJournalist.setLastName(lastName);
//        newJournalist.setPhoto(photo);

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
    public void modifyJournalist(String id, String name, String lastName
    //            , String photo
    ) throws NewsException {
        validateData(name, lastName
        //                , photo
        );

        Journalist journalist = getJournalistById(id);
        journalist.setName(name);
        journalist.setLastName(lastName);
//        journalist.setPhoto(photo);

        repository.save(journalist);
    }

    @Transactional
    public void deleteJournalist(String id) throws NewsException {
        Journalist journalist = getJournalistById(id);
        journalist.setDeleted(true);

        repository.save(journalist);
    }

    private void validateData(String name, String lastName
    //            , String photo
    ) throws NewsException {
        if (null == name || name.isEmpty()) {
            throw new NewsException("No valid name entered");
        }
        if (null == lastName || lastName.isEmpty()) {
            throw new NewsException("No valid surname entered");
        }
//        if (null == photo || photo.isEmpty()) {
//            throw new NewsException("No valid photo entered");
//        }
    }

    private void validateId(String id) throws NewsException {
        if (null == id || id.isEmpty()) {
            throw new NewsException("No valid id entered");
        }
    }
}
