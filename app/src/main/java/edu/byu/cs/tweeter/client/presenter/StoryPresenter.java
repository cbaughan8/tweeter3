package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {


    public StoryPresenter(PagedView<Status> view){
        super(view);

    }

    @Override
    public void getItems(User user) {
        statusService.loadMoreItemsStory(user, PAGE_SIZE, lastItem, new StoryObserver());
    }

    public class StoryObserver extends GetPagedObserver {

        final String STORY_DESCRIPTION = "story";

        @Override
        public String getDescription() {
            return STORY_DESCRIPTION;
        }
    }
}
