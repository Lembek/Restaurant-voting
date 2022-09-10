package com.github.lembek.restaurant_voting.controller.admin;

import com.github.lembek.restaurant_voting.model.Restaurant;
import com.github.lembek.restaurant_voting.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.github.lembek.restaurant_voting.controller.admin.AdminController.ADMIN_URL;
import static com.github.lembek.restaurant_voting.util.ValidationUtil.checkNew;

@RestController
@CacheConfig(cacheNames = "restaurants")
@RequestMapping(value = AdminRestaurantController.ADMIN_RESTAURANT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {
    public static final String ADMIN_RESTAURANT_URL = ADMIN_URL + "/restaurants";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminRestaurantController.class);

    private final RestaurantRepository restaurantRepository;

    public AdminRestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @CacheEvict(allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant create(@RequestBody @Valid Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        return restaurantRepository.save(restaurant);
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

    @CacheEvict(allEntries = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant with id={}", id);
        restaurantRepository.deleteExisted(id);
    }

    @CacheEvict(allEntries = true)
    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int id, @RequestParam String name) {
        log.info("patch restaurant with id={}", id);
        Restaurant restaurant = restaurantRepository.getExisted(id);
        restaurant.setName(name);
    }
}
