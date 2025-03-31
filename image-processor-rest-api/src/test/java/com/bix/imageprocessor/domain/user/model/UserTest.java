package com.bix.imageprocessor.domain.user.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    void shouldReturnRoleNamesSeparatedBySpace() {

        var adminRole = new Role(1L, "ADMIN");
        var userRole = new Role(2L, "USER");

        var user = new User();
        user.setRoles(Set.of(adminRole, userRole));

        var roleNames = user.getRoleNames();

        assertThat(roleNames).isEqualTo("ADMIN USER");
    }
}
