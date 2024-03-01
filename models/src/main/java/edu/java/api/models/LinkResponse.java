package edu.java.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record LinkResponse(
    @NotNull @JsonProperty("id") Long id,
    @NotBlank @JsonProperty("url") URI url
) {
}
