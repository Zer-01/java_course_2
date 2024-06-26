package edu.java.api.handlers;

import edu.java.api.models.ApiErrorResponse;
import edu.java.exceptions.api.ChatAlreadyExistsException;
import edu.java.exceptions.api.ChatNotFoundException;
import edu.java.exceptions.api.InvalidLinkException;
import edu.java.exceptions.api.LinkAlreadyTrackingException;
import edu.java.exceptions.api.LinkNotFoundException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ScrapperApiApplicationHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse illegalRequest(MethodArgumentNotValidException e) {
        return new ApiErrorResponse(
            "Illegal arguments",
            e.getStatusCode().toString(),
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toList()
        );
    }

    @ExceptionHandler(value = InvalidLinkException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse invalidLink(InvalidLinkException e) {
        return new ApiErrorResponse(
            "Invalid link",
            HttpStatus.BAD_REQUEST.toString(),
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toList()
        );
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse messageNotReadable(HttpMessageNotReadableException e) {
        return new ApiErrorResponse(
            "Reading request error",
            HttpStatus.BAD_REQUEST.toString(),
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toList()
        );
    }

    @ExceptionHandler(value = LinkNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiErrorResponse linkNotFound(LinkNotFoundException e) {
        return new ApiErrorResponse(
            "Link not found",
            HttpStatus.NOT_FOUND.toString(),
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toList()
        );
    }

    @ExceptionHandler(value = ChatNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiErrorResponse chatNotFound(ChatNotFoundException e) {
        return new ApiErrorResponse(
            "Chat not found",
            HttpStatus.NOT_FOUND.toString(),
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toList()
        );
    }

    @ExceptionHandler(value = LinkAlreadyTrackingException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ApiErrorResponse linkAlreadyExist(LinkAlreadyTrackingException e) {
        return new ApiErrorResponse(
            "Link already tracking",
            HttpStatus.CONFLICT.toString(),
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toList()
        );
    }

    @ExceptionHandler(value = ChatAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ApiErrorResponse chatAlreadyExist(ChatAlreadyExistsException e) {
        return new ApiErrorResponse(
            "Chat already exists",
            HttpStatus.CONFLICT.toString(),
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toList()
        );
    }
}
