package com.robot.cairo.dto.response;

import com.robot.cairo.model.Users;
import lombok.Data;

import java.io.Serializable;

@Data
public class JwtResponse implements Serializable {

    private final Long id;
    private final String name;
    private final String email;
    private final String username;
    private final String token;

    public JwtResponse(Users user, String token) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.token = token;
    }
}
