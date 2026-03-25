package com.robot.cairo.dto.response;

import com.robot.cairo.model.Users;
import lombok.Getter;

import java.io.Serializable;

/**
 * Safe user response DTO — never exposes the password hash.
 */
@Getter
public class UserResponse implements Serializable {

    private final Long id;
    private final String name;
    private final String email;
    private final String username;

    public UserResponse(Users user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.username = user.getUsername();
    }
}
