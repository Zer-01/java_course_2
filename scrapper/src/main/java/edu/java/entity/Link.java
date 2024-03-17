package edu.java.entity;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
