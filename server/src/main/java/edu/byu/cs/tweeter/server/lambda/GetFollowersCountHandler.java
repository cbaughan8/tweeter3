package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersCountHandler extends Handler implements RequestHandler
                                <FollowersCountRequest, FollowersCountResponse> {
    @Override
    public FollowersCountResponse handleRequest(FollowersCountRequest request, Context context) {
        FollowService followService = new FollowService(getFollowDao());
        return followService.getFollowersCount(request);
    }
}
