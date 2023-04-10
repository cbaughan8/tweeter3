package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.handler.AuthenticateHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SingleInputObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginService extends TweeterService {

    public void doLogin(String alias, String password, SingleInputObserver<User> observer) {
        LoginTask loginTask = new LoginTask(alias, password, new AuthenticateHandler(observer));
        executeTask(loginTask);
    }

    public void logoutTask(SimpleNotificationObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(),
                new SimpleNotificationHandler(observer));
        executeTask(logoutTask);
    }



}
