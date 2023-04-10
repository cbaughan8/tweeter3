package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.SingleInputObserver;

public class GetCountHandler extends BackgroundTaskHandler<SingleInputObserver<Integer>> {

    public GetCountHandler(SingleInputObserver<Integer> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, SingleInputObserver<Integer> observer) {
        observer.handleSuccess(data.getInt(GetCountTask.COUNT_KEY));
    }
}
