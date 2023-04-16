package edu.byu.cs.tweeter.server.lambda;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAODynamo;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.FeedDAODynamo;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAODummy;
import edu.byu.cs.tweeter.server.dao.ImageDAO;
import edu.byu.cs.tweeter.server.dao.ImageDAOS3;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAODynamo;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.dao.UserDAODummy;

public class Handler implements DAOFactory {

    @Override
    public UserDAO getUserDao() {
        return new UserDAODummy();
    }

    @Override
    public FollowDAO getFollowDao() {
        return new FollowDAODummy();
    }

    @Override
    public AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAODynamo();
    }

    @Override
    public FeedDAO getFeedDAO() {
        return new FeedDAODynamo();
    }

    @Override
    public StoryDAO getStoryDAO() {
        return new StoryDAODynamo();
    }

    @Override
    public ImageDAO getImageDao() {
        return new ImageDAOS3();
    }
}
