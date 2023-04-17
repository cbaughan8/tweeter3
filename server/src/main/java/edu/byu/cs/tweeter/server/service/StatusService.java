package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PagedStatusRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedDAO;
import edu.byu.cs.tweeter.server.dao.StatusDAODummy;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {
    StoryDAO storyDAO = null;
    FeedDAO feedDAO = null;

    public StatusService(StoryDAO storyDAO) {
        this.storyDAO = storyDAO;
    }

    public StatusService(FeedDAO feedDAO) {
        this.feedDAO = feedDAO;
    }

    public StatusService(StoryDAO storyDAO, FeedDAO feedDAO) {
        this.storyDAO = storyDAO;
        this.feedDAO = feedDAO;
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Request needs to have an authToken");
        } else if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authToken");
        }
        StatusDAODummy statusDAODummy = new StatusDAODummy();
        return statusDAODummy.postStatus(request);
    }

    public FeedResponse getFeed(FeedRequest request) {
        statusesCheck(request);
        Pair<List<Status>, Boolean> dummyData = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
        return new FeedResponse(dummyData.getFirst(), dummyData.getSecond());
    }

    public StoryResponse getStory(StoryRequest request) {
        statusesCheck(request);

        Pair<List<Status>, Boolean> dummyData = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
        return new StoryResponse(dummyData.getFirst(), dummyData.getSecond());
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



    FakeData getFakeData() {
        return FakeData.getInstance();
    }

    public StoryDAO getStoryDAO() {
        return storyDAO;
    }

    public FeedDAO getFeedDAO() {
        return feedDAO;
    }
}
