package com.robot.cairo.service.blueprints;

import com.robot.cairo.dto.request.AuthRequest;
import com.robot.cairo.dto.request.UserDTO;
import com.robot.cairo.model.Users;

public interface IUserService {

    /** Returns the user or throws {@code EntityNotFoundException}. Never returns null. */
    Users findUserByUsername(String username);

    Users authenticate(AuthRequest authRequest);

    Users register(UserDTO userRequest);
}
