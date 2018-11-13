package com.curry.security;

/**
 * Created by test on 2016-09-24.
 */

import com.curry.users.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserDetailsImpl extends org.springframework.security.core.userdetails.User {

    public UserDetailsImpl(User user) {
        super(user.getEmail(), user.getPassword(), authorities(user));
    }

    private static Collection<? extends GrantedAuthority> authorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList();

        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (user.isAdmin())
        {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return authorities;
    }
}