package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {

    public FollowingPresenter(PagedView<User> view) {
        super(view);
    }

    @Override
    public void getItems(User user) {
        followService.loadMoreFollowing(user, PAGE_SIZE, lastItem, new FollowingObserver());
    }

    public class FollowingObserver extends GetPagedObserver {

        final String DESCRIPTION = "following";
        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    }
}