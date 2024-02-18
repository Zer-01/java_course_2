package edu.java.bot;

import edu.java.bot.validators.URLValidator;
import java.net.URI;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

public class URLValidationTest {
    static Arguments[] urls() {
        return new Arguments[] {
            Arguments.of("https://test.com/link1", true),
            Arguments.of("https://test.com/", true),
            Arguments.of("https://test/", false),
            Arguments.of("ftp://test.com/link1", false)
        };
    }

    @ParameterizedTest
    @MethodSource("urls")
    void urlValidationTest(String urlStr, boolean expResult) {
        URI url = URI.create(urlStr);

        boolean result = URLValidator.isValidLink(url);

        assertThat(result)
            .isEqualTo(expResult);
    }
}
