package edu.java.linkParser.parsers;

import edu.java.linkParser.links.ParseResult;
import java.net.URI;
import java.util.Optional;

public abstract class SiteParser {
    private SiteParser nextParser;

    public SiteParser() {
    }

    public abstract Optional<ParseResult> parse(String host, String path);

    protected SiteParser getNext() {
        return nextParser;
    }

    public void setNext(SiteParser nextParser) {
        this.nextParser = nextParser;
    }
}
