package com.github.lembek.RestaurantVoting.controller;

import com.github.lembek.RestaurantVoting.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.github.lembek.RestaurantVoting.PopulateTestData.*;
import static com.github.lembek.RestaurantVoting.controller.RestaurantController.RESTAURANT_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(USER_MAIL)
    void getAll() throws Exception {
        perform(get(RESTAURANT_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1, restaurant2));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getOne() throws Exception {
        perform(get(RESTAURANT_URL + FIRST_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }
}