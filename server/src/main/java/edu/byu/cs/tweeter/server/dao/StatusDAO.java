package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

public class StatusDAO {
    public PostStatusResponse postStatus() {
        return new PostStatusResponse();
    }
}
