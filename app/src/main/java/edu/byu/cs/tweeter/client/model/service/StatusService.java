package edu.byu.cs.tweeter.client.model.service;

import android.util.Log;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.handler.GetItemsHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PagesObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
public class StatusService extends TweeterService {
    private static final String LOG_TAG = "MainActivity";

    public void loadMoreItemsStory(User user, int PAGE_SIZE, Status lastStatus, PagesObserver<Status> observer) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastStatus, new GetItemsHandler<>(observer));
        executeTask(getStoryTask);
    }

    public void loadMoreItemsFeed(User user, int PAGE_SIZE, Status lastStatus, PagesObserver<Status> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastStatus, new GetItemsHandler<>(observer));
        executeTask(getFeedTask);
    }

    public void onStatusTask(Status newStatus, SimpleNotificationObserver observer) {
        try {
            PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                    newStatus, new SimpleNotificationHandler(observer));
            executeTask(statusTask);
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            observer.handleFailure("Failed to post the status because of exception: " + ex.getMessage());
        }
    }


}
