package edu.byu.cs.tweeter.model.net.response;

public class FollowersCountResponse extends CountResponse {

    public FollowersCountResponse(String message) {
        super(message);
    }

    public FollowersCountResponse(int count){
        super(count);
    }
}
