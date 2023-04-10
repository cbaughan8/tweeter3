package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.handler.AuthenticateHandler;
import edu.byu.cs.tweeter.client.model.service.observer.SingleInputObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterService extends TweeterService {

    public void doRegistration(String firstName, String lastName, String alias, String password,
                               String imageBytesBase64, SingleInputObserver<User> observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new AuthenticateHandler( observer));
        executeTask(registerTask);
    }
}
