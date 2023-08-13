package com.robot.cairo.service.bluprints;


import com.robot.cairo.dto.request.AuthRequest;
import com.robot.cairo.dto.request.UserDTO;
import com.robot.cairo.model.Users;

public interface IUserService {

    Users findUserByUsername(String username);

    Users authenticate(AuthRequest authRequest);

    Users register(UserDTO userRequest);

}
