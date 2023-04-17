package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.beans.DataPage;
import edu.byu.cs.tweeter.server.dao.beans.FollowsBean;

public interface FollowDAO {
    public DataPage<FollowsBean> getPageOfFollowers(String targetUserAlias, int limit, String lastUserAlias);
    public DataPage<FollowsBean> getPageOfFollowees(String targetUserAlias, int limit, String lastUserAlias);
    public void deleteDynamoDBItem(String followerHandleVal, String followeeHandleVal);

    public void putItemInTable(String followersHandleVal, String followerNameVal,
                               String followeesHandleVal,
                               String followeeNameVal);

    public void create(FollowsBean followsBean);
    public void delete(FollowsBean followsBean);
//    public FollowingResponse getFollowing(FollowingRequest request);
//    public FollowersResponse getFollowers(FollowersRequest request);
//    public FollowingCountResponse getFollowingCount(FollowingCountRequest request);
//    public FollowersCountResponse getFollowersCount(FollowersCountRequest request);
//    public FollowResponse follow(FollowRequest request);
//    public UnfollowResponse unfollow(UnfollowRequest request);
//    public IsFollowerResponse isFollower(IsFollowerRequest request);
}
