package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepositoryResponse(
    @JsonProperty("id") long id,
    @JsonProperty("updated_at") OffsetDateTime lastUpdate
) {
}
