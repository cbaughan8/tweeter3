package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StoryDAODynamo implements StoryDAO {

    private static final String TableName = "storys";
    public StoryResponse getStory(StoryRequest request) {
        System.out.println("lastStatus" + request.lastStatus);
        Pair<List<Status>, Boolean> dummyData = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
        return new StoryResponse(dummyData.getFirst(), dummyData.getSecond());
    }

    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
