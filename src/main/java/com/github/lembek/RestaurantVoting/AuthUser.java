package com.github.lembek.RestaurantVoting;

import com.github.lembek.RestaurantVoting.model.User;
import org.springframework.lang.NonNull;

public class AuthUser extends org.springframework.security.core.userdetails.User {
    private final User user;

    public AuthUser(@NonNull User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public Integer id() {
        return user.getId();
    }

    public User getUser() {
        return this.user;
    }

    public String toString() {
        return "AuthUser(user=" + this.getUser() + ")";
    }
}
