package edu.java.service;

import edu.java.api.models.LinkUpdateRequest;
import java.util.List;

public interface SendUpdateService {
    void sendUpdates(List<LinkUpdateRequest> requests);
}
