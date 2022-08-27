package com.github.lembek.RestaurantVoting.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dish")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends NamedEntity {

    @Min(value = 10)
    @NotNull
    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
}
