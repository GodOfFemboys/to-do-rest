package org.example.todorest;

import org.example.todorest.entity.RoleType;
import org.example.todorest.entity.User;
import org.example.todorest.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User user1 = new User(
                    Set.of(RoleType.ROLE_USER),
                    passwordEncoder.encode("password1"),
                    "igor"
            );

            User user2 = new User(
                    Set.of(RoleType.ROLE_USER),
                    passwordEncoder.encode("password2"),
                    "misha"
            );

            User admin = new User(
                    Set.of(RoleType.ROLE_ADMIN),
                    passwordEncoder.encode("adminpass"),
                    "admin"
            );

            userRepository.saveAll(List.of(user1, user2, admin));
            System.out.println(">>> Users initialized");
        }
    }
}
