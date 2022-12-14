package com.github.lembek.restaurant_voting.controller.admin;

import com.github.lembek.restaurant_voting.AbstractControllerTest;
import com.github.lembek.restaurant_voting.repository.DishRepository;
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
class AdminDishControllerTest extends AbstractControllerTest {
    public static final String ADMIN_DISH_TEST_URL = ADMIN_RESTAURANT_URL + "/" + FIRST_ID + "/dishes";

    @Autowired
    private DishRepository dishRepository;

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getLunchMenuByDate() throws Exception {
        perform(get(ADMIN_DISH_TEST_URL).param("localDate", "2000-05-11"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(DISH_MATCHER.contentJson(borch, salad));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void create() throws Exception {
        perform(post(ADMIN_DISH_TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getNewDish())))
                .andDo(print())
                .andExpect(status().isCreated());

        DISH_MATCHER.assertMatch(dishRepository.getExisted(SIXTH_ID), newDish);
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void remove() throws Exception {
        perform(delete(ADMIN_DISH_TEST_URL + "/" + FIRST_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(dishRepository.existsById(FIRST_ID));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        perform(patch(ADMIN_DISH_TEST_URL + "/" + FIRST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .param("price", "150"))
                .andDo(print())
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(dishRepository.getExisted(FIRST_ID), getUpdatedDish());
    }
}