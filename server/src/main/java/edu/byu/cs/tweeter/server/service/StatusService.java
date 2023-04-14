package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PagedStatusRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAODynamo;

public class StatusService {

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Request needs to have an authToken");
        } else if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authToken");
        }
        StatusDAODynamo statusDAODynamo = new StatusDAODynamo();
        return statusDAODynamo.postStatus();
    }

    public FeedResponse getFeed(FeedRequest request) {
        statusesCheck(request);
        return getStatusDao().getFeed(request);
    }

    public StoryResponse getStory(StoryRequest request) {
        statusesCheck(request);
        return getStatusDao().getStory(request);
    }

    public void statusesCheck(PagedStatusRequest request) {
//        if (request.getAuthToken() == null) {
//            throw new RuntimeException("[Bad Request] Request needs to have an authToken");
//        } else
        if (request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        } else if (request.getLimit() < 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
//        else if (request.getLastStatus() == null) {
//            throw new RuntimeException("[Bad Request] Request needs to have a last status");
//        }
        // when deleted the feed appears twice
    }

    public StatusDAODynamo getStatusDao() {
        return new StatusDAODynamo();
    }



}
