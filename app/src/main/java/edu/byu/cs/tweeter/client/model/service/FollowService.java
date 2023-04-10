package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.GetCountHandler;
import edu.byu.cs.tweeter.client.model.service.handler.GetItemsHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PagesObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SingleInputObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends TweeterService {

    public void unfollowTask(User selectedUser, MainPresenter.UnfollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        executeTask(unfollowTask);
        observer.displayMessage("Removing " + selectedUser.getName() + "...");
    }

    public void followTask(User selectedUser, MainPresenter.FollowObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        executeTask(followTask);
        observer.displayMessage("Adding " + selectedUser.getName() + "...");
    }

    public void loadMoreFollowing(User user, int PAGE_SIZE, User lastFollowee, PagesObserver<User> observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastFollowee, new GetItemsHandler<>(observer));
        executeTask(getFollowingTask);
    }

    public void loadMoreFollowers(User user, int PAGE_SIZE, User lastFollower, PagesObserver<User> observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastFollower, new GetItemsHandler<>(observer));
        executeTask(getFollowersTask);
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser,
            SingleInputObserver<Integer> followingObserver, SingleInputObserver<Integer> followersObserver) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetCountHandler(followersObserver));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetCountHandler(followingObserver));
        executor.execute(followingCountTask);
    }

    public void isFollowerTask(User selectedUser, SingleInputObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        executeTask(isFollowerTask);
    }

    private static class IsFollowerHandler extends BackgroundTaskHandler<SingleInputObserver<Boolean>> {
        public IsFollowerHandler(SingleInputObserver<Boolean> observer) {
            super(observer);
        }

        @Override
        protected void handleSuccess(Bundle data, SingleInputObserver<Boolean> observer) {
            observer.handleSuccess(data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY));
        }
    }


}