package com.github.lembek.RestaurantVoting.controller.admin;

import com.github.lembek.RestaurantVoting.model.User;
import com.github.lembek.RestaurantVoting.repository.UserRepository;
import com.github.lembek.RestaurantVoting.util.UserUtil;
import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.github.lembek.RestaurantVoting.util.ValidationUtil.assureIdConsistent;
import static com.github.lembek.RestaurantVoting.util.ValidationUtil.checkNew;

@RestController
@CacheConfig(cacheNames = "users")
@RequestMapping(value = AdminController.ADMIN_USER_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    public static final String ADMIN_URL = "/admin";
    public static final String ADMIN_USER_URL = ADMIN_URL + "/users";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminController.class);

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAll() {
        log.info("get all users");
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getOne(@PathVariable int id) {
        log.info("get user with id={}", id);
        return userRepository.getExisted(id);
    }

    @CacheEvict(allEntries = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete user with id={}", id);
        userRepository.deleteExisted(id);
    }

    @CacheEvict(allEntries = true)
    @PatchMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeEnable(@PathVariable int id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = userRepository.getExisted(id);
        user.setEnabled(enabled);
    }

    @CacheEvict(allEntries = true)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody User user) {
        log.info("put {} with id={}", user, id);
        assureIdConsistent(user, id);
        userRepository.save(UserUtil.prepareToSave(user));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public User create(@Valid @RequestBody User user) {
        log.info("create {}", user);
        checkNew(user);
        return userRepository.save(UserUtil.prepareToSave(user));
    }
}
