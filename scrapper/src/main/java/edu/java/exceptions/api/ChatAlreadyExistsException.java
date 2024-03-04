package edu.java.exceptions.api;

public class ChatAlreadyExistsException extends RuntimeException {
    public ChatAlreadyExistsException() {
    }

    public ChatAlreadyExistsException(String message) {
        super(message);
    }
}
