package edu.java.service.updates;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.entity.Link;
import edu.java.linkParser.Parser;
import edu.java.linkParser.links.ParseResult;
import edu.java.service.updates.updaters.SiteLinkUpdater;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LinksUpdater implements Updater {
    private final Parser parser;
    private final SiteLinkUpdater updater;

    @Autowired
    public LinksUpdater(List<SiteLinkUpdater> updaters, Parser parser) {
        if (updaters.isEmpty()) {
            throw new RuntimeException("No updaters created");
        }

        updater = updaters.getFirst();
        for (int i = 1; i < updaters.size(); i++) {
            updaters.get(i - 1).setNext(updaters.get(i));
        }
        this.parser = parser;
    }

    @Override
    public List<LinkUpdateRequest> getLinkUpdates(List<Link> links) {
        List<LinkUpdateRequest> requests = new ArrayList<>();
        for (Link link : links) {
            Optional<ParseResult> parseResult = parser.parse(link.getUrl());
            if (parseResult.isEmpty()) {
                continue;
            }
            Optional<LinkUpdateRequest> updateResult = updater.getLinkUpdate(link, parseResult.get());
            updateResult.ifPresent(requests::add);
        }
        return requests;
    }
}
