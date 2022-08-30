/*
// Curso Egg FullStack
 */
package com.egg.noticias;

// @author JulianCVidal
import com.egg.noticias.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountService accountService;

   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
       auth.userDetailsService(accountService).passwordEncoder(new BCryptPasswordEncoder());
   }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/news/*").hasAnyRole("ADMIN", "JOURNALIST")
                .antMatchers("/journalist/*").hasAnyRole("ADMIN","JOURNALIST")
                .antMatchers("/css/*", "/js/*", "/imgs/*", "/**")
                    .permitAll()
                .and().formLogin()
                    .loginPage("/sign_in")
                    .loginProcessingUrl("/signin")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/")
                    .permitAll()
                .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/sign_in")
                    .permitAll()
                .and().csrf()
                    .disable();
    }
}
