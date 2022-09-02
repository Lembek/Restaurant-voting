package com.github.lembek.RestaurantVoting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "local_date"},
        name = "menu_unique_restaurant_local_date_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @NotNull
    @Column(name = "local_date", nullable = false, updatable = false)
    private LocalDate localDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "menu_dish",
               joinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "dish_id", referencedColumnName = "id")})
    private List<Dish> lunchMenu;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false, updatable = false)
    private Restaurant restaurant;

    public Menu(Integer id, LocalDate localDate, Restaurant restaurant, Dish...dishes) {
        super(id);
        this.localDate = localDate;
        this.restaurant = restaurant;
        setLunchMenu(Arrays.asList(dishes));
    }
}
