package com.github.lembek.RestaurantVoting.repository;


import com.github.lembek.RestaurantVoting.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Integer> {
}
