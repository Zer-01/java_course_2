package edu.java.api.exceptions;

public class ChatAlreadyExistsException extends RuntimeException {
    public ChatAlreadyExistsException() {
        super();
    }

    public ChatAlreadyExistsException(String message) {
        super(message);
    }
}
