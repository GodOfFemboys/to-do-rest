package org.example.todorest.exception_handling;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final LocalDateTime timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors; // для валидационных ошибок, может быть null
}
