package org.example.todorest.exception_handling;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException exception, HttpServletRequest request) {
        log.warn("Custom exception: {}", exception.getReason());

        ErrorResponse err = ErrorResponse.builder()
                .status(exception.getStatusCode().value())
                .error(HttpStatus.valueOf(exception.getStatusCode().value()).getReasonPhrase())
                .message(exception.getReason())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonError(HttpMessageNotReadableException exception, HttpServletRequest request) {
        log.warn("Bad JSON: {}", exception.getMessage(), exception);
        String fullMessage = exception.getMostSpecificCause().getMessage();
        String cleanMessage = fullMessage.contains("\n")
                ? fullMessage.substring(0, fullMessage.indexOf("\n"))
                : fullMessage;

        ErrorResponse err = ErrorResponse.builder()
                .status(400)
                .error("Bad Request")
                .message(cleanMessage)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        log.warn("ValidException {}", exception.getMessage(), exception);

        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        ErrorResponse err = ErrorResponse.builder()
                .status(400)
                .error("Bad Request")
                .message("Validation failed")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(err);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException exception, HttpServletRequest request) {
        log.warn("AccessDeniedException {}", exception.getMessage(), exception);

        ErrorResponse err = ErrorResponse.builder()
                .status(403)
                .error("Forbidden")
                .message("Access is denied")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception exception, HttpServletRequest request) {
        log.error("Unexpected error", exception);

        ErrorResponse err = ErrorResponse.builder()
                .status(500)
                .error("Internal Server Error")
                .message("Что-то пошло не так. Мы уже работаем над этим.")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(500).body(err);
    }

    //когда клиент использует неподдерживаемый HTTP-метод.
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        log.warn("Unsupported HTTP method: {}", exception.getMethod(), exception);

        ErrorResponse err = ErrorResponse.builder()
                .status(405)
                .error("Method Not Allowed")
                .message("HTTP метод не поддерживается для этого эндпоинта")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(err);
    }

    // Когда клиент отправляет тело запроса в формате, который ты не принимаешь
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception, HttpServletRequest request) {
        log.warn("Unsupported media type: {}", exception.getContentType(), exception);

        ErrorResponse err = ErrorResponse.builder()
                .status(415)
                .error("Unsupported Media Type")
                .message("Тип содержимого запроса не поддерживается. Используйте application/json.")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(err);
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException exception, HttpServletRequest request) {
        log.warn("Unknown endpoint: {}", request.getRequestURI(), exception);

        ErrorResponse err = ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .message("Эндпоинт не найден: " + request.getRequestURI())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

}
