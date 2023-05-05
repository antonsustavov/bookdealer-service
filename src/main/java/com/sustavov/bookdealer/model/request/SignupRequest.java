package com.sustavov.bookdealer.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignupRequest {
    String username;
    String email;
    String password;
    Set<String> role;
}
