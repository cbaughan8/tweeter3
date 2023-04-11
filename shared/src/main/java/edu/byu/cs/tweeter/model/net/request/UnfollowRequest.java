package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowRequest extends ToggleFollowRequest {


    private UnfollowRequest() {
        super();
    }

    public UnfollowRequest(AuthToken authToken, User selectedUser) {
        super(authToken, selectedUser);
    }


}
