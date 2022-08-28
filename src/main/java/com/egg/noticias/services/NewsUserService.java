/*
// Curso Egg FullStack
 */
package com.egg.noticias.services;

// @author JulianCVidal
import com.egg.noticias.entities.Image;
import com.egg.noticias.enums.Roles;
import com.egg.noticias.entities.NewsUser;
import com.egg.noticias.exceptions.NewsException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.egg.noticias.repositories.NewsUserRepository;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NewsUserService implements UserDetailsService {

    @Autowired
    private NewsUserRepository repository;

    @Autowired
    private JournalistService journalistService;

    @Autowired
    private ImageService imageService;

    @Transactional
    public void signup(String name, String email,
            String password, String confirm,
            Roles role, MultipartFile photo) throws NewsException {

        validate(name, email, confirm, photo);
        if (!password.equals(confirm)) {
            throw new NewsException("Both passwords must be the same");
        }

        NewsUser user = new NewsUser();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRole(null == role ? Roles.USER : role);
        Image image = imageService.save(photo);
        user.setImage(image);

        if (role == Roles.JOURNALIST) {
            journalistService.createJournalistFromUser(user.getId(), name, image);
        }

        repository.save(user);
    }

    @Transactional
    public void update(String id, String name,
            String email, String password,
            String confirm, MultipartFile newImage) throws NewsException {
        validate(name, email, confirm, newImage);

        if (!new BCryptPasswordEncoder().matches(confirm, password)) {
            throw new NewsException("Incorrect password, retry");
        }

        if (null == id || id.isEmpty()) {
            throw new NewsException("No id entered");
        }

        NewsUser user = getUser(id);
        user.setName(name);
        user.setEmail(email);
        Image oldImage = user.getImage();
        if (null == oldImage || null == oldImage.getId()){
            user.setImage(imageService.save(newImage));
        }else{
            user.setImage(imageService.update(newImage,oldImage.getId()));
        }

        if (user.getRole() == Roles.JOURNALIST) {
            journalistService.modifyJournalistFromUser(id, name, user.getImage());
        }
        
        repository.save(user);
    }

    @Transactional(readOnly = true)
    private NewsUser getUser(String id) throws NewsException {
        Optional<NewsUser> user = repository.findById(id);
        if (!user.isPresent()) {
            throw new NewsException("No user found");
        }
        return user.get();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (null == email || email.isEmpty()) {
            return null;
        }

        NewsUser newsUser = repository.getUserByEmail(email);
        if (null == newsUser) {
            return null;
        }

        List<GrantedAuthority> permissions = new ArrayList();
        GrantedAuthority grantedAuth = new SimpleGrantedAuthority("ROLE_" + newsUser.getRole().toString());
        permissions.add(grantedAuth);

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("userSession", newsUser);
        return new User(newsUser.getEmail(), newsUser.getPassword(), permissions);
    }

    private void validate(String name, String email,
            String password, MultipartFile photo) throws NewsException {
        if (null == name || name.isEmpty()) {
            throw new NewsException("No valid name entered");
        }

        if (null == email || email.isEmpty()) {
            throw new NewsException("No valid email entered");
        }

        if (null == password || password.isEmpty() || 8 > password.length()) {
            throw new NewsException("No valid password entered, try again");
        }

        if (null == photo || photo.isEmpty()) {
            throw new NewsException("No valid image entered");
        }
    }
}
