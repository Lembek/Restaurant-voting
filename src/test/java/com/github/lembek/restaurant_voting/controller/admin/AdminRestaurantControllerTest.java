package com.github.lembek.restaurant_voting.controller.admin;

import com.github.lembek.restaurant_voting.AbstractControllerTest;
import com.github.lembek.restaurant_voting.model.Restaurant;
import com.github.lembek.restaurant_voting.repository.RestaurantRepository;
import com.github.lembek.restaurant_voting.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import static com.github.lembek.restaurant_voting.PopulateTestData.*;
import static com.github.lembek.restaurant_voting.controller.admin.AdminRestaurantController.ADMIN_RESTAURANT_URL;
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
    void createDuplicateName() throws Exception {
        Restaurant prepared = new Restaurant(null, restaurant1.getName());
        perform(post(ADMIN_RESTAURANT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(prepared)))
                .andDo(print())
                .andExpect(status().isConflict());
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