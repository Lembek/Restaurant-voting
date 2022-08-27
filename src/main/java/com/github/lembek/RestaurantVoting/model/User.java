package com.github.lembek.RestaurantVoting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends NamedEntity {

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "role")
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
    uniqueConstraints = @UniqueConstraint(name = "user_role_unique_idx" , columnNames = {"user_id", "role"}))
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToMany
    private List<Vote> votes;
}
