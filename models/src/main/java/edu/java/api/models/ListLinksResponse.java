package edu.java.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ListLinksResponse(
    @NotNull @JsonProperty("links") List<LinkResponse> links,
    @NotNull @JsonProperty("size") Integer size
) {
}
