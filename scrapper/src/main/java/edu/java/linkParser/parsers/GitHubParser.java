package edu.java.linkParser.parsers;

import edu.java.linkParser.links.GitHubParseResult;
import edu.java.linkParser.links.ParseResult;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class GitHubParser extends SiteParser {
    private static final String HOST = "github.com";
    private static final int MIN_ARGUMENTS_COUNT = 2;

    @Override
    public Optional<ParseResult> parse(String host, String path) {
        if (host.equals(HOST)) {
            String[] pathArr = path.replaceAll("^/|/$", "").split("/");
            if (pathArr.length >= MIN_ARGUMENTS_COUNT) {
                return Optional.of(new GitHubParseResult(pathArr[0], pathArr[1]));
            }
        }
        return Optional.empty();
    }
}
