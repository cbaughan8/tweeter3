package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class ToggleFollowRequest {
    AuthToken authToken;
    User selectedUser;

    public ToggleFollowRequest() {}
    public ToggleFollowRequest(AuthToken authToken, User selectedUser) {
        this.authToken = authToken;
        this.selectedUser = selectedUser;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }
}
