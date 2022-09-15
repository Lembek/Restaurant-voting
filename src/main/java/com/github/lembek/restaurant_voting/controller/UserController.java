package com.github.lembek.restaurant_voting.controller;

import com.github.lembek.restaurant_voting.AuthUser;
import com.github.lembek.restaurant_voting.error.IllegalRequestDataException;
import com.github.lembek.restaurant_voting.model.User;
import com.github.lembek.restaurant_voting.model.Vote;
import com.github.lembek.restaurant_voting.repository.UserRepository;
import com.github.lembek.restaurant_voting.repository.VoteRepository;
import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;

import static com.github.lembek.restaurant_voting.util.UserUtil.prepareForRegistration;
import static com.github.lembek.restaurant_voting.util.UserUtil.prepareForUpdate;
import static com.github.lembek.restaurant_voting.util.ValidationUtil.assureIdConsistent;

@RestController
@CacheConfig(cacheNames = "users")
public class UserController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public UserController(UserRepository userRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    @GetMapping("/profile")
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get user profile with id={}", authUser.id());
        return authUser.getUser();
    }

    @CacheEvict(key = "#authUser.getUsername()")
    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete user profile with id={}", authUser.id());
        userRepository.deleteById(authUser.id());
    }

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        log.info("registration {}", user);
        User created = userRepository.save(prepareForRegistration(user));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/profile").build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @CacheEvict(key = "#authUser.getUsername()")
    @PutMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody User user) {
        log.info("put user profile with id={}", authUser.id());
        assureIdConsistent(user, authUser.id());
        User updated = authUser.getUser();
        userRepository.save(prepareForUpdate(updated, user));
    }

    @GetMapping("/profile/my-vote")
    public Vote getTodayVote(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get vote by user with id={}", authUser.id());
        return voteRepository.getByUserAndDate(LocalDate.now(), authUser.id()).orElseThrow(
                () -> {
                    throw new IllegalRequestDataException("You haven't already voted today");
                });
    }
}
