package org.example.todorest.service;

import org.example.todorest.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface RegistrationService {
    public void register(RegisterRequest request);
}
