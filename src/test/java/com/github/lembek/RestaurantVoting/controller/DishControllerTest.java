package com.github.lembek.RestaurantVoting.controller;

import com.github.lembek.RestaurantVoting.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.github.lembek.RestaurantVoting.PopulateTestData.*;
import static com.github.lembek.RestaurantVoting.controller.RestaurantController.RESTAURANT_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DishControllerTest extends AbstractControllerTest {
    public static final String DISH_TEST_URL = RESTAURANT_URL + "/" + FIRST_ID + "/dishes";

    @Test
    @WithUserDetails(USER_MAIL)
    void getLunchMenuForUser() throws Exception {
        perform(get(DISH_TEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(DISH_MATCHER.contentJson(fish, soup));
    }
}