package com.github.lembek.RestaurantVoting.controller.admin;

import com.github.lembek.RestaurantVoting.model.Dish;
import com.github.lembek.RestaurantVoting.model.Restaurant;
import com.github.lembek.RestaurantVoting.repository.DishRepository;
import com.github.lembek.RestaurantVoting.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static com.github.lembek.RestaurantVoting.controller.admin.AdminRestaurantController.ADMIN_RESTAURANT_URL;
import static com.github.lembek.RestaurantVoting.util.ValidationUtil.assureIdConsistent;
import static com.github.lembek.RestaurantVoting.util.ValidationUtil.checkNew;

@Slf4j
@RestController
@CacheConfig(cacheNames = "dishes")
@AllArgsConstructor
@RequestMapping(value = AdminDishController.ADMIN_DISH_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController {
    public static final String ADMIN_DISH_URL = ADMIN_RESTAURANT_URL + "/{id}/dishes";

    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    @GetMapping
    public List<Dish> getLunchMenuByDate(@PathVariable int id, @RequestParam(required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
        if (localDate == null) {
            log.info("get all lunch menu of restaurant id={} ", id);
            return dishRepository.getAllByRestaurant(id);
        }
        log.info("get lunch menu of restaurant id={} on date={}", id, localDate);
        return dishRepository.getLunchMenu(id, localDate);
    }

    @CacheEvict(allEntries = true)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public Dish create(@PathVariable int id, @RequestBody @Valid Dish dish) {
        log.info("create {}", dish);
        checkNew(dish);
        Restaurant restaurant = restaurantRepository.getReferenceById(id);
        dish.setRestaurant(restaurant);
        return dishRepository.save(dish);
    }

    @CacheEvict(allEntries = true)
    @DeleteMapping("/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable int id, @PathVariable int dishId) {
        log.info("delete dish with id={} and restaurantId={}", dishId, id);
        Dish dish = dishRepository.getExisted(dishId);
        assureIdConsistent(dish.getRestaurant(), id);
        dishRepository.deleteExisted(dishId);
    }

    @CacheEvict(allEntries = true)
    @PatchMapping(value = "/{dishId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int id, @PathVariable int dishId,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) int price) {
        log.info("patch dish with id={} and restaurantId={}", dishId, id);
        Dish dish = dishRepository.getExisted(dishId);
        assureIdConsistent(dish.getRestaurant(), id);
        if (StringUtils.hasText(name)) {
            dish.setName(name);
        }
        if (price != 0) {
            dish.setPrice(price);
        }
    }

    @GetMapping("/{dishId}")
    public Dish getOne(@PathVariable int id, @PathVariable int dishId) {
        log.info("get dish with id={} and restaurantId={}", dishId, id);
        Dish dish = dishRepository.getExisted(dishId);
        assureIdConsistent(dish.getRestaurant(), id);
        return dish;
    }
}
