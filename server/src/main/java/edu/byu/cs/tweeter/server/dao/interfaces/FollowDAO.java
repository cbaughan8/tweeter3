package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.beans.DataPage;
import edu.byu.cs.tweeter.server.dao.beans.FollowsBean;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

public interface FollowDAO {
    public DataPage<FollowsBean> getPageOfFollowers(String targetUserAlias, int limit, String lastUserAlias);
    public DataPage<FollowsBean> getPageOfFollowees(String targetUserAlias, int limit, String lastUserAlias);
    public void deleteDynamoDBItem(String followerHandleVal, String followeeHandleVal);

    public void putItemInTable(String followersHandleVal, String followerNameVal,
                               String followeesHandleVal,
                               String followeeNameVal);

    public void create(FollowsBean followsBean);
    public void delete(FollowsBean followsBean);

    public FollowsBean getFollower(String followerAlias, String followeeAlias);

    List<FollowsBean> getFollowers(FollowersRequest request);

    List<FollowsBean> getFollowees(FollowingRequest request);

    SdkIterable<Page<FollowsBean>> getFollowersFeed(String alias);

    boolean hasMorePages();
}
