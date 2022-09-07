package com.github.lembek.RestaurantVoting.controller.admin;

import com.github.lembek.RestaurantVoting.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static com.github.lembek.RestaurantVoting.PopulateTestData.*;
import static com.github.lembek.RestaurantVoting.controller.admin.AdminRestaurantController.ADMIN_RESTAURANT_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminVoteControllerTest extends AbstractControllerTest {
    public static final String ADMIN_VOTE_TEST_URL = ADMIN_RESTAURANT_URL + "/" + FIRST_ID + "/votes";

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getRateByDate() throws Exception {
        perform(get(ADMIN_VOTE_TEST_URL + "/rate"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getAll() throws Exception {
        perform(get(ADMIN_VOTE_TEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(VOTE_MATCHER.contentJson(List.of(vote1)));
    }
}