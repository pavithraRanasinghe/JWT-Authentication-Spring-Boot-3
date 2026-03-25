package com.robot.cairo.security;

import com.robot.cairo.model.Users;
import com.robot.cairo.service.blueprints.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Loads user credentials from the database for Spring Security.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Users registeredUser = userService.findUserByUsername(username);
            // Default role assigned to every registered user.
            // Extend this when role-based access control (RBAC) is needed.
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            return new User(registeredUser.getUsername(), registeredUser.getPassword(), authorities);
        } catch (Exception ex) {
            log.error("User not found for username '{}': {}", username, ex.getMessage());
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
