package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAODummy;
import edu.byu.cs.tweeter.server.service.StatusService;

public class PostStatusHandler extends Handler implements RequestHandler<PostStatusRequest, PostStatusResponse> {

    @Override
    public PostStatusResponse handleRequest(PostStatusRequest request, Context context) {
        StatusService statusService = new StatusService(getStoryDAO(), getFeedDAO(), getUserDao(), getFollowDao(), getAuthTokenDAO());
        return statusService.postStatus(request);
    }
}
