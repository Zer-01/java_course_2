package edu.java.service;

import edu.java.api.models.LinkResponse;
import java.net.URI;
import java.util.List;

public interface UserService {
    void newChat(long id);

    void deleteChat(long id);

    List<URI> getAllLinks(long chatId);

    LinkResponse addLink(long chatId, URI link);

    LinkResponse deleteLink(long chatId, URI link);
}
