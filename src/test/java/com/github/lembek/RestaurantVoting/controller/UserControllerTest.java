package com.github.lembek.RestaurantVoting.controller;

import com.github.lembek.RestaurantVoting.AbstractControllerTest;
import com.github.lembek.RestaurantVoting.model.User;
import com.github.lembek.RestaurantVoting.repository.UserRepository;
import com.github.lembek.RestaurantVoting.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.github.lembek.RestaurantVoting.PopulateTestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class UserControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void getUnAuth() throws Exception {
        perform(get("/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getMySelf() throws Exception {
        perform(get("/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void deleteMySelf() throws Exception {
        perform(delete("/profile"))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(userRepository.existsById(FIRST_ID));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void updateMySelf() throws Exception {
        perform(put("/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeAdditionProps(getUpdatedUser(), "password", "new password")))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userRepository.getExisted(FIRST_ID), updated);
    }

    @Test
    void register() throws Exception {
        User newUser = getNewUser();
        ResultActions action = perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeAdditionProps(getNewUser(), "password", "somePassword")))
                .andDo(print())
                .andExpect(status().isCreated());
        User user = USER_MATCHER.readFromJson(action);
        newUser.setId(user.getId());

        USER_MATCHER.assertMatch(userRepository.getExisted(user.getId()), newUser);
    }

}