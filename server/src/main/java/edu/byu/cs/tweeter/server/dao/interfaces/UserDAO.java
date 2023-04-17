package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.beans.UserBean;

public interface UserDAO  {

    public void create(UserBean userBean);

    public UserBean get(String alias);

}
