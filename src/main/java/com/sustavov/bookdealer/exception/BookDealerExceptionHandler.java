package com.sustavov.bookdealer.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class BookDealerExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage runtimeException(RuntimeException ex, WebRequest request) {
        return handleErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage authorNotFoundException(AuthorNotFoundException ex, WebRequest request) {
        return handleErrorMessage(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage bookNotFoundException(BookNotFoundException ex, WebRequest request) {
        return handleErrorMessage(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage userNotFoundException(UserNotFoundException ex, WebRequest request) {
        return handleErrorMessage(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessage userNotFoundException(AccessDeniedException ex, WebRequest request) {
        return handleErrorMessage(HttpStatus.FORBIDDEN, ex, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        return handleErrorMessage(HttpStatus.BAD_REQUEST, ex, request);
    }

    private ErrorMessage handleErrorMessage(HttpStatus status, Exception ex, WebRequest request) {
        return new ErrorMessage(
                status.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
    }
}
