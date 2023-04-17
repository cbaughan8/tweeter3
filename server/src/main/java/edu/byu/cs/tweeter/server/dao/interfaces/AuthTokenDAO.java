package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.server.dao.beans.AuthTokenBean;

public interface AuthTokenDAO {

    void create(AuthTokenBean authTokenBean);

    AuthTokenBean get(String token);

    void update(AuthTokenBean authTokenBean);

    void delete(String token);
}
