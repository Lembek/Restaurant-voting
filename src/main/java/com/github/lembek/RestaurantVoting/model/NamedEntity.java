package com.github.lembek.RestaurantVoting.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class NamedEntity extends BaseEntity {

    @NotBlank
    @Size(min = 4, max = 128)
    @Column(name = "name", nullable = false)
    protected String name;

    public NamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }
}
