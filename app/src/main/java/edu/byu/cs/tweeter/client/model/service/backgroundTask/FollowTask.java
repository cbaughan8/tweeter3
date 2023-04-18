package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.ToggleFollowResponse;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends ToggleFollowTask {

    /**
     * The user that is being followed.
     */

    public static final String URL_PATH = "/follow";

    public FollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(authToken, followee, messageHandler);
    }

    @Override
    public ToggleFollowResponse getResponse() throws IOException, TweeterRemoteException {
        FollowRequest request = new FollowRequest(authToken, followee, Cache.getInstance().getCurrUser());
        return getServerFacade().follow(request, URL_PATH);
    }


}
