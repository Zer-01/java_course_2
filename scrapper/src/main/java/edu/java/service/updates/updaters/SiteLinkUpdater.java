package edu.java.service.updates.updaters;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.entity.Link;
import edu.java.exceptions.LinkUpdateException;
import edu.java.linkParser.links.ParseResult;
import java.util.Optional;

public abstract class SiteLinkUpdater {
    private SiteLinkUpdater nextLinkUpdater;

    public SiteLinkUpdater() {
    }

    public Optional<LinkUpdateRequest> getLinkUpdateOrDelegate(Link link, ParseResult parseResult) {
        Optional<LinkUpdateRequest> tmp;
        try {
            tmp = getLinkUpdate(link, parseResult);
        } catch (LinkUpdateException e) {
            return Optional.empty();
        }
        if (tmp.isPresent()) {
            return tmp;
        }

        if (nextLinkUpdater != null) {
            return nextLinkUpdater.getLinkUpdate(link, parseResult);
        } else {
            return Optional.empty();
        }
    }

    protected abstract Optional<LinkUpdateRequest> getLinkUpdate(Link link, ParseResult parseResult);

    public void setNext(SiteLinkUpdater nextUpdater) {
        this.nextLinkUpdater = nextUpdater;
    }
}
