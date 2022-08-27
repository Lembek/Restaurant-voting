package com.github.lembek.RestaurantVoting.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToMany
    private List<Dish> lunchMenu;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
