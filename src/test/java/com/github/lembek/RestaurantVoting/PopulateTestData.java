package com.github.lembek.RestaurantVoting;

import com.github.lembek.RestaurantVoting.model.*;

import java.time.LocalDate;

public class PopulateTestData {

    public static final int FIRST_ID = 1;
    public static final int SECOND_ID = 2;
    public static final int THIRD_ID = 3;
    public static final int FOURTH_ID = 4;
    public static final int FIFTH_ID = 5;

    public static final User user = new User(FIRST_ID, "User", "{noop}password", "user@gmail.com", Role.USER);
    public static final User admin = new User(SECOND_ID, "Admin", "{noop}admin", "admin@javaops.ru", Role.ADMIN, Role.USER);

    public static final Restaurant restaurant1 = new Restaurant(FIRST_ID, "Restaurant1");
    public static final Restaurant restaurant2 = new Restaurant(SECOND_ID, "Restaurant2");

    public static final Dish fish = new Dish(FIRST_ID, "Fish", 100, restaurant1);
    public static final Dish soup = new Dish(SECOND_ID, "Soup", 50, restaurant1);
    public static final Dish steak = new Dish(THIRD_ID, "Steak", 90, restaurant1);
    public static final Dish borch = new Dish(FOURTH_ID, "Borch", 60, restaurant1);
    public static final Dish salad = new Dish(FIFTH_ID, "Salad", 45, restaurant1);

    public static final Vote vote1 = new Vote(FIRST_ID, LocalDate.now(), user, restaurant1);
    public static final Vote vote2 = new Vote(SECOND_ID, LocalDate.of(2000, 5,11), user, restaurant1);
}
