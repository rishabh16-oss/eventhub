package com.rishabh.eventhub.config;

import com.rishabh.eventhub.entity.Role;
import com.rishabh.eventhub.entity.User;
import com.rishabh.eventhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@eventhub.com")) {
            userRepository.save(User.builder()
                    .name("Admin")
                    .email("admin@eventhub.com")
                    .password(passwordEncoder.encode("Admin@12345"))
                    .role(Role.ADMIN)
                    .build());
        }
    }
}