package com.curry.users;

import org.springframework.security.core.AuthenticationException;

public class UserNotAuthEmailException extends AuthenticationException {
    String email;

    public UserNotAuthEmailException(String email) {
        super(email);
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
