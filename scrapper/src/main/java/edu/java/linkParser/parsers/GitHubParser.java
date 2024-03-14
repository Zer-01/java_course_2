package edu.java.linkParser.parsers;

import edu.java.linkParser.links.GitHubParseResult;
import edu.java.linkParser.links.ParseResult;
import org.springframework.stereotype.Component;
import java.util.Optional;

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

        if (getNext() != null) {
            return getNext().parse(host, path);
        } else {
            return Optional.empty();
        }
    }
}
