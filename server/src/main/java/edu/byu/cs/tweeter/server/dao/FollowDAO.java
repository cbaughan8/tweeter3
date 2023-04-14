package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

public interface FollowDAO {
    public FollowingResponse getFollowing(FollowingRequest request);
    public FollowersResponse getFollowers(FollowersRequest request);
    public FollowingCountResponse getFollowingCount(FollowingCountRequest request);
    public FollowersCountResponse getFollowersCount(FollowersCountRequest request);
    public FollowResponse follow(FollowResponse request);
    public UnfollowResponse unfollow(UnfollowRequest request);
    public IsFollowerResponse isFollower(IsFollowerRequest request);
}
