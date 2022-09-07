package com.github.lembek.RestaurantVoting.repository;


import com.github.lembek.RestaurantVoting.model.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.restaurant.id =:restaurantId AND d.localDate =:localDate")
    List<Dish> getLunchMenu(int restaurantId, LocalDate localDate);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.restaurant.id =:restaurantId ORDER BY d.localDate DESC ")
    List<Dish> getAllByRestaurant(int restaurantId);
}
