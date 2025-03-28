package com.bix.imageprocessor.domain.user.model;

import com.bix.imageprocessor.domain.subscription.model.Subscription;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.stream.Collectors.joining;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Subscription subscription;

    @ManyToMany(fetch = EAGER, cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinTable(name = "user_roles")
    private Set<Role> roles;

    public String getRoleNames() {
        return roles.stream().map(Role::getName).collect(joining(" "));
    }
}