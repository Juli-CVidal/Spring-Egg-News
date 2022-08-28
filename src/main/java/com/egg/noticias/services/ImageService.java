/*
// Curso Egg FullStack
 */
package com.egg.noticias.services;

// @author JulianCVidal
import com.egg.noticias.entities.Image;
import com.egg.noticias.exceptions.NewsException;
import com.egg.noticias.repositories.ImageRepository;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    @Autowired
    private ImageRepository repository;

    @Transactional
    public Image save(MultipartFile file) throws NewsException {
        validate(file);
        System.out.println("guardando la foto");
        try {
            Image image = new Image();
            image.setContent(file.getBytes());
            image.setMime(file.getContentType());
            image.setName(file.getName());
            return repository.save(image);

        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            return null;
        }
    }

    @Transactional
    public Image update(MultipartFile file, String id) throws NewsException {
        System.out.println("intentando hacer update a la foto");
        validate(file);

        try {
            Image image = getImage(id);
            image.setContent(file.getBytes());
            image.setMime(file.getContentType());
            image.setName(file.getName());
            return repository.save(image);

        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            return null;
        }
    }

    private void validate(MultipartFile file) throws NewsException {
        if (null == file) {
            throw new NewsException("No valid image entered");
        }
    }

    @Transactional(readOnly = true)
    public Image getImage(String id) throws NewsException {
        if (null == id || id.isEmpty()) {
            throw new NewsException("No valid id entered");
        }

        Optional<Image> image = repository.findById(id);
        if (!image.isPresent()) {
            throw new NewsException("No image found");
        }
        return image.get();
    }
}
