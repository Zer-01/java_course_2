package edu.java.entity;

import java.net.URI;
import java.time.OffsetDateTime;

public record Link(Long id, URI url, OffsetDateTime lastModifiedDate) {
}
