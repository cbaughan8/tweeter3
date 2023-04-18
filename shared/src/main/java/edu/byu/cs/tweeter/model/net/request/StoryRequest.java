package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryRequest extends PagedStatusRequest {
    private StoryRequest() {
        super();
    }

    public StoryRequest(AuthToken authToken, User targetUser, int limit, Status lastStatus, User currUser) {
        super(authToken, targetUser, limit, lastStatus, currUser);
    }
}
