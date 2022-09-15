package com.github.lembek.restaurant_voting.controller.admin;

import com.github.lembek.restaurant_voting.repository.VoteRepository;
import org.slf4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.github.lembek.restaurant_voting.controller.admin.AdminRestaurantController.ADMIN_RESTAURANT_URL;

@RestController
@RequestMapping(value = AdminVoteController.ADMIN_VOTE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminVoteController {
    public static final String ADMIN_VOTE_URL = ADMIN_RESTAURANT_URL + "/{id}/votes";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminVoteController.class);

    private final VoteRepository voteRepository;

    public AdminVoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping("/rate")
    public int getRateByDate(@PathVariable int id, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @RequestParam(required = false) LocalDate localDate) {
        log.info("get rate of restaurant with id={}", id);
        if (localDate == null) {
            localDate = LocalDate.now();
        }
        return voteRepository.getRate(id, localDate);
    }
}
