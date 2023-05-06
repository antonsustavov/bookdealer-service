package com.sustavov.bookdealer.jpa;

import com.sustavov.bookdealer.model.Role;
import com.sustavov.bookdealer.model.User;
import com.sustavov.bookdealer.repository.RoleRepository;
import com.sustavov.bookdealer.repository.UserRepository;
import com.sustavov.bookdealer.security.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("User/Roles - Data JPA Tests")
public class UserJpaTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Test
    public void shouldCreateUserWithRoles() {
        var user = createUser();
        var savedUser = userRepository.save(user);

        var optionalUserById = userRepository.findById(savedUser.getId());

        assertThat(optionalUserById).isPresent().hasValue(savedUser);
        assertThat(optionalUserById.get().getRoles().size()).isEqualTo(2);

        var roles = roleRepository.findAll();
        assertThat(roles.size()).isEqualTo(2);
    }

    private User createUser() {
        return User.builder()
                .username("user")
                .email("user@gmail.com")
                .password("password")
                .roles(Set.of(Role.builder()
                                .name(UserRole.ROLE_USER)
                                .build(),
                        Role.builder()
                                .name(UserRole.ROLE_ADMIN)
                                .build()))
                .build();
    }
}
