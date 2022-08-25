/*
// Curso Egg FullStack
 */
package com.egg.noticias.services;

// @author JulianCVidal
import com.egg.noticias.configs.Roles;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class NewsUserService implements UserDetailsService {

    @Autowired
    private NewsUserRepository repository;

    @Transactional
    public void signup(String name, String email, String password, String confirm) throws NewsException {
        validate(name, email, password, confirm);

        NewsUser user = new NewsUser();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRole(Roles.USER);

        repository.save(user);
    }

    private void validate(String name, String email, String password, String confirm) throws NewsException {
        if (null == name || name.isEmpty()) {
            throw new NewsException("No valid name entered");
        }

        if (null == email || email.isEmpty()) {
            throw new NewsException("No valid email entered");
        }

        if (null == password || password.isEmpty() || 6 > password.length()) {
            throw new NewsException("No valid password entered");
        }

        if (!password.equals(confirm)) {
            throw new NewsException("Both passwords must be the same");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        NewsUser newsUser = repository.getUserByEmail(email);
        if (null == newsUser){
            throw new UsernameNotFoundException("No user found");
        }
        
        List<GrantedAuthority> permissions = new ArrayList();
        
        GrantedAuthority grantedAuth = new SimpleGrantedAuthority("ROLE_" + newsUser.getRole().toString());
        
        permissions.add(grantedAuth);
        return new User(newsUser.getEmail(), newsUser.getPassword(), permissions);
        
    }
}
