package com.rishabh.eventhub.service;

import com.rishabh.eventhub.dto.RegisterRequest;
import com.rishabh.eventhub.dto.UserResponse;
import com.rishabh.eventhub.entity.Role;
import com.rishabh.eventhub.entity.User;
import com.rishabh.eventhub.exception.DuplicateResourceException;
import com.rishabh.eventhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already registered");
        }

        User user = User.builder()
                .name(request.name())
                .email(email)
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER) // never taken from the client
                .build();

        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getName(), saved.getEmail(), saved.getRole());
    }
}