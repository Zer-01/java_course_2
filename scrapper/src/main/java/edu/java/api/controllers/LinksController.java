package edu.java.api.controllers;

import edu.java.api.models.AddLinkRequest;
import edu.java.api.models.LinkResponse;
import edu.java.api.models.ListLinksResponse;
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

@Slf4j
@RequiredArgsConstructor
@RestController
public class LinksController {
    private final UserService service;

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> listLinks(@RequestHeader("Tg-Chat-Id") long chatId) {
        var linksList = service.getAllLinks(chatId);

        ListLinksResponse response = new ListLinksResponse(
            linksList.stream()
                .map(link -> new LinkResponse((long) link.hashCode(), link))
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
        return new ResponseEntity<>(service.addLink(chatId, request.link()), HttpStatus.OK);
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader("Tg-Chat-Id") long chatId,
        @Validated @RequestBody AddLinkRequest request
    ) {
        return new ResponseEntity<>(service.deleteLink(chatId, request.link()), HttpStatus.OK);
    }
}
