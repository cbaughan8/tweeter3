package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PagedStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusDAO {
    public PostStatusResponse postStatus() {
        return new PostStatusResponse();
    }

    public FeedResponse getFeed(FeedRequest request) {
        boolean hasMorePages = false;
        Pair<List<Status>, Boolean> dummyData = getDummyStatuses(request, hasMorePages);
        List<Status> responseFeed = dummyData.getFirst();
        hasMorePages = dummyData.getSecond();
        return new FeedResponse(responseFeed, hasMorePages);
    }

    public StoryResponse getStory(StoryRequest request) {
        boolean hasMorePages = false;
        Pair<List<Status>, Boolean> dummyData = getDummyStatuses(request, hasMorePages);
        List<Status> responseStory = dummyData.getFirst();
        hasMorePages = dummyData.getSecond();
        return new StoryResponse(responseStory, hasMorePages);
    }

    private int getStartingIndex(Status lastStatus, List<Status> allStatuses) {
        int followeesIndex = 0;

        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }

    private Pair<List<Status>, Boolean> getDummyStatuses(PagedStatusRequest request, boolean hasMorePages) {
        assert request.getLimit() > 0;
        assert request.getTargetUser() != null;

        List<Status> allStatuses = getDummyFeed();
        List<Status> responseFeed = new ArrayList<>(request.getLimit());

        if(request.getLimit() > 0) {
            if (allStatuses != null) {
                int index = getStartingIndex(request.getLastStatus(), allStatuses);

                for(int limitCounter = 0; index < allStatuses.size() && limitCounter < request.getLimit();
                    index++, limitCounter++) {
                    responseFeed.add(allStatuses.get(index));
                }

                hasMorePages = index < allStatuses.size();
            }
        }
        return new Pair<>(responseFeed, hasMorePages);
    }

    List<Status> getDummyFeed() {
        return getFakeData().getFakeStatuses();
    }

    FakeData getFakeData() {
        return FakeData.getInstance();
    }


}
