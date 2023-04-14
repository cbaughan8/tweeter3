package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public interface UserDAO {
    public LogoutResponse logout(LogoutRequest request);
    public LoginResponse login(LogoutRequest request);
    public RegisterResponse register(RegisterRequest request);
}
