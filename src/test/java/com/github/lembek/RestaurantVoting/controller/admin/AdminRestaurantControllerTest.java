package com.github.lembek.RestaurantVoting.controller.admin;

import com.github.lembek.RestaurantVoting.AbstractControllerTest;
import com.github.lembek.RestaurantVoting.repository.RestaurantRepository;
import com.github.lembek.RestaurantVoting.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import static com.github.lembek.RestaurantVoting.PopulateTestData.*;
import static com.github.lembek.RestaurantVoting.controller.admin.AdminRestaurantController.ADMIN_RESTAURANT_URL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class AdminRestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void create() throws Exception {
        perform(post(ADMIN_RESTAURANT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getNewRestaurant())))
                .andDo(print())
                .andExpect(status().isCreated());

        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getExisted(THIRD_ID), newRestaurant);
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getAll() throws Exception {
        perform(get(ADMIN_RESTAURANT_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1, restaurant2));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getOne() throws Exception {
        perform(get(ADMIN_RESTAURANT_URL + "/" + FIRST_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void remove() throws Exception {
        perform(delete(ADMIN_RESTAURANT_URL + "/" + FIRST_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(restaurantRepository.findById(FIRST_ID).isPresent());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        perform(patch(ADMIN_RESTAURANT_URL + "/" + FIRST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "updated restaurant"))
                .andDo(print())
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getExisted(FIRST_ID), getUpdatedRestaurant());
    }
}