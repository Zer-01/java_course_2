package edu.java.api.exceptions;

public class ChatAlreadyExistsException extends RuntimeException {
    public ChatAlreadyExistsException() {
    }

    public ChatAlreadyExistsException(String message) {
        super(message);
    }
}
