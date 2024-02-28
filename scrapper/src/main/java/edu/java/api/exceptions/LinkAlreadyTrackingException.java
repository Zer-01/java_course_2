package edu.java.api.exceptions;

public class LinkAlreadyTrackingException extends RuntimeException {
    public LinkAlreadyTrackingException() {
        super();
    }

    public LinkAlreadyTrackingException(String message) {
        super(message);
    }
}
