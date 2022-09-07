package com.github.lembek.RestaurantVoting.controller;

import com.github.lembek.RestaurantVoting.model.Dish;
import com.github.lembek.RestaurantVoting.repository.DishRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.github.lembek.RestaurantVoting.controller.RestaurantController.RESTAURANT_URL;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = DishController.DISH_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {
    public static final String DISH_URL = RESTAURANT_URL + "{id}/dishes";

    private final DishRepository dishRepository;

    @GetMapping
    public List<Dish> getLunchMenuForUser(@PathVariable int id) {
        log.info("get lunch menu of restaurant with id={}", id);
        return dishRepository.getLunchMenu(id, LocalDate.now());
    }
}
