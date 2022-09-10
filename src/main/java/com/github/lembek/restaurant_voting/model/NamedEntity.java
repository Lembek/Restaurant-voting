package com.github.lembek.restaurant_voting.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@MappedSuperclass
public abstract class NamedEntity extends BaseEntity {
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 128;

    @NotBlank
    @Size(min = MIN_LENGTH, max = MAX_LENGTH)
    @Column(name = "name", nullable = false)
    private String name;

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

    public @NotBlank @Size(min = MIN_LENGTH, max = MAX_LENGTH) String getName() {
        return this.name;
    }

    public void setName(@NotBlank @Size(min = MIN_LENGTH, max = MAX_LENGTH) String name) {
        this.name = name;
    }
}
