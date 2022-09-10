package com.github.lembek.RestaurantVoting;

import com.github.lembek.RestaurantVoting.controller.MatcherFactory;
import com.github.lembek.RestaurantVoting.model.*;

import java.time.LocalDate;

public class PopulateTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingEqualsComparator(Dish.class);
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingEqualsComparator(Restaurant.class);
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static final int FIRST_ID = 1;
    public static final int SECOND_ID = 2;
    public static final int THIRD_ID = 3;
    public static final int FOURTH_ID = 4;
    public static final int FIFTH_ID = 5;
    public static final int SIXTH_ID = 6;

    public static final String USER_MAIL = "user@gmail.com";
    public static final String ADMIN_MAIL = "admin@javaops.ru";

    public static final User user = new User(FIRST_ID, "User", "{noop}password", "user@gmail.com", Role.USER);
    public static final User admin = new User(SECOND_ID, "Admin", "{noop}admin", "admin@javaops.ru", Role.ADMIN, Role.USER);
    public static final User updated = new User(FIRST_ID, "updated user", "{noop}new password", "newemail@gmail.com", Role.USER);

    public static final Restaurant restaurant1 = new Restaurant(FIRST_ID, "Restaurant1");
    public static final Restaurant restaurant2 = new Restaurant(SECOND_ID, "Restaurant2");
    public static final Restaurant newRestaurant = new Restaurant(THIRD_ID, "NewRestaurant");

    public static final Dish fish = new Dish(FIRST_ID, "Fish", 100, restaurant1);
    public static final Dish soup = new Dish(SECOND_ID, "Soup", 50, restaurant1);
    public static final Dish steak = new Dish(THIRD_ID, "Steak", 90, restaurant2);
    public static final Dish borch = new Dish(FOURTH_ID, "Borch", 60, restaurant2);
    public static final Dish salad = new Dish(FIFTH_ID, "Salad", 45, LocalDate.of(2000, 5, 11), restaurant1);
    public static final Dish newDish = new Dish(SIXTH_ID, "new dish", 100, LocalDate.now(), restaurant1);

    public static final Vote vote1 = new Vote(FIRST_ID, LocalDate.now(), user, restaurant1);
    public static final Vote vote2 = new Vote(SECOND_ID, LocalDate.of(2000, 5, 11), user, restaurant1);
    public static final Vote newVote = new Vote(THIRD_ID, LocalDate.now(), admin, restaurant2);
    public static final Vote changedVote = new Vote(FIRST_ID, LocalDate.now(), user, restaurant2);

    private PopulateTestData() {
    }

    public static Dish getNewDish() {
        return new Dish(null, "new dish", 100, LocalDate.now());
    }

    public static Dish getUpdatedDish() {
        return new Dish(FIRST_ID, "Fish", 150, restaurant1);
    }

    public static Restaurant getNewRestaurant() {
        return new Restaurant(null, "NewRestaurant");
    }

    public static Restaurant getUpdatedRestaurant() {
        return new Restaurant(FIRST_ID, "updated restaurant");
    }

    public static User getNewUser() {
        return new User(null, "new User", "somePassword", "email@yadex.ru");
    }

    public static User getUpdatedUser() {
        return new User(null, "updated user", "new password", "newemail@gmail.com");
    }
}
