package edu.java.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat")
public class Chat {
    @Id
    private Long id;
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
    @ManyToMany
    @JoinTable(name = "chat_link",
               joinColumns = @JoinColumn(name = "chat_id"),
               inverseJoinColumns = @JoinColumn(name = "link_id"))
    private Set<Link> links = new HashSet<>();

    public Chat(Long id, OffsetDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public void addLink(Link link) {
        links.add(link);
        link.getChats().add(this);
    }

    public void removeLink(Link link) {
        links.remove(link);
        link.getChats().remove(this);
    }
}
