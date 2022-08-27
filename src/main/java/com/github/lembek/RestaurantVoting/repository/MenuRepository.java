package com.github.lembek.RestaurantVoting.repository;

import com.github.lembek.RestaurantVoting.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
