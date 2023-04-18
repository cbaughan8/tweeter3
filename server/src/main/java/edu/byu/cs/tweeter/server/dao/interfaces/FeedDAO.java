package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.server.dao.beans.FeedBean;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

public interface FeedDAO {

    List<FeedBean> getStatuses(FeedRequest request);


    void create(FeedBean bean);
}
