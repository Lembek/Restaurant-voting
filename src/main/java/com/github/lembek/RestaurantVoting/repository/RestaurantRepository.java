package com.github.lembek.RestaurantVoting.repository;

import com.github.lembek.RestaurantVoting.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
}
