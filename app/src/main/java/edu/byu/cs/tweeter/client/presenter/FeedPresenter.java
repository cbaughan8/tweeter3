package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {

    public FeedPresenter(PagedView<Status> view) {
        super(view);
    }

    public void getItems(User user){
        statusService.loadMoreItemsFeed(user, PAGE_SIZE, lastItem, new FeedObserver());
    }

    public class FeedObserver extends GetPagedObserver {
        final String FEED_DESCRIPTION = "feed";
        @Override
        public String getDescription() {
            return FEED_DESCRIPTION;
        }
    }
}
