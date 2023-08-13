package com.robot.cairo.service.implementations;


import com.robot.cairo.dto.request.AuthRequest;
import com.robot.cairo.dto.request.UserDTO;
import com.robot.cairo.exceptions.EntityExistsException;
import com.robot.cairo.exceptions.EntityNotFoundException;
import com.robot.cairo.exceptions.UnAuthorizedException;
import com.robot.cairo.model.Users;
import com.robot.cairo.repository.UserRepository;
import com.robot.cairo.service.bluprints.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public Users findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    @Override
    public Users authenticate(AuthRequest authRequest) {
        Users existUser = this.userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(()-> new EntityNotFoundException(authRequest.getUsername()));
        if (passwordEncoder.matches(authRequest.getPassword(), existUser.getPassword())){
            return existUser;
        }else {
            throw new UnAuthorizedException("Password doesn't match for user");
        }
    }

    @Override
    public Users register(UserDTO userRequest) {
        Optional<Users> existingUser = userRepository.findByUsername(userRequest.getUsername());
        if (existingUser.isPresent())
            throw new EntityExistsException("username", userRequest.getUsername());
        Users user = new Users();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));


        return userRepository.save(user);
    }
}
