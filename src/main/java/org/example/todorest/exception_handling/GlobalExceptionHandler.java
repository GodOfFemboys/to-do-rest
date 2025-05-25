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

        ErrorResponse err = new ErrorResponse(
                exception.getStatusCode().value(),
                HttpStatus.valueOf(exception.getStatusCode().value()).getReasonPhrase(), // error
                exception.getReason(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
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
        ErrorResponse err = new ErrorResponse(
                400,
                "Bad Request",
                cleanMessage,
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        log.warn("ValidException {}", exception.getMessage(), exception);
        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        ErrorResponse err = new ErrorResponse(
                400,
                "Bad Request",
                "Validation failed",
                request.getRequestURI(),
                LocalDateTime.now(),
                errors
        );
        return ResponseEntity.badRequest().body(err);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException exception, HttpServletRequest request) {
        log.warn("AccessDeniedException {}", exception.getMessage(), exception);
        ErrorResponse err = new ErrorResponse(
                403,
                "Forbidden",
                "Access is denied",
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception exception, HttpServletRequest request) {
        log.error("Unexpected error", exception);
        ErrorResponse err = new ErrorResponse(
                500,
                "Internal Server Error",
                "Что-то пошло не так. Мы уже работаем над этим.",
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(500).body(err);
    }

//    // Доп., потом прочитать че и за чем
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
//        List<String> details = ex.getConstraintViolations().stream()
//                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
//                .toList();
//
//        ErrorResponse err = new ErrorResponse(
//                400,
//                "Bad Request",
//                "Validation failed",
//                request.getRequestURI(),
//                LocalDateTime.now(),
//                details
//        );
//        return ResponseEntity.badRequest().body(err);
//    }
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
//        log.warn("Entity not found: {}", ex.getMessage(), ex);
//        ErrorResponse err = new ErrorResponse(
//                404,
//                "Not Found",
//                ex.getMessage(),
//                request.getRequestURI(),
//                LocalDateTime.now()
//        );
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
//    }


    //когда клиент использует неподдерживаемый HTTP-метод.
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        log.warn("Unsupported HTTP method: {}", exception.getMethod(), exception);

        ErrorResponse err = new ErrorResponse(
                405,
                "Method Not Allowed",
                "HTTP метод не поддерживается для этого эндпоинта",
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(err);
    }

    // Когда клиент отправляет тело запроса в формате, который ты не принимаешь
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception, HttpServletRequest request) {
        log.warn("Unsupported media type: {}", exception.getContentType(), exception);

        ErrorResponse err = new ErrorResponse(
                415,
                "Unsupported Media Type",
                "Тип содержимого запроса не поддерживается. Используйте application/json.",
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(err);
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException exception, HttpServletRequest request) {
        log.warn("Unknown endpoint: {}", request.getRequestURI(), exception);

        ErrorResponse err = new ErrorResponse(
                404,
                "Not Found",
                "Эндпоинт не найден: " + request.getRequestURI(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

}
