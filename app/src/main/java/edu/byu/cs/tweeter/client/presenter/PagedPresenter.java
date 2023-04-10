package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagesObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {

    protected static final int PAGE_SIZE = 10;
    protected UserService userService;
    protected FollowService followService;
    protected StatusService statusService;

    protected boolean isLoading = false;
    protected boolean hasMorePages;

    protected T lastItem;

    public abstract void getItems(User user);

    public PagedPresenter(PresenterView view) {
        super(view);
        this.userService = new UserService();
        this.followService = new FollowService();
        this.statusService = new StatusService();
    }

    public interface PagedView<T> extends PresenterView {
        void setLoadingFooter(boolean status);
        void getIntentStartActivity(User user);

        void addItems(List<T> items);
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            ((PagedView)view).setLoadingFooter(true);
            getItems(user);
        }
    }
    public void getUserTask(String aliasString) {
        userService.getUserTask(aliasString, new UserObserver());
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }


    public class UserObserver extends SimpleFailMessageObserver implements GetUserObserver {

        public final String DESCRIPTION = "get user's profile";

        @Override
        public void handleSuccess(User user) {
            ((PagedView)view).getIntentStartActivity(user);
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    }

    public abstract class GetPagedObserver extends FailedToObserver implements PagesObserver<T> {

        public void setLoadingFooter(boolean status) {
            setLoading(status);
            ((PagedView)view).setLoadingFooter(status);
        }

        @Override
        public void handleFailure(String message) {
            setLoadingFooter(false);
            displayFailure(message);
        }

        @Override
        public void handleException(String message) {
            setLoadingFooter(false);
            displayError(message);
        }

        @Override
        public void handleSuccess(List<T> items, boolean hasMorePages) {
            setLoadingFooter(false);
            setHasMorePages(hasMorePages);
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            ((PagedView<T>)view).addItems(items);
        }

    }

}
