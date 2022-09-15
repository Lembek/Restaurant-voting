package com.github.lembek.restaurant_voting.controller;

import com.github.lembek.restaurant_voting.AuthUser;
import com.github.lembek.restaurant_voting.error.IllegalRequestDataException;
import com.github.lembek.restaurant_voting.error.VoteAlreadyExistException;
import com.github.lembek.restaurant_voting.model.Vote;
import com.github.lembek.restaurant_voting.repository.RestaurantRepository;
import com.github.lembek.restaurant_voting.repository.VoteRepository;
import com.github.lembek.restaurant_voting.util.DateTimeUtil;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.github.lembek.restaurant_voting.controller.RestaurantController.RESTAURANT_URL;

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
    public ResponseEntity<Vote> vote(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("make vote for restaurant with id={} by user with id={}", id, authUser.id());
        Vote created = voteRepository.save(new Vote(LocalDate.now(), authUser.getUser(), restaurantRepository.getReferenceById(id)));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/profile/my-vote")
                .build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void changeVote(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("change vote for restaurant with id={} by user with id={}", id, authUser.id());
        if (DateTimeUtil.timeNow().isAfter(BOUNDARY_TIME)) {
            throw new VoteAlreadyExistException("You can't change your vote after 11:00 a.m.");
        }
        Vote vote = voteRepository.getByUserAndDate(LocalDate.now(), authUser.id()).orElseThrow(
                () -> {
                    throw new IllegalRequestDataException("You haven't already voted today");
                });
        vote.setRestaurant(restaurantRepository.getReferenceById(id));
    }

    @GetMapping("/rate")
    public int getRate(@PathVariable int id) {
        log.info("get rate of restaurant with id={}", id);
        return voteRepository.getRate(id, LocalDate.now());
    }
}
