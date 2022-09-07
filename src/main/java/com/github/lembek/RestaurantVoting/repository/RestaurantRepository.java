package com.github.lembek.RestaurantVoting.repository;

import com.github.lembek.RestaurantVoting.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Override
    Optional<Restaurant> findById(Integer integer);
}
