package com.github.lembek.RestaurantVoting.model;

import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.util.Assert;

import javax.persistence.*;

@MappedSuperclass
@Access(value = AccessType.FIELD)
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity implements Persistable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Override
    public boolean isNew() {
        return id == null;
    }

    public int id() {
        Assert.notNull(id, "Entity must have id");
        return id;
    }
}
