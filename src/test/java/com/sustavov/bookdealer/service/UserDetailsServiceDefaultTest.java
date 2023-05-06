package com.sustavov.bookdealer.service;

import com.sustavov.bookdealer.exception.UserNotFoundException;
import com.sustavov.bookdealer.model.User;
import com.sustavov.bookdealer.repository.UserRepository;
import com.sustavov.bookdealer.security.UserDetailsDefault;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceDefaultTest {
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserDetailsServiceDefault userDetailsServiceDefault;

    @Test
    void loadUserByUsername() {
        var user = User.builder()
                .roles(Set.of())
                .build();
        var expected = UserDetailsDefault.build(user);

        when(repository.findByUsername("username")).thenReturn(Optional.of(user));

        var actual = userDetailsServiceDefault.loadUserByUsername("username");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void loadUserByUsernameThrowException() {
        when(repository.findByUsername("username")).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> {
            userDetailsServiceDefault.loadUserByUsername("username");
        });
    }
}