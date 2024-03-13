package edu.java.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.net.URI;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    public Link(URI url) {
        this.url = url;
    }

    Long id;
    URI url;
    OffsetDateTime lastModifiedDate;
    OffsetDateTime lastCheckDate;
}
