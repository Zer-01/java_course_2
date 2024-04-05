package edu.java.service.updates.updaters;

import edu.java.api.models.LinkUpdateRequest;
import edu.java.clients.stackoverflow.StackoverflowClient;
import edu.java.dto.QuestionResponse;
import edu.java.entity.Link;
import edu.java.exceptions.LinkUpdateException;
import edu.java.linkParser.links.ParseResult;
import edu.java.linkParser.links.StackOverflowParseResult;
import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StackOverflowLinkUpdater extends SiteLinkUpdater {
    private final static String NEW_UPDATE_MESSAGE = "В вопросе произошли изменения";

    private final StackoverflowClient stackoverflowClient;

    @Override
    public Optional<LinkUpdateRequest> getLinkUpdate(Link link, ParseResult parseResult) {
        if (parseResult instanceof StackOverflowParseResult) {
            Optional<QuestionResponse> response = stackoverflowClient.fetchLastActivity(
                Long.parseLong(((StackOverflowParseResult) parseResult).questionId())
            );

            if (response.isEmpty()) {
                throw new LinkUpdateException();
            }

            Optional<LinkUpdateRequest> result = checkLink(link, response.get());
            link.setLastCheckDate(OffsetDateTime.now());
            return result;
        }
        return Optional.empty();
    }

    private Optional<LinkUpdateRequest> checkLink(Link link, QuestionResponse response) {
        if (link.getLastModifiedDate().isBefore(response.items().getFirst().lastActivity())) {
            link.setLastModifiedDate(response.items().getFirst().lastActivity());

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
