package edu.java.scrapper;

import edu.java.linkParser.Parser;
import edu.java.linkParser.links.GitHubParseResult;
import edu.java.linkParser.links.ParseResult;
import java.net.URI;
import java.util.Optional;
import edu.java.linkParser.links.StackOverflowParseResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ParserTest {
    @Autowired
    Parser linkParser;

    @Test
    public void GitHubTest() {
        URI link = URI.create("https://github.com/sanyarnd/java-course-2023-backend-template");
        ParseResult expResult = new GitHubParseResult("sanyarnd", "java-course-2023-backend-template");

        Optional<ParseResult> result = linkParser.parse(link);

        assertThat(result)
            .isNotEmpty();
        assertEquals(expResult, result.get());
    }

    @Test
    public void StackoverflowTest() {
        URI link = URI.create("https://stackoverflow.com/questions/31433117/random-question");
        ParseResult expResult = new StackOverflowParseResult("31433117");

        Optional<ParseResult> result = linkParser.parse(link);

        assertThat(result)
            .isNotEmpty();
        assertEquals(expResult, result.get());
    }

    @Test
    public void notValidLinkTest() {
        URI link = URI.create("https://random.com/abc/");

        Optional<ParseResult> result = linkParser.parse(link);

        assertThat(result)
            .isEmpty();
    }
}
