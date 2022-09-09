package com.github.lembek.RestaurantVoting.controller.admin;

import com.github.lembek.RestaurantVoting.AbstractControllerTest;
import com.github.lembek.RestaurantVoting.model.Role;
import com.github.lembek.RestaurantVoting.model.User;
import com.github.lembek.RestaurantVoting.repository.UserRepository;
import com.github.lembek.RestaurantVoting.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.github.lembek.RestaurantVoting.PopulateTestData.*;
import static com.github.lembek.RestaurantVoting.controller.admin.AdminController.ADMIN_USER_URL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class AdminControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(USER_MAIL)
    void getForbidden() throws Exception {
        perform(get(ADMIN_USER_URL))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getAll() throws Exception {
        perform(get(ADMIN_USER_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(USER_MATCHER.contentJson(user, admin));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getOne() throws Exception {
        perform(get(ADMIN_USER_URL + "/" + FIRST_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void remove() throws Exception {
        perform(delete(ADMIN_USER_URL + "/" + FIRST_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(userRepository.existsById(FIRST_ID));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void changeEnable() throws Exception {
        perform(patch(ADMIN_USER_URL + "/" + FIRST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .param("enabled", "false"))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(userRepository.getExisted(FIRST_ID).isEnabled());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        User prepared = getUpdatedUser();
        prepared.setRoles(Set.of(Role.USER));
        perform(put(ADMIN_USER_URL + "/" + FIRST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeAdditionProps(prepared, "password", "new password")))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userRepository.getExisted(FIRST_ID), updated);
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void create() throws Exception {
        User newUser = getNewUser();
        ResultActions action = perform(post(ADMIN_USER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeAdditionProps(getNewUser(), "password", "somePassword")))
                .andDo(print())
                .andExpect(status().isCreated());
        User user = USER_MATCHER.readFromJson(action);
        newUser.setId(user.getId());

        USER_MATCHER.assertMatch(userRepository.getExisted(user.getId()), newUser);
    }
}