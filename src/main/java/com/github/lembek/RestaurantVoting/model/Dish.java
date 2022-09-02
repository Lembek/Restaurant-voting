package com.github.lembek.RestaurantVoting.model;

import lombok.*;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(columnNames = {"name","price"}, name = "dish_unique_name_price_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends NamedEntity {

    @Min(value = 10)
    @NotNull
    @Column(name = "price", nullable = false)
    private int price;

    @ManyToMany(mappedBy = "lunchMenu")
    private List<Menu> menu;

    public Dish(Integer id, String name, int price, Menu...menu) {
        super(id, name);
        this.price = price;
        setMenu(Arrays.asList(menu));
    }

    public void setMenu(Collection<Menu> menu) {
        this.menu = CollectionUtils.isEmpty(menu) ? Collections.emptyList() : List.copyOf(menu);
    }
}
