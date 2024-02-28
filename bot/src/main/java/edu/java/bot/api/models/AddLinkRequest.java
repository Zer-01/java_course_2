package edu.java.bot.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record AddLinkRequest(
    @NotNull @JsonProperty("link") URI link
) {
}
