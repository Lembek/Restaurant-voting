package com.github.lembek.restaurant_voting.controller.admin;

import com.github.lembek.restaurant_voting.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.github.lembek.restaurant_voting.PopulateTestData.ADMIN_MAIL;
import static com.github.lembek.restaurant_voting.PopulateTestData.FIRST_ID;
import static com.github.lembek.restaurant_voting.controller.admin.AdminRestaurantController.ADMIN_RESTAURANT_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminVoteControllerTest extends AbstractControllerTest {
    public static final String ADMIN_VOTE_TEST_URL = ADMIN_RESTAURANT_URL + "/" + FIRST_ID + "/votes";

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getRateByDate() throws Exception {
        perform(get(ADMIN_VOTE_TEST_URL + "/rate").param("localDate", "2000-05-11"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
}