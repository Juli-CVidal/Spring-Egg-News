/*
// Curso Egg FullStack
 */
package com.egg.news.controllers;

// @author JulianCVidal
import com.egg.news.entities.Image;
import com.egg.news.entities.Account;
import com.egg.news.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.egg.news.repositories.AccountRepository;

@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    AccountRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/profile/{id}")
    public ResponseEntity<byte[]> userImage(@PathVariable String id) {
        Account user = userRepository.searchAccountById(id);

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
