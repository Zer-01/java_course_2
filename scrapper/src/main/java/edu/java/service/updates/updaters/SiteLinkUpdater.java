package edu.java.service.updates.updaters;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.entity.Link;
import edu.java.linkParser.links.ParseResult;
import java.util.Optional;

public abstract class SiteLinkUpdater {
    private SiteLinkUpdater nextLinkUpdater;

    public SiteLinkUpdater() {
    }

    public abstract Optional<LinkUpdateRequest> getLinkUpdate(Link link, ParseResult parseResult);

    protected SiteLinkUpdater getNext() {
        return nextLinkUpdater;
    }

    public void setNext(SiteLinkUpdater nextUpdater) {
        this.nextLinkUpdater = nextUpdater;
    }
}
