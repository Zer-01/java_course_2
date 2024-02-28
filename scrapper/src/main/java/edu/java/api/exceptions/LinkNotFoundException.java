package edu.java.api.exceptions;

public class LinkNotFoundException extends RuntimeException {
    public LinkNotFoundException() {
    }

    public LinkNotFoundException(String message) {
        super(message);
    }
}
