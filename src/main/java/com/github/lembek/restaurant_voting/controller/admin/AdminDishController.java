package com.github.lembek.restaurant_voting.controller.admin;

import com.github.lembek.restaurant_voting.model.Dish;
import com.github.lembek.restaurant_voting.repository.DishRepository;
import org.slf4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.github.lembek.restaurant_voting.controller.DishController.ALL_MENU_URL;

@RestController
@RequestMapping(value = AdminDishController.ADMIN_ALL_DISH_URL)
public class AdminDishController {
    public static final String ADMIN_ALL_DISH_URL = "/admin" + ALL_MENU_URL;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminDishController.class);

    private final DishRepository dishRepository;

    public AdminDishController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @GetMapping
    public List<Dish> getAllMenu(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
        log.info("get All menu");
        if (localDate == null) {
            return dishRepository.getAll();
        }
        return dishRepository.getAllByDate(localDate);
    }
}
