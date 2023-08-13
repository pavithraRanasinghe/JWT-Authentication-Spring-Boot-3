package com.robot.cairo.controller;

import com.robot.cairo.dto.request.AuthRequest;
import com.robot.cairo.dto.request.UserDTO;
import com.robot.cairo.dto.response.JwtResponse;
import com.robot.cairo.model.Users;
import com.robot.cairo.security.JwtTokenUtil;
import com.robot.cairo.security.JwtUserDetailsService;
import com.robot.cairo.service.bluprints.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/auth")
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated AuthRequest authRequest) {
        Users existingUser = userService.authenticate(authRequest);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(existingUser.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(existingUser, token));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Validated UserDTO userRequest) {
        Users existingUser = userService.register(userRequest);
        return ResponseEntity.ok(existingUser);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<?> authenticate(Principal principal){
        String name = principal.getName();
        log.info("Authenticate User : "+ name);
        return ResponseEntity.ok().build();
    }
}
