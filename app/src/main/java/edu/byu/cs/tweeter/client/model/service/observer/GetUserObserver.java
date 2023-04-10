package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.model.domain.User;

public interface GetUserObserver extends SingleInputObserver<User> {

    void displayMessage(String message);

}
