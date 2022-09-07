package com.github.lembek.RestaurantVoting.util;

import com.github.lembek.RestaurantVoting.model.User;
import lombok.experimental.UtilityClass;

import static com.github.lembek.RestaurantVoting.config.WebSecurityConfig.PASSWORD_ENCODER;

@UtilityClass
public class UserUtil {

    public static User prepareToSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
