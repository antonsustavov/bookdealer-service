package com.sustavov.bookdealer.service;

import com.sustavov.bookdealer.exception.UserNotFoundException;
import com.sustavov.bookdealer.model.User;
import com.sustavov.bookdealer.repository.UserRepository;
import com.sustavov.bookdealer.security.UserDetailsDefault;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceDefault implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User Not Found with username: " + username));

        return UserDetailsDefault.build(user);
    }
}
