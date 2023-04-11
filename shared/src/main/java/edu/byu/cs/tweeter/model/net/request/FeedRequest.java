package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedRequest extends PagedStatusRequest {
    private FeedRequest() {
        super();
    }

    public FeedRequest(AuthToken authToken, User targetUser, int limit, Status lastStatus) {
        super(authToken, targetUser, limit, lastStatus);
    }
}
