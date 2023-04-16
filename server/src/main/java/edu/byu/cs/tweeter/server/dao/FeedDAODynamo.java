package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class FeedDAODynamo implements FeedDAO {

    private static final String TableName = "feeds";

    public FeedResponse getFeed(FeedRequest request) {
        Pair<List<Status>, Boolean> dummyData = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
        return new FeedResponse(dummyData.getFirst(), dummyData.getSecond());
    }

    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
