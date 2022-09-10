package com.github.lembek.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users")
public class User extends NamedEntity {
    private static final int MAX_LENGTH_PASSWORD = 256;
    private static final int MAX_LENGTH_EMAIL = 128;

    @NotBlank
    @Size(max = MAX_LENGTH_PASSWORD)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    @Email
    @NotBlank
    @Size(max = MAX_LENGTH_EMAIL)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default true")
    private boolean enabled = true;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date registered = new Date();

    @Column(name = "role")
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(name = "user_role_unique_idx", columnNames = {"user_id", "role"}))
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id")
    @Fetch(FetchMode.JOIN)
    private Set<Role> roles;

    public User(Integer id, String name, String password, String email, Collection<Role> roles) {
        super(id, name);
        this.password = password;
        this.email = email;
        setRoles(roles);
    }

    public User(Integer id, String name, String password, String email, Role... roles) {
        this(id, name, password, email, Arrays.asList(roles));
    }

    public User(User user) {
        this(null, user.getName(), user.getPassword(), user.getEmail(), user.getRoles());
    }

    protected User() {
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    public @NotBlank @Size(max = MAX_LENGTH_PASSWORD) String getPassword() {
        return this.password;
    }

    public @Email @NotBlank @Size(max = MAX_LENGTH_EMAIL) String getEmail() {
        return this.email;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public @NotNull Date getRegistered() {
        return this.registered;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setPassword(@NotBlank @Size(max = MAX_LENGTH_PASSWORD) String password) {
        this.password = password;
    }

    public void setEmail(@Email @NotBlank @Size(max = MAX_LENGTH_EMAIL) String email) {
        this.email = email;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public void setRegistered(@NotNull Date registered) {
        this.registered = registered;
    }
}
