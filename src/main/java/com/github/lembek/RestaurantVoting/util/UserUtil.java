package com.github.lembek.RestaurantVoting.util;

import com.github.lembek.RestaurantVoting.model.Role;
import com.github.lembek.RestaurantVoting.model.User;
import lombok.experimental.UtilityClass;

import java.util.Set;

import static com.github.lembek.RestaurantVoting.config.WebSecurityConfig.PASSWORD_ENCODER;

@UtilityClass
public class UserUtil {

    public static User prepareToSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }

    public static User prepareForRegistration(User user) {
        User prepared = prepareToSave(user);
        prepared.setRoles(Set.of(Role.USER));
        return prepared;
    }

    public User prepareForUpdate(User user, User newUser) {
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail().toLowerCase());
        user.setPassword(PASSWORD_ENCODER.encode(newUser.getPassword()));
        return user;
    }
}
