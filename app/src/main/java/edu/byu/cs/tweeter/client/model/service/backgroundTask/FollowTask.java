package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.Response;

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
    public Response getResponse() throws IOException, TweeterRemoteException {
        FollowRequest request = new FollowRequest(authToken, followee);
        return getServerFacade().follow(request, URL_PATH);
    }


}
