package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;

/**
 * A response for a {@link RegisterRequest}.
 */
public class RegisterResponse extends Response {

    private User user;
    private AuthToken authToken;


    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public RegisterResponse(String message) {
        super(false, message);
    }

    public RegisterResponse(boolean success, String message) {
        super(success, message);
    }
}
