package com.github.lembek.restaurant_voting.repository;


import com.github.lembek.restaurant_voting.model.Dish;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id =:restaurantId AND d.localDate =:localDate")
    List<Dish> getAllByDateAndRestaurant(int restaurantId, LocalDate localDate);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id =:restaurantId ORDER BY d.localDate DESC ")
    List<Dish> getAllByRestaurant(int restaurantId);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.localDate =:localDate ORDER BY d.restaurant.id")
    List<Dish> getAllByDate(LocalDate localDate);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant ORDER BY d.restaurant.id")
    List<Dish> getAll();

    @Query("SELECT d FROM Dish d WHERE d.id =:dishId AND d.restaurant.id =:restaurantId")
    Dish getOneByRestaurant(int restaurantId, int dishId);

    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id =:id AND d.restaurant.id =:restaurantId")
    int deleteByIdAndRestaurant(int id, int restaurantId);
}
