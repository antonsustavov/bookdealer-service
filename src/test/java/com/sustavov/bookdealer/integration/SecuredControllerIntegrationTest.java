package com.sustavov.bookdealer.integration;

import com.sustavov.bookdealer.model.Role;
import com.sustavov.bookdealer.model.User;
import com.sustavov.bookdealer.model.request.AuthorRequest;
import com.sustavov.bookdealer.security.UserDetailsDefault;
import com.sustavov.bookdealer.security.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Client API - Security Integration Tests")
public class SecuredControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    public void givenAuthRequestOnSaveMethodUser_shouldFailedWith401() throws Exception {
        AuthorRequest authorRequest = createAuthorRequest();

        mvc.perform(post("/api/v1/authors")
                        .content(objectMapper.writeValueAsString(authorRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenAuthRequestOnSaveMethodAdmin_shouldSucceedWith201() throws Exception {
        AuthorRequest authorRequest = createAuthorRequest();
        User roleAdmin = setupUserRole(UserRole.ROLE_ADMIN, "admin", "admin@gmail.com", "password");
        UserDetailsDefault userDetails = setupUserDetails(roleAdmin.getUsername(), "password");

        mvc.perform(post("/api/v1/authors")
                        .with(user(userDetails))
                        .content(objectMapper.writeValueAsString(authorRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenAuthRequestOnSaveMethodUser_shouldFailedWith403() throws Exception {
        AuthorRequest authorRequest = createAuthorRequest();
        User roleUser = setupUserRole(UserRole.ROLE_USER, "user", "user@gmail.com", "password");
        UserDetailsDefault userDetails = setupUserDetails(roleUser.getUsername(), "password");

        mvc.perform(post("/api/v1/authors")
                        .with(user(userDetails))
                        .content(objectMapper.writeValueAsString(authorRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private AuthorRequest createAuthorRequest() {
        return AuthorRequest.builder()
                .firstName("adminUser")
                .lastName("adminUser")
                .dateOfBirth(LocalDate.of(2021, 3, 2))
                .build();
    }

    private User setupUserRole(UserRole userRole, String username, String email, String password) {
        Role role = Role.builder()
                .name(userRole)
                .build();
        Role savedRole = roleRepository.save(role);

        return userRepository.save(User.builder()
                .username(username)
                .email(email)
                .password(encoder.encode(password))
                .roles(Set.of(savedRole))
                .build());
    }

    private UserDetailsDefault setupUserDetails(String username, String password) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return (UserDetailsDefault) authenticate.getPrincipal();
    }
}
