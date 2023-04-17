package edu.byu.cs.tweeter.server.lambda;

import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dynamo.AuthTokenDAODynamo;
import edu.byu.cs.tweeter.server.dao.dynamo.FeedDAODynamo;
import edu.byu.cs.tweeter.server.dao.dynamo.FollowDAODynamo;
import edu.byu.cs.tweeter.server.dao.dynamo.ImageDAOS3;
import edu.byu.cs.tweeter.server.dao.dynamo.StoryDAODynamo;
import edu.byu.cs.tweeter.server.dao.dynamo.UserDAODynamo;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.ImageDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;

public class Handler implements DAOFactory {

    @Override
    public UserDAO getUserDao() {
        return new UserDAODynamo();
    }

    @Override
    public FollowDAO getFollowDao() {
        return new FollowDAODynamo();
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
