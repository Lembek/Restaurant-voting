package com.github.lembek.restaurant_voting.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"local_date", "user_id"},
        name = "vote_unique_local_date_user_idx")},
        indexes = {@Index(name = "vote_local_date_restaurant_id_idx", columnList = "local_date, restaurant_id")}
)
public class Vote extends BaseEntity {

    @NotNull
    @Column(name = "local_date", nullable = false, updatable = false, columnDefinition = "date default now()")
    private LocalDate localDate;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Vote(Integer id, LocalDate localDate, User user, Restaurant restaurant) {
        super(id);
        this.localDate = localDate;
        this.user = user;
        this.restaurant = restaurant;
    }

    public Vote(LocalDate localDate, User user, Restaurant restaurant) {
        this.localDate = localDate;
        this.user = user;
        this.restaurant = restaurant;
    }

    protected Vote() {
    }

    public @NotNull LocalDate getLocalDate() {
        return this.localDate;
    }

    public User getUser() {
        return this.user;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setLocalDate(@NotNull LocalDate localDate) {
        this.localDate = localDate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
