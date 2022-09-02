package com.github.lembek.RestaurantVoting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends NamedEntity {

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @Email
    @NotBlank
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "role")
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
    uniqueConstraints = @UniqueConstraint(name = "user_role_unique_idx" , columnNames = {"user_id", "role"}))
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToMany
    private List<Vote> votes;

    public User(Integer id, String name, String password, String email, Collection<Role> roles, List<Vote> votes) {
        super(id, name);
        this.password = password;
        this.email = email;
        this.votes = votes;
        setRoles(roles);
    }

    public User(Integer id, String name, String password, String email, Role...roles) {
        this(id,name,password,email, Arrays.asList(roles),null);
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }
}
