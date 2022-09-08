package com.github.lembek.RestaurantVoting.repository;

import com.github.lembek.RestaurantVoting.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
}
