package com.github.lembek.RestaurantVoting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "dish", indexes =
        {@Index(name = "dish_restaurant_id_local_date_idx", columnList = "restaurant_id, local_date")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends NamedEntity {

    @Min(value = 10)
    @NotNull
    @Column(name = "price", nullable = false)
    private int price;

    @NotNull
    @Column(name = "local_date", nullable = false, updatable = false, columnDefinition = "date default now()")
    private LocalDate localDate;

    @ManyToOne
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
}
