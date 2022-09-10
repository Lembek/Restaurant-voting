package com.github.lembek.restaurant_voting.repository;

import com.github.lembek.restaurant_voting.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v JOIN FETCH v.user JOIN FETCH v.restaurant WHERE v.localDate =:localDate AND v.restaurant.id =:restaurantId")
    List<Vote> getAllByRestaurant(int restaurantId, LocalDate localDate);

    @Query("SELECT v FROM Vote v JOIN FETCH v.user JOIN FETCH v.restaurant WHERE v.localDate =:localDate AND v.user.id=:userId")
    Vote getByUserAndDate(LocalDate localDate, int userId);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.localDate =:localDate AND v.restaurant.id =:restaurantId")
    int getRate(int restaurantId, LocalDate localDate);
}
