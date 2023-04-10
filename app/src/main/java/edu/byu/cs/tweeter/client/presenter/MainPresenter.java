package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SingleInputObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter{

    private final FollowService followService;
    private StatusService statusService;
    private final LoginService loginService;



    public interface MainView extends PresenterView {
        void setFollowButtonEnabled();

        void updateSelectedUsers(boolean buttonUpdate);

        void toastCancel();

        void setFollowerCountText(int count);

        void setFolloweeCountText(int count);

        void logoutUserCancelToast();

        void setFollowButton(boolean isFollower);
    }

    public MainPresenter(PresenterView view) {
        super(view);
        followService = new FollowService();
//        statusService = getStatusService();
        loginService = getLoginService();
    }

    protected LoginService getLoginService() {
        return new LoginService();
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }

    public void followTask(String followButtonText, android.view.View v, User selectedUser) {
        if (followButtonText.equals(v.getContext().getString(R.string.following))) {
            followService.unfollowTask(selectedUser, new UnfollowObserver());
        }
        else {
            followService.followTask(selectedUser, new FollowObserver());
        }
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser) {
        followService.updateSelectedUserFollowingAndFollowers(selectedUser,
                new GetFollowingCountObserver(),new GetFollowersCountObserver());
    }
    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }
        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public void postStatus(String post) {
        Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), System.currentTimeMillis(), parseURLs(post), parseMentions(post));
        getStatusService().onStatusTask(newStatus, new PostStatusObserver());
    }

    public void clearCache() {
        Cache.getInstance().clearCache();
    }

    public void logoutTask() {
        loginService.logoutTask(new LogoutObserver());
    }

    public void isFollowerTask(User selectedUser) {
        followService.isFollowerTask(selectedUser, new IsFollowerObserver());
    }

    public PostStatusObserver makePostStatusObserver() {
        return new PostStatusObserver();
    }

    public class PostStatusObserver extends SimpleFailMessageObserver implements SimpleNotificationObserver {

        final String DESCRIPTION = "post status";

        @Override
        public String getDescription(){
            return DESCRIPTION;
        }

        @Override
        public void handleSuccess() {
            ((MainView)view).toastCancel();
            view.displayMessage("Successfully Posted!");
        }
    }

    public abstract class ToggleFollowObserver extends FailedToObserver implements SimpleNotificationObserver {

        @Override
        public void handleFailure(String message) {
            displayFailure(message);
            ((MainView)view).setFollowButtonEnabled();
        }

        @Override
        public void handleException(String message) {
            displayError(message);
            ((MainView)view).setFollowButtonEnabled();
        }
    }

    public class FollowObserver extends ToggleFollowObserver {

        final String FOLLOW_DESCRIPTION = "follow";

        public String getDescription() {
            return FOLLOW_DESCRIPTION;
        }
        @Override
        public void handleSuccess() {
            ((MainView)view).updateSelectedUsers(false);
            ((MainView)view).setFollowButtonEnabled();
        }
    }

    public class UnfollowObserver extends ToggleFollowObserver {
        final String UNFOLLOW_DESCRIPTION = "unfollow";

        public String getDescription() {
            return UNFOLLOW_DESCRIPTION;
        }

        @Override
        public void handleSuccess() {
            ((MainView)view).updateSelectedUsers(true);
            ((MainView)view).setFollowButtonEnabled();
        }

    }

    public class GetFollowingCountObserver extends SimpleFailMessageObserver implements SingleInputObserver<Integer> {

        final String DESCRIPTION = "get following count";

        @Override
        public String getDescription(){
            return DESCRIPTION;
        }

        @Override
        public void handleSuccess(Integer count) {
            ((MainView)view).setFolloweeCountText(count);
        }
    }

    public class GetFollowersCountObserver extends SimpleFailMessageObserver implements SingleInputObserver<Integer> {
        final String DESCRIPTION = "get followers count";

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
        @Override
        public void handleSuccess(Integer count) {
            ((MainView)view).setFollowerCountText(count);
        }
    }

    public class IsFollowerObserver extends SimpleFailMessageObserver implements SingleInputObserver<Boolean> {

        final String DESCRIPTION = "determine following relationship";

        public String getDescription() {
            return DESCRIPTION;
        }

        @Override
        public void handleSuccess(Boolean isFollower) {
            ((MainView)view).setFollowButton(isFollower);
        }
    }

    public class LogoutObserver extends SimpleFailMessageObserver implements SimpleNotificationObserver {
        final String DESCRIPTION = "logout";

        public String getDescription() {
            return DESCRIPTION;
        }

        @Override
        public void handleSuccess() {
            ((MainView)view).logoutUserCancelToast();
        }
    }
}
