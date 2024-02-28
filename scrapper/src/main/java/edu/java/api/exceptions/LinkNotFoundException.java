package edu.java.api.exceptions;

public class LinkNotFoundException extends RuntimeException{
    public LinkNotFoundException() {
        super();
    }

    public LinkNotFoundException(String message) {
        super(message);
    }
}
