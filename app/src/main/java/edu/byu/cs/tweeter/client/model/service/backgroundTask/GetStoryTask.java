package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {

    private final String URL_PATH = "/getstory";

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected void getItems() {
        try {
            StoryRequest request = new StoryRequest(authToken, getTargetUser(), getLimit(), getLastItem());
            StoryResponse response = getServerFacade().getStory(request, URL_PATH);
            if (response.isSuccess()) {
                setItems(response.getStory());
                setHasMorePages(response.getHasMorePages());
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followees", ex);
            sendExceptionMessage(ex);
        }
//        return getFakeData().getPageOfStatus(getLastItem(), getLimit());
    }
}
