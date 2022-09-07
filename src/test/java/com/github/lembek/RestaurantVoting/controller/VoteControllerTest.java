package com.github.lembek.RestaurantVoting.controller;

import com.github.lembek.RestaurantVoting.AbstractControllerTest;
import com.github.lembek.RestaurantVoting.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.github.lembek.RestaurantVoting.PopulateTestData.*;
import static com.github.lembek.RestaurantVoting.controller.RestaurantController.RESTAURANT_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class VoteControllerTest extends AbstractControllerTest {
    public static final String VOTE_TEST_FIRST_RESTAURANT_URL = RESTAURANT_URL + "/" + FIRST_ID + "/votes";
    public static final String VOTE_TEST_SECOND_RESTAURANT_URL = RESTAURANT_URL + "/" + SECOND_ID + "/votes";
    public static final LocalTime BOUNDARY_TEST_TIME = LocalTime.of(11, 0);

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void vote() throws Exception {
        perform(post(VOTE_TEST_FIRST_RESTAURANT_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        VOTE_MATCHER.assertMatch(voteRepository.getByUserAndDate(LocalDate.now(), SECOND_ID), newVote);
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void voteSecondPerDay() throws Exception {
        perform(post(VOTE_TEST_FIRST_RESTAURANT_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void changeVote() throws Exception {
        LocalTime time = LocalTime.now();
        if (time.isBefore(BOUNDARY_TEST_TIME)) {
            perform(patch(VOTE_TEST_SECOND_RESTAURANT_URL)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            VOTE_MATCHER.assertMatch(voteRepository.getByUserAndDate(LocalDate.now(), FIRST_ID), changedVote);
        } else {
            perform(patch(VOTE_TEST_SECOND_RESTAURANT_URL)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getRate() throws Exception {
        perform(get(VOTE_TEST_FIRST_RESTAURANT_URL + "/rate"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
}