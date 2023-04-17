package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.ImageDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;

public interface DAOFactory {
    public UserDAO getUserDao();
    public FollowDAO getFollowDao();
    public AuthTokenDAO getAuthTokenDAO();
    public FeedDAO getFeedDAO();
    public StoryDAO getStoryDAO();
    public ImageDAO getImageDao();
}
