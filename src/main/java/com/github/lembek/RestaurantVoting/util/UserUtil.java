package com.github.lembek.RestaurantVoting.util;

import com.github.lembek.RestaurantVoting.model.Role;
import com.github.lembek.RestaurantVoting.model.User;

import java.util.Set;

import static com.github.lembek.RestaurantVoting.config.WebSecurityConfig.PASSWORD_ENCODER;

public final class UserUtil {

    private UserUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

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

    public static User prepareForUpdate(User user, User newUser) {
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail().toLowerCase());
        user.setPassword(PASSWORD_ENCODER.encode(newUser.getPassword()));
        return user;
    }
}
