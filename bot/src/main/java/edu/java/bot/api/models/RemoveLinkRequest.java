package edu.java.bot.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.net.URI;

public record RemoveLinkRequest(
    @NotBlank @JsonProperty("link") URI link
) {
}
