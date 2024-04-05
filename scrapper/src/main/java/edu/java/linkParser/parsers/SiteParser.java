package edu.java.linkParser.parsers;

import edu.java.linkParser.links.ParseResult;
import java.util.Optional;

public abstract class SiteParser {
    private SiteParser nextParser;

    public SiteParser() {
    }

    public Optional<ParseResult> parseOrDelegate(String host, String path) {
        Optional<ParseResult> tmp = parse(host, path);
        if (tmp.isPresent()) {
            return tmp;
        }

        if (nextParser != null) {
            return nextParser.parse(host, path);
        } else {
            return Optional.empty();
        }
    }

    protected abstract Optional<ParseResult> parse(String host, String path);

    public void setNext(SiteParser nextParser) {
        this.nextParser = nextParser;
    }
}
