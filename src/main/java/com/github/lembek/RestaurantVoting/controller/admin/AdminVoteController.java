package com.github.lembek.RestaurantVoting.controller.admin;

import com.github.lembek.RestaurantVoting.model.Vote;
import com.github.lembek.RestaurantVoting.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.github.lembek.RestaurantVoting.controller.admin.AdminRestaurantController.ADMIN_RESTAURANT_URL;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = AdminVoteController.ADMIN_VOTE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminVoteController {
    public static final String ADMIN_VOTE_URL = ADMIN_RESTAURANT_URL + "/{id}/votes";

    private final VoteRepository voteRepository;

    @GetMapping("/rate")
    public int getRateByDate(@PathVariable int id, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @RequestParam(required = false) LocalDate localDate) {
        log.info("get rate of restaurant with id={}", id);
        return voteRepository.getRate(id, prepareLocalDate(localDate));
    }

    @GetMapping
    public List<Vote> getAll(@PathVariable int id, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @RequestParam(required = false) LocalDate localDate) {
        log.info("get all votes of restaurants with id={}", id);
        return voteRepository.getAllByRestaurant(id, prepareLocalDate(localDate));
    }

    private LocalDate prepareLocalDate(LocalDate localDate) {
        return localDate == null ? LocalDate.now() : localDate;
    }
}
