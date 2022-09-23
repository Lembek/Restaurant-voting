package com.github.lembek.restaurant_voting.controller;

import com.github.lembek.restaurant_voting.AbstractControllerTest;
import com.github.lembek.restaurant_voting.repository.VoteRepository;
import com.github.lembek.restaurant_voting.util.DateTimeUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.github.lembek.restaurant_voting.PopulateTestData.*;
import static com.github.lembek.restaurant_voting.controller.RestaurantController.RESTAURANT_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class VoteControllerTest extends AbstractControllerTest {
    public static final String VOTE_TEST_FIRST_RESTAURANT_URL = RESTAURANT_URL + "/" + FIRST_ID + "/votes";
    public static final String VOTE_TEST_SECOND_RESTAURANT_URL = RESTAURANT_URL + "/" + SECOND_ID + "/votes";

    @Autowired
    private VoteRepository voteRepository;

    @AfterAll
    static void cleanup() {
        DateTimeUtil.resetTime();
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void vote() throws Exception {
        perform(post(VOTE_TEST_FIRST_RESTAURANT_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        VOTE_MATCHER.assertMatch(voteRepository.getByUserAndDate(LocalDate.now(), SECOND_ID).get(), newVote);
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
    void changeVoteAfterBoundary() throws Exception {
        DateTimeUtil.setTime("2007-12-03T11:01:30.00Z");

        perform(patch(VOTE_TEST_SECOND_RESTAURANT_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void changeVoteBeforeBoundary() throws Exception {
        DateTimeUtil.setTime("2007-12-03T10:59:30.00Z");

        perform(patch(VOTE_TEST_SECOND_RESTAURANT_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        VOTE_MATCHER.assertMatch(voteRepository.getByUserAndDate(LocalDate.now(), FIRST_ID).get(), changedVote);
    }
}