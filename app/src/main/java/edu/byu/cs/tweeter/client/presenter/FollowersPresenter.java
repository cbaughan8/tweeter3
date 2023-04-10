package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {

    public FollowersPresenter(PagedView<User> view){
        super(view);
    }

    @Override
    public void getItems(User user) {
        followService.loadMoreFollowers(user, PAGE_SIZE, lastItem, new FollowersObserver());
    }

    public class FollowersObserver extends GetPagedObserver {
        final String DESCRIPTION = "followers";
        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    }

}
