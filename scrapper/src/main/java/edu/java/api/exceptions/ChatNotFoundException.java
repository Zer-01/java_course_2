package edu.java.api.exceptions;

public class ChatNotFoundException extends RuntimeException{
    public ChatNotFoundException() {
        super();
    }

    public ChatNotFoundException(String message) {
        super(message);
    }
}
