package edu.java.api.controllers;

import edu.java.api.models.AddLinkRequest;
import edu.java.api.models.LinkResponse;
import edu.java.api.models.ListLinksResponse;
import edu.java.entity.Link;
import edu.java.service.LinkService;
import edu.java.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LinksController {
    private final LinkService service;

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> listLinks(@RequestHeader("Tg-Chat-Id") long chatId) {
        List<Link> linksList = service.listAll(chatId);

        ListLinksResponse response = new ListLinksResponse(
            linksList.stream()
                .map(link -> new LinkResponse(link.getId(), link.getUrl()))
                .toList(),
            linksList.size()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") long chatId,
        @Validated @RequestBody AddLinkRequest request
    ) {
        Link responseLink = service.add(chatId, request.link());
        return new ResponseEntity<>(new LinkResponse(responseLink.getId(), responseLink.getUrl()), HttpStatus.OK);
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader("Tg-Chat-Id") long chatId,
        @Validated @RequestBody AddLinkRequest request
    ) {
        Link responseLink = service.remove(chatId, request.link());
        return new ResponseEntity<>(new LinkResponse(responseLink.getId(), responseLink.getUrl()), HttpStatus.OK);
    }
}
