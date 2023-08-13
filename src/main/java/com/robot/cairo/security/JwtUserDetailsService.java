package com.robot.cairo.security;

import com.robot.cairo.service.bluprints.IUserService;
import lombok.extern.slf4j.Slf4j;
import com.robot.cairo.model.Users;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * The type Jwt user details service.
 */
@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final IUserService userService;

    /**
     * Instantiates a new Jwt user details service.
     *
     * @param userService the user service
     */
    public JwtUserDetailsService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Users registeredUser = userService.findUserByUsername(username);
            return new User(registeredUser.getUsername(), registeredUser.getPassword(), Collections.emptyList());
        } catch (Exception ex) {
            log.error("Request format error", ex);
            throw new UsernameNotFoundException("Request format Error");
        }
    }

}
