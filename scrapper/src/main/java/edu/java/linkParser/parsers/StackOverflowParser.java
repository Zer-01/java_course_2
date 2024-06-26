package edu.java.linkParser.parsers;

import edu.java.linkParser.links.ParseResult;
import edu.java.linkParser.links.StackOverflowParseResult;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class StackOverflowParser extends SiteParser {
    private static final String HOST = "stackoverflow.com";
    private static final String QUESTIONS = "questions";
    private static final int MIN_ARGUMENTS_COUNT = 2;

    @Override
    public Optional<ParseResult> parse(String host, String path) {
        if (host.equals(HOST)) {
            String[] pathArr = path.replaceAll("^/|/$", "").split("/");
            if (pathArr.length >= MIN_ARGUMENTS_COUNT && pathArr[0].equals(QUESTIONS)) {
                return Optional.of(new StackOverflowParseResult(pathArr[1]));
            }
        }
        return Optional.empty();
    }
}
