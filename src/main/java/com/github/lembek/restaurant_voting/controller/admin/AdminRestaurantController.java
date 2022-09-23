package com.github.lembek.restaurant_voting.controller.admin;

import com.github.lembek.restaurant_voting.model.Restaurant;
import com.github.lembek.restaurant_voting.repository.RestaurantRepository;
import com.github.lembek.restaurant_voting.repository.VoteRepository;
import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.lembek.restaurant_voting.controller.RestaurantController.RESTAURANT_URL;
import static com.github.lembek.restaurant_voting.controller.admin.AdminUserController.ADMIN_URL;
import static com.github.lembek.restaurant_voting.util.ValidationUtil.assureIdConsistent;
import static com.github.lembek.restaurant_voting.util.ValidationUtil.checkNew;

@RestController
@CacheConfig(cacheNames = {"restaurants", "dishes"})
@RequestMapping(value = AdminRestaurantController.ADMIN_RESTAURANT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {
    public static final String ADMIN_RESTAURANT_URL = ADMIN_URL + "/restaurants";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminRestaurantController.class);

    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;

    public AdminRestaurantController(RestaurantRepository restaurantRepository, VoteRepository voteRepository) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@RequestBody @Valid Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(RESTAURANT_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return restaurantRepository.findAll();
    }

    @GetMapping("/{id}")
    public Restaurant getOne(@PathVariable int id) {
        log.info("get restaurant with id={}", id);
        return restaurantRepository.getExisted(id);
    }

    @Caching(evict = {
            @CacheEvict(value = "restaurants", allEntries = true),
            @CacheEvict(value = "dishes", key = "'getLunchMenuForUser' + #id + T(java.time.LocalDate).now()"),
            @CacheEvict(value = "dishes", key = "'getAllMenu' + T(java.time.LocalDate).now()")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant with id={}", id);
        restaurantRepository.deleteExisted(id);
    }

    @Caching(evict = {
            @CacheEvict(value = "restaurants", allEntries = true),
            @CacheEvict(value = "dishes", key = "'getAllMenu' + T(java.time.LocalDate).now()")
    })
    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void updateName(@PathVariable int id, @RequestParam String name) {
        log.info("patch restaurant with id={}", id);
        Restaurant restaurant = restaurantRepository.getExisted(id);
        restaurant.setName(name);
    }

    @Caching(evict = {
            @CacheEvict(value = "restaurants", allEntries = true),
            @CacheEvict(value = "dishes", key = "'getAllMenu' + T(java.time.LocalDate).now()")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @RequestBody Restaurant restaurant) {
        log.info("update restaurant with id={}", id);
        assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }

    @GetMapping("{id}/votes/rate")
    public int getRateByDate(@PathVariable int id, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @RequestParam(required = false) LocalDate localDate) {
        log.info("get rate of restaurant with id={}", id);
        if (localDate == null) {
            localDate = LocalDate.now();
        }
        return voteRepository.getRate(id, localDate);
    }
}
