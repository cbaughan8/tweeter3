package edu.byu.cs.tweeter.model.net.response;

public abstract class ToggleFollowResponse extends Response {
    public ToggleFollowResponse(String message) {
        super(false, message);
    }
    public ToggleFollowResponse() {
        super(true, null);
    }
}
