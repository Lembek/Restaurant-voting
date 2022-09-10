package com.github.lembek.restaurant_voting.controller;

import com.github.lembek.restaurant_voting.AbstractControllerTest;
import com.github.lembek.restaurant_voting.model.Role;
import com.github.lembek.restaurant_voting.model.User;
import com.github.lembek.restaurant_voting.repository.UserRepository;
import com.github.lembek.restaurant_voting.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.github.lembek.restaurant_voting.PopulateTestData.*;
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
        perform(patch("/profile")
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
                .content(JsonUtil.writeAdditionProps(newUser, "password", "somePassword")))
                .andDo(print())
                .andExpect(status().isCreated());
        User user = USER_MATCHER.readFromJson(action);
        newUser.setId(user.getId());
        newUser.setRoles(Set.of(Role.USER));

        USER_MATCHER.assertMatch(userRepository.getExisted(user.getId()), newUser);
    }

    @Test
    void registerDuplicateEmail() throws Exception {
        User prepared = new User(user);
        perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeAdditionProps(prepared, "password", prepared.getPassword())))
                .andDo(print())
                .andExpect(status().isConflict());
    }

}