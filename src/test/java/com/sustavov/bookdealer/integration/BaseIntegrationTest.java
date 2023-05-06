package com.sustavov.bookdealer.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sustavov.bookdealer.repository.AuthorRepository;
import com.sustavov.bookdealer.repository.BookRepository;
import com.sustavov.bookdealer.repository.RoleRepository;
import com.sustavov.bookdealer.repository.UserRepository;
import com.sustavov.bookdealer.security.JwtUtils;
import com.sustavov.bookdealer.service.AuthorService;
import com.sustavov.bookdealer.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@Slf4j
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BaseIntegrationTest {
    @Autowired
    protected BookService bookService;
    @Autowired
    protected AuthorService authorService;
    @Autowired
    protected WebApplicationContext context;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected JwtUtils jwtUtils;
    @Autowired
    protected AuthenticationManager authenticationManager;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    protected PasswordEncoder encoder;
    @Autowired
    protected SessionFactory sessionFactory;
    @Autowired
    protected CacheManager cacheManager;
    @Autowired
    protected AuthorRepository authorRepository;
    @Autowired
    protected BookRepository bookRepository;

    protected MockMvc mvc;

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation) {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void contextLoads() {
        assertThat(bookService).isNotNull();
        assertThat(authorService).isNotNull();
        assertThat(mvc).isNotNull();
        assertThat(context).isNotNull();
        assertThat(authenticationManager).isNotNull();
        assertThat(cacheManager).isNotNull();
    }

}

