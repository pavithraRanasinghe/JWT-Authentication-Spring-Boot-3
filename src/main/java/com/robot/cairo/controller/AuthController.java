package com.robot.cairo.controller;

import com.robot.cairo.dto.request.AuthRequest;
import com.robot.cairo.dto.request.RefreshTokenRequest;
import com.robot.cairo.dto.request.UserDTO;
import com.robot.cairo.dto.response.JwtResponse;
import com.robot.cairo.dto.response.UserResponse;
import com.robot.cairo.exceptions.UnAuthorizedException;
import com.robot.cairo.model.Users;
import com.robot.cairo.security.JwtTokenUtil;
import com.robot.cairo.security.JwtUserDetailsService;
import com.robot.cairo.service.blueprints.IUserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Authentication endpoints — all paths under {@code /v1/auth/**} are public
 * (permitted without a JWT in {@link com.robot.cairo.config.WebSecurityConfig}).
 */
@Slf4j
@RestController
@RequestMapping("v1/auth")
public class AuthController {

    private final IUserService userService;
    private final JwtUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(IUserService userService,
                          JwtUserDetailsService userDetailsService,
                          JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Authenticate a user and return a short-lived access token + a refresh token.
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid AuthRequest authRequest) {
        Users existingUser = userService.authenticate(authRequest);
        UserDetails userDetails = userDetailsService.loadUserByUsername(existingUser.getUsername());
        String accessToken  = jwtTokenUtil.generateAccessToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(existingUser, accessToken, refreshToken));
    }

    /**
     * Register a new user. Returns the saved user without the password hash.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserDTO userRequest) {
        Users savedUser = userService.register(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(savedUser));
    }

    /**
     * Exchange a valid refresh token for a new access token.
     */
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        try {
            if (!jwtTokenUtil.isRefreshToken(refreshToken)) {
                throw new UnAuthorizedException("Provided token is not a refresh token");
            }
            String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!jwtTokenUtil.validateToken(refreshToken, userDetails)) {
                throw new UnAuthorizedException("Refresh token is invalid or expired");
            }
            Users user = userService.findUserByUsername(username);
            String newAccessToken  = jwtTokenUtil.generateAccessToken(userDetails);
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(user, newAccessToken, newRefreshToken));
        } catch (ExpiredJwtException ex) {
            throw new UnAuthorizedException("Refresh token has expired — please log in again");
        } catch (JwtException ex) {
            throw new UnAuthorizedException("Invalid refresh token");
        }
    }

    /**
     * Simple endpoint to verify that the caller's JWT is still valid.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Principal principal) {
        log.debug("Authenticated user: {}", principal.getName());
        Users user = userService.findUserByUsername(principal.getName());
        return ResponseEntity.ok(new UserResponse(user));
    }
}
