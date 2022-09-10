package com.github.lembek.restaurant_voting.controller;

import com.github.lembek.restaurant_voting.model.Restaurant;
import com.github.lembek.restaurant_voting.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CacheConfig(cacheNames = "restaurants")
@RequestMapping(value = RestaurantController.RESTAURANT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    public static final String RESTAURANT_URL = "/restaurants";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Cacheable
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
}
