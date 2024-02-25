package edu.java.clients.stackoverflow;

import edu.java.dto.QuestionResponse;
import java.util.Optional;

public interface StackoverflowClient {
    Optional<QuestionResponse> fetchLastActivity(long id);
}
