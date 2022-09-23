package com.github.lembek.restaurant_voting.controller.admin;

import com.github.lembek.restaurant_voting.model.Dish;
import com.github.lembek.restaurant_voting.model.Restaurant;
import com.github.lembek.restaurant_voting.repository.DishRepository;
import com.github.lembek.restaurant_voting.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.lembek.restaurant_voting.controller.DishController.ALL_MENU_URL;
import static com.github.lembek.restaurant_voting.controller.DishController.DISH_URL;
import static com.github.lembek.restaurant_voting.controller.admin.AdminRestaurantController.ADMIN_RESTAURANT_URL;
import static com.github.lembek.restaurant_voting.util.ValidationUtil.*;

@RestController
@CacheConfig(cacheNames = "dishes")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController {
    public static final String ADMIN_DISH_URL = ADMIN_RESTAURANT_URL + "/{id}/dishes";
    public static final String ADMIN_ALL_DISH_URL = "/admin" + ALL_MENU_URL;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminDishController.class);

    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    public AdminDishController(RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @GetMapping(ADMIN_DISH_URL)
    public List<Dish> getLunchMenuByDate(@PathVariable int id, @RequestParam(required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
        restaurantRepository.getExisted(id);
        if (localDate == null) {
            log.info("get all lunch menu of restaurant id={} ", id);
            return dishRepository.getAllByRestaurant(id);
        }
        log.info("get lunch menu of restaurant id={} on date={}", id, localDate);
        return dishRepository.getAllByDateAndRestaurant(id, localDate);
    }

    @Caching(evict = {
            @CacheEvict(key = "'getLunchMenuForUser' + #id + T(java.time.LocalDate).now()"),
            @CacheEvict(key = "'getAllMenu' + T(java.time.LocalDate).now()")
    })
    @PostMapping(value = ADMIN_DISH_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Dish> create(@PathVariable int id, @RequestBody @Valid Dish dish) {
        log.info("create {}", dish);
        checkNew(dish);
        Restaurant restaurant = restaurantRepository.getReferenceById(id);
        dish.setRestaurant(restaurant);
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DISH_URL + "/{dishId}")
                .buildAndExpand(id, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Caching(evict = {
            @CacheEvict(key = "'getLunchMenuForUser' + #id + T(java.time.LocalDate).now()"),
            @CacheEvict(key = "'getAllMenu' + T(java.time.LocalDate).now()")
    })
    @DeleteMapping(ADMIN_DISH_URL + "/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int dishId) {
        log.info("delete dish with id={} and restaurantId={}", dishId, id);
        checkModification(dishRepository.deleteByIdAndRestaurant(dishId, id), dishId);
    }

    @Caching(evict = {
            @CacheEvict(key = "'getLunchMenuForUser' + #id + T(java.time.LocalDate).now()"),
            @CacheEvict(key = "'getAllMenu' + T(java.time.LocalDate).now()")
    })
    @PatchMapping(ADMIN_DISH_URL + "/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void patch(@PathVariable int id, @PathVariable int dishId,
                      @RequestParam(required = false) String name,
                      @RequestParam(required = false) Integer price) {
        log.info("patch dish with id={} and restaurantId={}", dishId, id);
        Dish dish = dishRepository.getOneByRestaurant(id, dishId);
        checkDish(dish, dishId, id);
        if (StringUtils.hasText(name)) {
            dish.setName(name);
        }
        if (price != null) {
            dish.setPrice(price);
        }
    }

    @Caching(evict = {
            @CacheEvict(key = "'getLunchMenuForUser' + #id + T(java.time.LocalDate).now()"),
            @CacheEvict(key = "'getAllMenu' + T(java.time.LocalDate).now()")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(ADMIN_DISH_URL + "/{dishId}")
    @Transactional
    public void update(@PathVariable int id, @PathVariable int dishId, @RequestBody Dish newDish) {
        log.info("put dish with id={} and restaurantId={}", dishId, id);
        Dish dish = dishRepository.getOneByRestaurant(id, dishId);
        checkDish(dish, dishId, id);
        assureIdConsistent(newDish, dishId);
        newDish.setRestaurant(restaurantRepository.getReferenceById(id));
        dishRepository.save(newDish);
    }

    @Transactional
    @GetMapping(ADMIN_DISH_URL + "/{dishId}")
    public Dish getOne(@PathVariable int id, @PathVariable int dishId) {
        log.info("get dish with id={} and restaurantId={}", dishId, id);
        restaurantRepository.getExisted(id);
        Dish dish = dishRepository.getOneByRestaurant(id, dishId);
        checkDish(dish, dishId, id);
        return dish;
    }

    @GetMapping(ADMIN_ALL_DISH_URL)
    public List<Dish> getAllMenu(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
        log.info("get All menu");
        if (localDate == null) {
            return dishRepository.getAll();
        }
        return dishRepository.getAllByDate(localDate);
    }
}
