package edu.java.scrapper;

import edu.java.linkParser.Parser;
import edu.java.linkParser.links.GitHubParseResult;
import edu.java.linkParser.links.ParseResult;
import edu.java.linkParser.links.StackOverflowParseResult;
import java.net.URI;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ParserTest.Config.class)
public class ParserTest {
    @Configuration
    @ComponentScan("edu.java.linkParser")
    public static class Config {
    }

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
