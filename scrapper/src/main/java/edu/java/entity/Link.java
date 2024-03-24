package edu.java.entity;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import edu.java.domain.jpa.URIConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "link")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url", unique = true, nullable = false)
    @Convert(converter = URIConverter.class)
    private URI url;
    @Column(name = "last_modified_date")
    private OffsetDateTime lastModifiedDate;
    @Column(name = "last_check_date")
    private OffsetDateTime lastCheckDate;
    @ManyToMany(mappedBy = "links")
    private Set<Chat> chats = new HashSet<>();

    public Link(URI url) {
        this.url = url;
    }

    public Link(Long id, URI url, OffsetDateTime lastModifiedDate, OffsetDateTime lastCheckDate) {
        this.id = id;
        this.url = url;
        this.lastModifiedDate = lastModifiedDate;
        this.lastCheckDate = lastCheckDate;
    }

    public void addChat(Chat chat) {
        chats.add(chat);
        chat.getLinks().add(this);
    }

    public void removeChat(Chat chat) {
        chats.remove(chat);
        chat.getLinks().remove(this);
    }
}
