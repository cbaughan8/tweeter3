package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.beans.StoryBean;

public interface StoryDAO {
    List<StoryBean> getStatuses(StoryRequest request);

    void create(StoryBean storyBean);

    public StoryResponse getStory(StoryRequest request);

    boolean hasMorePages();
}
