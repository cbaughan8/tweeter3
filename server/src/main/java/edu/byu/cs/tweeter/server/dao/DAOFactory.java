package edu.byu.cs.tweeter.server.dao;

public interface DAOFactory {
    public UserDAO getUserDao();
    public FollowDAO getFollowDao();
    public AuthTokenDAO getAuthTokenDAO();
    public FeedDAO getFeedDAO();
    public StoryDAO getStoryDAO();
    public ImageDAO getImageDao();
}
