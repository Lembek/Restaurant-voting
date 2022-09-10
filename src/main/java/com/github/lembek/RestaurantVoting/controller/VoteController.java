package com.github.lembek.RestaurantVoting.controller;

import com.github.lembek.RestaurantVoting.AuthUser;
import com.github.lembek.RestaurantVoting.error.IllegalRequestDataException;
import com.github.lembek.RestaurantVoting.error.VoteAlreadyExistException;
import com.github.lembek.RestaurantVoting.model.Vote;
import com.github.lembek.RestaurantVoting.repository.RestaurantRepository;
import com.github.lembek.RestaurantVoting.repository.VoteRepository;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.github.lembek.RestaurantVoting.controller.RestaurantController.RESTAURANT_URL;

@RestController
@RequestMapping(value = VoteController.VOTE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    private static final LocalTime BOUNDARY_TIME = LocalTime.of(11, 0);
    public static final String VOTE_URL = RESTAURANT_URL + "/{id}/votes";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(VoteController.class);

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    public VoteController(VoteRepository voteRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Transactional
    public Vote vote(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("make vote for restaurant with id={} by user with id={}", id, authUser.id());
        return voteRepository.save(new Vote(LocalDate.now(), authUser.getUser(), restaurantRepository.getReferenceById(id)));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void changeVote(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("change vote for restaurant with id={} by user with id={}", id, authUser.id());
        Vote vote = voteRepository.getByUserAndDate(LocalDate.now(), authUser.id());
        if (vote == null) {
            throw new IllegalRequestDataException("You haven't already voted today");
        }
        if (LocalTime.now().isAfter(BOUNDARY_TIME)) {
            throw new VoteAlreadyExistException("You can't change your vote after 11:00 a.m.");
        }
        vote.setRestaurant(restaurantRepository.getReferenceById(id));
    }

    @GetMapping("/rate")
    public int getRate(@PathVariable int id) {
        log.info("get rate of restaurant with id={}", id);
        return voteRepository.getRate(id, LocalDate.now());
    }
}
