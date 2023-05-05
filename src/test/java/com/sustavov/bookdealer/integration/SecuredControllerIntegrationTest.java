package com.sustavov.bookdealer.integration;

import com.sustavov.bookdealer.model.Role;
import com.sustavov.bookdealer.model.User;
import com.sustavov.bookdealer.model.request.AuthorRequest;
import com.sustavov.bookdealer.security.UserDetailsDefault;
import com.sustavov.bookdealer.security.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecuredControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    @WithMockUser("user")
    public void givenAuthRequestOnSaveMethodUser_shouldFailedWith500() throws Exception {

        AuthorRequest authorRequest = createAuthorRequest();

        mvc.perform(post("/api/v1/authors")
                        .content(objectMapper.writeValueAsString(authorRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser("admin")
    public void givenAuthRequestOnSaveMethodAdmin_shouldSucceedWith201() throws Exception {

        AuthorRequest authorRequest = createAuthorRequest();

        Role role = Role.builder()
                .name(UserRole.ROLE_ADMIN)
                .build();
        Role savedRole = roleRepository.save(role);

        User savedUser = userRepository.save(User.builder()
                .username("username")
                .email("username@gm.com")
                .password(encoder.encode("password"))
                .roles(Set.of(savedRole))
                .build());

        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken("username", "password"));
        UserDetailsDefault userDetails = (UserDetailsDefault) authenticate.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        mvc.perform(post("/api/v1/authors")
                        .with(user(userDetails))
                        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                        .content(objectMapper.writeValueAsString(authorRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser("user")
    public void givenAuthRequestOnSaveMethodUser_shouldFailedWith403() throws Exception {
        AuthorRequest authorRequest = createAuthorRequest();

        Role role = Role.builder()
                .name(UserRole.ROLE_USER)
                .build();
        Role savedRole = roleRepository.save(role);

        User savedUser = userRepository.save(User.builder()
                .username("username_3")
                .email("username_3@gm.com")
                .password(encoder.encode("password"))
                .roles(Set.of(savedRole))
                .build());

        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken("username_3", "password"));
        UserDetailsDefault userDetails = (UserDetailsDefault) authenticate.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        mvc.perform(post("/api/v1/authors")
                        .with(user(userDetails))
                        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                        .content(objectMapper.writeValueAsString(authorRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private AuthorRequest createAuthorRequest() {
        AuthorRequest authorRequest = new AuthorRequest();
        authorRequest.setFirstName("adminUser");
        authorRequest.setLastName("adminUser");
        authorRequest.setDateOfBirth(LocalDate.of(2021, 3, 2));

        return authorRequest;
    }
}
