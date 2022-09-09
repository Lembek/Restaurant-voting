package com.github.lembek.RestaurantVoting.controller;

import com.github.lembek.RestaurantVoting.AuthUser;
import com.github.lembek.RestaurantVoting.model.User;
import com.github.lembek.RestaurantVoting.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.github.lembek.RestaurantVoting.util.UserUtil.prepareForRegistration;
import static com.github.lembek.RestaurantVoting.util.UserUtil.prepareForUpdate;
import static com.github.lembek.RestaurantVoting.util.ValidationUtil.assureIdConsistent;

@Slf4j
@RestController
@CacheConfig(cacheNames = "users")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/profile")
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get user profile with id={}", authUser.id());
        return authUser.getUser();
    }

    @CacheEvict(allEntries = true)
    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete user profile with id={}", authUser.id());
        userRepository.deleteById(authUser.id());
    }

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@Valid @RequestBody User user) {
        log.info("registration {}", user);
        return userRepository.save(prepareForRegistration(user));
    }

    @CacheEvict(allEntries = true)
    @PatchMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody User user) {
        log.info("put user profile with id={}", authUser.id());
        assureIdConsistent(user, authUser.id());
        User updated = authUser.getUser();
        userRepository.save(prepareForUpdate(updated, user));
    }
}
