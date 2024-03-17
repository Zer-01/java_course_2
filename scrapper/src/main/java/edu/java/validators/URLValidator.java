package edu.java.validators;

import java.net.URI;

public class URLValidator {
    private final static String URL_PATTERN = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}"
        + "\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$";

    private URLValidator() {
    }

    public static boolean isValidLink(URI url) {
        return url.toString().matches(URL_PATTERN);
    }
}
