package edu.java.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    List<String> stacktrace
) {
    @Override
    public String toString() {
        return description + '\n'
            + code + '\n'
            + exceptionName + '\n'
            + exceptionMessage + '\n';
    }
}
