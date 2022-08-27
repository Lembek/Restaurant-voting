package com.github.lembek.RestaurantVoting.repository;

import com.github.lembek.RestaurantVoting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
}
