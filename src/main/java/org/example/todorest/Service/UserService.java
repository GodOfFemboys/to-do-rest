package org.example.todorest.Service;

import org.example.todorest.Entity.User;
import org.example.todorest.Repository.UserRepository;
import org.example.todorest.Security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByName(username)
                .orElseThrow( () -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }

}
