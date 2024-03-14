package edu.java.linkParser;

import edu.java.linkParser.links.ParseResult;
import edu.java.linkParser.parsers.SiteParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class LinksParser implements Parser {
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private final SiteParser parser;

    @Autowired
    public LinksParser(List<SiteParser> parsers) {
        if (parsers.isEmpty()) {
            throw new RuntimeException("No parsers created");
        }
        parser = parsers.getFirst();
        for (int i = 1; i < parsers.size(); i++) {
            parsers.get(i - 1).setNext(parsers.get(i));
        }
    }

    public Optional<ParseResult> parse(URI url) {
        if (!isValidScheme(url)) {
            return Optional.empty();
        }
        return parser.parse(url.getHost(), url.getPath());
    }

    private boolean isValidScheme(URI url) {
        return url.getScheme().equals(HTTP) || url.getScheme().equals(HTTPS);
    }
}
