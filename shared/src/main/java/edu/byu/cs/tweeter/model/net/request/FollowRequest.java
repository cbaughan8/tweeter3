package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowRequest extends  ToggleFollowRequest {


    private FollowRequest() {
        super();
    }

    public FollowRequest(AuthToken authToken, User selectedUser) {
        super(authToken, selectedUser);
        this.authToken = authToken;
        this.selectedUser = selectedUser;
    }
}
