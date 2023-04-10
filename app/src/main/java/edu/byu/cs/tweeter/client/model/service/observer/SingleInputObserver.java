package edu.byu.cs.tweeter.client.model.service.observer;

public interface SingleInputObserver<T> extends ServiceObserver {
    void handleSuccess(T t);
}
