package com.bix.imageprocessor.domain.user.model;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    void shouldReturnRoleNamesSeparatedBySpace() {

        var adminRole = new Role(1L, "ADMIN");
        var userRole = new Role(2L, "USER");

        var roles = new LinkedHashSet<>(List.of(adminRole, userRole));

        var user = new User();
        user.setRoles(roles);

        var roleNames = user.getRoleNames();

        assertThat(roleNames).isEqualTo("ADMIN USER");
    }
}
