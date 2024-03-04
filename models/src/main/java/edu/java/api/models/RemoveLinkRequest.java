package edu.java.api.models;

import jakarta.validation.constraints.NotBlank;
import java.net.URI;

public record RemoveLinkRequest(
    @NotBlank URI link
) {
}
