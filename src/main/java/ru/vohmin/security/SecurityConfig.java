package ru.vohmin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@ComponentScan("ru.vohmin")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userService; // сервис, с помощью которого тащим пользователя
    private final SuccessUserHandler successUserHandler; // класс, в котором описана логика перенаправления пользователей по ролям

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public SecurityConfig(UserDetailsService userService, SuccessUserHandler successUserHandler) {
        this.userService = userService;
        this.successUserHandler = successUserHandler;
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //- попробуйте выяснить сами, что это даёт
        http.authorizeRequests()
                .antMatchers("/login", "/")
                .permitAll();
        http.authorizeRequests()
                .antMatchers("/admin/**").access("hasAnyRole('ROLE_ADMIN')") // разрешаем входить на /user пользователям с ролью User
                .antMatchers("/user/**").access("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") // разрешаем входить на /user пользователям с ролью User
                .and().formLogin()  // Spring сам подставит свою логин форму
                .loginProcessingUrl("/login")
                .permitAll()
                .successHandler(successUserHandler)
                .and()
                .logout()
                .permitAll(); // подключаем наш SuccessHandler для перенеправления по ролям
    }
}
