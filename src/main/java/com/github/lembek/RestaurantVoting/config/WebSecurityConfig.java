package com.github.lembek.RestaurantVoting.config;


import com.github.lembek.RestaurantVoting.AuthUser;
import com.github.lembek.RestaurantVoting.model.User;
import com.github.lembek.RestaurantVoting.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;


@Slf4j
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PASSWORD_ENCODER;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("authenticated").authenticated()
                .antMatchers("/").permitAll()
                .and()
                .httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
         return email -> {
             log.debug("Authenticating '{}'", email);
             Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);
             return new AuthUser(userOptional.orElseThrow(
                     () -> new UsernameNotFoundException("Not found user with email" + email)));
         };
    }
}
