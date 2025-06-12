package com.station.admin.security;

import com.station.admin.user.UserRepository;
import com.station.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userByEmail = userRepository.getUserByEmail(email);
        if (userByEmail != null) {
            return new CustomUserDetails(userByEmail);
        }
        throw new UsernameNotFoundException("User 404 with email: " + email);
    }
}
