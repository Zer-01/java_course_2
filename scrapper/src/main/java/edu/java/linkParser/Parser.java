package edu.java.linkParser;

import edu.java.linkParser.links.ParseResult;
import java.net.URI;
import java.util.Optional;

public interface Parser {
    public Optional<ParseResult> parse(URI url);
}
