package org.example.todorest.service;

import lombok.RequiredArgsConstructor;
import org.example.todorest.dto.RegisterRequest;
import org.example.todorest.entity.RoleType;
import org.example.todorest.entity.User;
import org.example.todorest.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.findUserByName(request.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoleTypeSet(Set.of(RoleType.ROLE_USER));

        userRepository.save(user);
    }
}
