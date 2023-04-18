package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PagedStatusRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.beans.AuthTokenBean;
import edu.byu.cs.tweeter.server.dao.beans.FeedBean;
import edu.byu.cs.tweeter.server.dao.beans.FollowsBean;
import edu.byu.cs.tweeter.server.dao.beans.StoryBean;
import edu.byu.cs.tweeter.server.dao.beans.UserBean;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedDAO;
import edu.byu.cs.tweeter.server.dao.StatusDAODummy;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

public class StatusService {
    StoryDAO storyDAO;
    FeedDAO feedDAO;

    UserDAO userDAO;

    FollowDAO followDAO;

    AuthTokenDAO authTokenDAO;

    public StatusService(StoryDAO storyDAO, FeedDAO feedDAO, UserDAO userDAO, FollowDAO followDAO, AuthTokenDAO authTokenDAO) {
        this.storyDAO = storyDAO;
        this.feedDAO = feedDAO;
        this.userDAO = userDAO;
        this.followDAO = followDAO;
        this.authTokenDAO = authTokenDAO;
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Request needs to have an authToken");
        } else if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authToken");
        } else if (!validateAuthToken(request.getAuthToken(), request.getCurrUser().getAlias())){
            throw new RuntimeException("[Bad Request] Invalid authtoken provided");
        }

        try {
            StoryBean storyBean = new StoryBean(request.getCurrUser().getAlias(), request.getStatus().getTimestamp(),
                    request.getStatus().getPost(), request.getStatus().getMentions(), request.getStatus().getUrls());
            getStoryDAO().create(storyBean);
            SdkIterable<Page<FollowsBean>> iterable = getFollowDAO().getFollowersFeed(request.getCurrUser().getAlias());
            iterable.forEach(page -> {
                List<FollowsBean> followers = page.items();
                for (FollowsBean follower : followers) {
                    FeedBean feedBean = new FeedBean(follower.getFollower_handle(), request.getStatus().getTimestamp(), request.getCurrUser().getAlias(),
                            request.getStatus().getPost(),  request.getStatus().getMentions() ,request.getStatus().getUrls());
                    getFeedDAO().create(feedBean);
                }
            });
            return new PostStatusResponse();
        } catch (Exception e) {
            return new PostStatusResponse(e.getMessage());
        }
        // add once to story table and multiple times to feeed

//        StatusDAODummy statusDAODummy = new StatusDAODummy();
//        return statusDAODummy.postStatus(request);
    }

    public FeedResponse getFeed(FeedRequest request) {
        statusesCheck(request);

        List<FeedBean> storyBeanList = getFeedDAO().getStatuses(request);
        List<Status> statusList = new ArrayList<>();
        for (FeedBean entry : storyBeanList) {
            UserBean userBean = getUserDAO().get(entry.getPoster_alias());
            User newUser = new User(userBean.getFirst_name(), userBean.getLast_name(), userBean.getAlias(), userBean.getImage_url());
            Status newStatus = new Status(entry.getPost(), newUser, entry.getTimestamp(), entry.getUrls(), entry.getMentions());
            statusList.add(newStatus);
        }
        return new FeedResponse(statusList, getStoryDAO().hasMorePages());
//        Pair<List<Status>, Boolean> dummyData = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
//        return new FeedResponse(dummyData.getFirst(), dummyData.getSecond());
    }

    public StoryResponse getStory(StoryRequest request) {
        statusesCheck(request);

        List<StoryBean> storyBeanList = getStoryDAO().getStatuses(request);
        List<Status>statusList = new ArrayList<>();
        for (StoryBean entry : storyBeanList) {
            UserBean userBean = getUserDAO().get(entry.getSender_alias());
            User newUser = new User(userBean.getFirst_name(), userBean.getLast_name(), userBean.getAlias(), userBean.getImage_url());
            Status newStatus = new Status(entry.getPost(), newUser, entry.getTimestamp(), entry.getUrls(), entry.getMentions());
            statusList.add(newStatus);
        }
        return new StoryResponse(statusList, getStoryDAO().hasMorePages());

//        Pair<List<Status>, Boolean> dummyData = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
//        return new StoryResponse(dummyData.getFirst(), dummyData.getSecond());
    }

    public void statusesCheck(PagedStatusRequest request) {
        if (request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        } else if (request.getLimit() < 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (!validateAuthToken(request.getAuthToken(), request.getCurrUser().getAlias())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken provided");
        }
        // when deleted the feed appears twice
    }


    private boolean validateAuthToken(AuthToken authToken, String alias) {
        long currTime = System.currentTimeMillis();
        System.out.println(currTime - Long.parseLong(authToken.getDatetime()));
        if (authToken == null || currTime - Long.parseLong(authToken.getDatetime()) > 300000) {
            if (authToken != null) {
                getAuthTokenDAO().delete(authToken.getToken());
            }
            return false;
        }
        AuthTokenBean newAuthTokenBean = new AuthTokenBean(authToken.getToken(), Long.parseLong(authToken.getDatetime()), alias);
        System.out.println(newAuthTokenBean);
        getAuthTokenDAO().update(newAuthTokenBean);
        return true;
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

    public AuthTokenDAO getAuthTokenDAO() {
        return authTokenDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public FollowDAO getFollowDAO() {
        return followDAO;
    }
}
