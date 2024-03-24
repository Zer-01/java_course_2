package edu.java.domain.jpa;

import jakarta.persistence.AttributeConverter;
import java.net.URI;

public class URIConverter implements AttributeConverter<URI, String> {
    @Override
    public String convertToDatabaseColumn(URI uri) {
        return (uri == null) ? null : uri.toString();
    }

    @Override
    public URI convertToEntityAttribute(String s) {
        return (s == null) ? null : URI.create(s);
    }
}
