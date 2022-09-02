package com.github.lembek.RestaurantVoting.model;


import lombok.*;
import org.springframework.util.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @OneToMany
    private List<Vote> votes;

    public Restaurant(Integer id, String name, Vote...votes) {
        super(id, name);
        setVotes(Arrays.asList(votes));
    }

    public void setVotes(Collection<Vote> votes) {
        this.votes = CollectionUtils.isEmpty(votes) ? Collections.emptyList() : List.copyOf(votes);
    }
}
