package com.github.lembek.restaurant_voting.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "dish", indexes =
        {@Index(name = "dish_restaurant_id_local_date_idx", columnList = "restaurant_id, local_date")})
public class Dish extends NamedEntity {

    @NotNull
    @Column(name = "price", nullable = false)
    private int price;

    @NotNull
    @Column(name = "local_date", nullable = false, updatable = false, columnDefinition = "date default now()")
    private LocalDate localDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Dish(Integer id, String name, int price, LocalDate localDate, Restaurant restaurant) {
        super(id, name);
        this.price = price;
        this.localDate = localDate;
        this.restaurant = restaurant;
    }

    public Dish(Integer id, String name, int price, LocalDate localDate) {
        super(id, name);
        this.price = price;
        this.localDate = localDate;
    }

    public Dish(Integer id, String name, int price, Restaurant restaurant) {
        this(id, name, price, LocalDate.now(), restaurant);
    }

    protected Dish() {
    }

    public @NotNull int getPrice() {
        return this.price;
    }

    public @NotNull LocalDate getLocalDate() {
        return this.localDate;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setPrice(@NotNull int price) {
        this.price = price;
    }

    public void setLocalDate(@NotNull LocalDate localDate) {
        this.localDate = localDate;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
