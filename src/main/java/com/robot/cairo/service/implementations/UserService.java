package com.robot.cairo.service.implementations;

import com.robot.cairo.dto.request.AuthRequest;
import com.robot.cairo.dto.request.UserDTO;
import com.robot.cairo.exceptions.EntityExistsException;
import com.robot.cairo.exceptions.EntityNotFoundException;
import com.robot.cairo.exceptions.UnAuthorizedException;
import com.robot.cairo.model.Users;
import com.robot.cairo.repository.UserRepository;
import com.robot.cairo.service.blueprints.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }

    @Override
    public Users authenticate(AuthRequest authRequest) {
        Users user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + authRequest.getUsername()));
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new UnAuthorizedException("Invalid credentials");
        }
        return user;
    }

    @Override
    public Users register(UserDTO userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new EntityExistsException("username", userRequest.getUsername());
        }
        Users user = new Users();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return userRepository.save(user);
    }
}
