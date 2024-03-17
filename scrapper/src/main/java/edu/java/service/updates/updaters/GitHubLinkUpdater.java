package edu.java.service.updates.updaters;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.clients.github.GitHubClient;
import edu.java.dto.RepositoryResponse;
import edu.java.entity.Link;
import edu.java.linkParser.links.GitHubParseResult;
import edu.java.linkParser.links.ParseResult;
import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GitHubLinkUpdater extends SiteLinkUpdater {
    private final static String NEW_UPDATE_MESSAGE = "В репозитории произошли изменения";

    private final GitHubClient gitHubClient;

    @Override
    public Optional<LinkUpdateRequest> getLinkUpdate(Link link, ParseResult parseResult) {
        if (parseResult instanceof GitHubParseResult) {
            Optional<RepositoryResponse> response = gitHubClient.fetchLastActivity(
                ((GitHubParseResult) parseResult).owner(),
                ((GitHubParseResult) parseResult).repo()
            );

            if (response.isEmpty()) {
                return Optional.empty();
            }

            Optional<LinkUpdateRequest> result = checkLink(link, response.get());
            link.setLastCheckDate(OffsetDateTime.now());
            return result;
        }

        if (getNext() != null) {
            return getNext().getLinkUpdate(link, parseResult);
        } else {
            return Optional.empty();
        }
    }

    private Optional<LinkUpdateRequest> checkLink(Link link, RepositoryResponse response) {
        if (link.getLastModifiedDate().isBefore(response.lastUpdate())) {
            link.setLastModifiedDate(response.lastUpdate());

            return Optional.of(new LinkUpdateRequest(
                link.getId(),
                link.getUrl(),
                NEW_UPDATE_MESSAGE,
                null
            ));
        } else {
            return Optional.empty();
        }
    }
}
