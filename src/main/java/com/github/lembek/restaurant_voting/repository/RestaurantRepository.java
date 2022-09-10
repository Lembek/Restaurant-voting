package com.github.lembek.restaurant_voting.repository;

import com.github.lembek.restaurant_voting.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
}
