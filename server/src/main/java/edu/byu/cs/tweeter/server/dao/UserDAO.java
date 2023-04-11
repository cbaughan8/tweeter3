package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

public class UserDAO {
    public LogoutResponse logout(LogoutRequest request) {
        return new LogoutResponse();
    }
}
