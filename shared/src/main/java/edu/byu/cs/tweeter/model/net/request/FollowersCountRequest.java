package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersCountRequest extends CountRequest {

    private FollowersCountRequest() {
        super();
    }

    public FollowersCountRequest(AuthToken authToken, User targetUser) {
        super(authToken, targetUser);
    }
}
