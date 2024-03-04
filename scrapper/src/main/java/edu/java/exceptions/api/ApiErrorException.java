package edu.java.exceptions.api;

import edu.java.api.models.ApiErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ApiErrorException extends RuntimeException {
    private final ApiErrorResponse response;
}
