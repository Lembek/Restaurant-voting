package com.github.lembek.RestaurantVoting.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@MappedSuperclass
public abstract class NamedEntity extends BaseEntity {

    @NotBlank
    @Size(min = 4, max = 128)
    @Column(name = "name", nullable = false)
    protected String name;

    public NamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    protected NamedEntity() {
    }

    @Override
    public String toString() {
        return super.toString() + '[' + name + ']';
    }

    public @NotBlank @Size(min = 4, max = 128) String getName() {
        return this.name;
    }

    public void setName(@NotBlank @Size(min = 4, max = 128) String name) {
        this.name = name;
    }
}
