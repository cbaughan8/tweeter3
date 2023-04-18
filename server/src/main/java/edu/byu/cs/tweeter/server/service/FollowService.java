package edu.byu.cs.tweeter.server.service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.CountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.beans.AuthTokenBean;
import edu.byu.cs.tweeter.server.dao.beans.DataPage;
import edu.byu.cs.tweeter.server.dao.beans.FollowsBean;
import edu.byu.cs.tweeter.server.dao.beans.UserBean;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAODummy;
import edu.byu.cs.tweeter.server.dao.interfaces.ImageDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    FollowDAO followDAO;

    UserDAO userDAO;

    AuthTokenDAO authTokenDAO;

    public FollowService(FollowDAO followDAO, UserDAO userDAO, AuthTokenDAO authTokenDAO) {
        this.followDAO = followDAO;
        this.userDAO = userDAO;
        this.authTokenDAO = authTokenDAO;
    }

    public FollowService(FollowDAO followDAO) {
        this.followDAO = followDAO;
    }


    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAODummy} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        assert request.getLimit() > 0;
        assert request.getFollowerAlias() != null;
        User follower = getFakeData().findUserByAlias(request.getFollowerAlias());
        User lastFollowee = getFakeData().findUserByAlias(request.getLastFolloweeAlias());

        DataPage<FollowsBean> dataPage = getFollowDAO().getPageOfFollowees(request.getFollowerAlias(),
                request.getLimit(), request.getLastFolloweeAlias());

        System.out.println("FOLLOWEES!");
        System.out.println("DATAPAGE VALUES:");
        System.out.println(dataPage.getValues());

        Pair<List<User>, Boolean> data = getFakeData().getPageOfUsers(lastFollowee, request.getLimit(), follower);
        return new FollowingResponse(data.getFirst(), data.getSecond());
//        return getFollowingDAO().getFollowing(request);
    }

    public FollowersResponse getFollowers(FollowersRequest request) {
        if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (!validateAuthToken(request.getAuthToken(), request.getFolloweeAlias())){
            throw new RuntimeException("[Bad Request] Invalid authtoken provided");
        }

        //
        assert request.getLimit() > 0;
        assert request.getFolloweeAlias() != null;

        DataPage<FollowsBean> dataPage = getFollowDAO().getPageOfFollowers(request.getFolloweeAlias(),
                request.getLimit(), request.getLastFollowerAlias());


        System.out.println("FOLLOWERS!");
        System.out.println("DATAPAGE VALUES:");
        System.out.println(dataPage.getValues());

        User followee = getFakeData().findUserByAlias(request.getFolloweeAlias());
        User lastFollower = getFakeData().findUserByAlias(request.getLastFollowerAlias());

        Pair<List<User>, Boolean> data = getFakeData().getPageOfUsers(lastFollower, request.getLimit(), followee);
        return new FollowersResponse(data.getFirst(), data.getSecond());
//        return getFollowingDAO().getFollowers(request);
    }

    public FollowResponse follow(FollowRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authToken");
        } else if (request.getSelectedUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user selected");
        }

        AuthTokenBean authTokenBean = getAuthTokenDAO().get(request.getAuthToken().getToken());
        UserBean followerUserBean = getUserDAO().get(authTokenBean.getAlias());

        if (!validateAuthToken(request.getAuthToken(), followerUserBean.getAlias())){
            throw new RuntimeException("[Bad Request] Invalid authtoken provided");
        }

        FollowsBean bean = new FollowsBean(followerUserBean.getAlias(), request.getSelectedUser().getAlias(),
                followerUserBean.getFirst_name(), request.getSelectedUser().getFirstName());
        getFollowDAO().create(bean);
        return new FollowResponse();
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authToken");
        } else if (request.getSelectedUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user selected");
        }
        AuthTokenBean authTokenBean = getAuthTokenDAO().get(request.getAuthToken().getToken());
        UserBean followerUserBean = getUserDAO().get(authTokenBean.getAlias());

        if (!validateAuthToken(request.getAuthToken(), followerUserBean.getAlias())){
            throw new RuntimeException("[Bad Request] Invalid authtoken provided");
        }

        FollowsBean bean = new FollowsBean(followerUserBean.getAlias(), request.getSelectedUser().getAlias(),
                followerUserBean.getFirst_name(), request.getSelectedUser().getFirstName());
        getFollowDAO().delete(bean);

        return new UnfollowResponse();
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if (request.getFollower() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower");
        } else if (request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authToken");
        } else if (!validateAuthToken(request.getAuthToken(), request.getFollower().getAlias())){
            throw new RuntimeException("[Bad Request] Invalid authtoken provided");
        }

        boolean isFollower = followDAO.getFollower(request.getFollower().getAlias(),
                request.getFollowee().getAlias()) != null;

        return new IsFollowerResponse(isFollower);
    }

    public FollowersCountResponse getFollowersCount(FollowersCountRequest request) {
        getCountCheck(request);
        // getUser and pull following count from that


        return new FollowersCountResponse(getFolloweeCount(request.getTargetUser()));

    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        getCountCheck(request);
        // getUser and pull following count from that

        return new FollowingCountResponse(getFolloweeCount(request.getTargetUser()));
    }

    private void getCountCheck(CountRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authToken");
        } else if (request.getTargetUser() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        } else if (!validateAuthToken(request.getAuthToken(), request.getTargetUser().getAlias())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken provided");
        }
    }

    private int getStartingIndex(String lastPersonAlias, List<User> allPeople) {
        int followeesIndex = 0;

        if(lastPersonAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allPeople.size(); i++) {
                if(lastPersonAlias.equals(allPeople.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
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

    /**
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param follower the User whose count of how many following is desired.
     * @return said count.
     */
    public Integer getFolloweeCount(User follower) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        // Includes user at the moment
        assert follower != null;
        // -1 for the user
        return 20;
    }

    FakeData getFakeData() {
        return FakeData.getInstance();
    }
    FollowDAO getFollowDAO() {
        return followDAO;
    }

    UserDAO getUserDAO() {
        return userDAO;
    }

    public AuthTokenDAO getAuthTokenDAO() {
        return authTokenDAO;
    }
}


