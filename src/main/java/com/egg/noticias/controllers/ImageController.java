/*
// Curso Egg FullStack
 */
package com.egg.noticias.controllers;

// @author JulianCVidal
import com.egg.noticias.entities.Image;
import com.egg.noticias.entities.NewsUser;
import com.egg.noticias.repositories.ImageRepository;
import com.egg.noticias.repositories.NewsUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    NewsUserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/profile/{id}")
    public ResponseEntity<byte[]> userImage(@PathVariable String id) {
        NewsUser user = userRepository.getUserById(id);

        byte[] image = user.getImage().getContent();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity(image, headers, HttpStatus.OK);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        Image res = imageRepository.searchById(id);

        byte[] image = res.getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity(image, headers, HttpStatus.OK);

    }

}
