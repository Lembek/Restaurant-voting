package com.github.lembek.restaurant_voting.controller;

import com.github.lembek.restaurant_voting.model.Dish;
import com.github.lembek.restaurant_voting.repository.DishRepository;
import com.github.lembek.restaurant_voting.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.github.lembek.restaurant_voting.controller.RestaurantController.RESTAURANT_URL;

@RestController
@CacheConfig(cacheNames = "dishes")
public class DishController {
    public static final String DISH_URL = RESTAURANT_URL + "/{id}/dishes";
    public static final String ALL_MENU_URL = RESTAURANT_URL + "/dishes";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DishController.class);

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    public DishController(DishRepository dishRepository, RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Cacheable(key = "'getLunchMenuForUser' + #id + T(java.time.LocalDate).now()")
    @GetMapping(DISH_URL)
    public List<Dish> getMenuByRestaurantToday(@PathVariable int id) {
        log.info("get lunch menu of restaurant with id={}", id);
        restaurantRepository.getExisted(id);
        return dishRepository.getAllByDateAndRestaurant(id, LocalDate.now());
    }

    @Cacheable(key = "'getAllMenu' + T(java.time.LocalDate).now()")
    @GetMapping(ALL_MENU_URL)
    public List<Dish> getAllMenuToday() {
        log.info("get all menu today");
        return dishRepository.getAllByDate(LocalDate.now());
    }
}
