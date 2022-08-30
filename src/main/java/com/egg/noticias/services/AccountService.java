/*
// Curso Egg FullStack
 */
package com.egg.noticias.services;

// @author JulianCVidal
import com.egg.noticias.entities.Image;
import com.egg.noticias.enums.Roles;
import com.egg.noticias.entities.Account;
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
import javax.servlet.http.HttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import com.egg.noticias.repositories.AccountRepository;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ImageService imageService;

    @Transactional
    public void signup(String name, String password,
            String confirm, Roles role,
            MultipartFile photo) throws NewsException {
        validate(name, password, photo);

        if (null == confirm || !password.equals(confirm)) {
            throw new NewsException("Both passwords must be the same");
        }
        Account user = new Account();
        setData(user, name, password, role, photo);
        repository.save(user);
    }

    @Transactional
    public void update(String id, String name,
            String password, String confirm,
            MultipartFile newImage) throws NewsException {
        validate(name, password, newImage);

        if (!new BCryptPasswordEncoder().matches(confirm, password)) {
            throw new NewsException("Incorrect password, retry");
        }

        if (null == id || id.isEmpty()) {
            throw new NewsException("No id entered");
        }

        Account user = getUserById(id);
        user.setName(name);
        updateImage(user, user.getImage(), newImage);

        repository.save(user);
    }

    @Transactional(readOnly = true)
    public Account getUserById(String id) throws NewsException {
        return repository.searchAccountById(id);
    }

    @Transactional(readOnly = true)
    public Account getJournalistById(String id) throws NewsException {
        return repository.searchJournalistById(id);
    }

    @Transactional(readOnly = true)
    public Account getAdminById(String id) throws NewsException {
        return repository.searchAdminById(id);
    }

    @Transactional(readOnly = true)
    public List<Account> getJournalistsAndAdmins() {
        List<Account> journalistAndAdmins = new ArrayList();
        journalistAndAdmins.addAll(repository.getAllAdmins());
        journalistAndAdmins.addAll(repository.getAllJournalists());
        return journalistAndAdmins;

    }

    @Transactional
    public void dismissJournalist(String id) throws NewsException {
        Account user = getUserById(id);
        if (null == user) {
            throw new NewsException("No account found");
        }
        if (user.getAccountType() != Roles.JOURNALIST) {
            throw new NewsException("The account belongs to a " + user.getAccountType() + ", it cannot be terminated");
        }
        user.setAccountType(Roles.USER);
        repository.save(user);
    }

    private void setData(Account user, String name, String password, Roles role, MultipartFile photo) throws NewsException {
        user.setName(name);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setAccountType(role);
        Image image = imageService.save(photo);
        user.setImage(image);
    }

    //If the account did not have an associated image, I generate one, otherwise I modify the current one.
    private void updateImage(Account user, Image oldImage, MultipartFile newImage) throws NewsException {
        if (null == oldImage || null == oldImage.getId()) {
            user.setImage(imageService.save(newImage));
        } else {
            user.setImage(imageService.update(newImage, oldImage.getId()));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (null == username || username.isEmpty()) {
            throw new UsernameNotFoundException("No valid name entered");
        }

        Account newsUser = repository.searchUserByName(username);
        if (null == newsUser) {
            throw new UsernameNotFoundException("No user found");
        }

        List<GrantedAuthority> permissions = new ArrayList();
        GrantedAuthority grantedAuth = new SimpleGrantedAuthority("ROLE_" + newsUser.getAccountType().toString());
        permissions.add(grantedAuth);

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("userSession", newsUser);
        return new User(newsUser.getName(), newsUser.getPassword(), permissions);
    }

    private void validate(String name,
            String password, MultipartFile photo) throws NewsException {
        if (null == name || name.isEmpty()) {
            throw new NewsException("No valid name entered");
        }

        if (null == password || password.isEmpty() || 8 > password.length()) {
            throw new NewsException("No valid password entered, try again");
        }

        if (null == photo || photo.isEmpty()) {
            throw new NewsException("No valid image entered");
        }
    }
}
