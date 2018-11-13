package com.curry.security;

/**
 * Created by test on 2016-09-24.
 */

import com.curry.users.User;
import com.curry.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =   this.userRepository.findByEmail(email);

        if (user == null)
        {
            throw new UsernameNotFoundException(email);
        }

//        if (user.getAuthMailkey() != null)
//        {
//            throw new UserNotAuthEmailException("[" + email + "] 해당 계정을 찾을 수 없습니다.");
//        }

        return new UserDetailsImpl(user);
    }
}