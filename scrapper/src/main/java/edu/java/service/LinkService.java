package edu.java.service;

import edu.java.entity.Link;
import java.net.URI;
import java.util.List;

public interface LinkService {
    Link add(long chatId, URI url);

    Link remove(long chatId, URI url);

    List<Link> listAll(long chatId);
}
