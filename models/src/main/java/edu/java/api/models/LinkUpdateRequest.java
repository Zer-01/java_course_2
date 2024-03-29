package edu.java.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(
    @NotNull Long id,
    @NotNull URI url,
    @NotBlank String description,
    @NotEmpty @JsonProperty("tgChatIds") List<Long> chatIds
) {
}
