package edu.java.service.updates;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.entity.Link;
import java.util.List;

public interface Updater {
    List<LinkUpdateRequest> getLinkUpdates(List<Link> links);
}
