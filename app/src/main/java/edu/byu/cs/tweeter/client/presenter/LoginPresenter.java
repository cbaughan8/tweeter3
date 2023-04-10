package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.observer.SingleInputObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends Presenter {
    final private LoginService loginService;
    public LoginPresenter(PresenterView view) {
        super(view);
        this.loginService = new LoginService();

    }
    public interface LoginView extends PresenterView {
        void startLogin(User loggedInUser);
    }

    public void doLogin(String alias, String password) {
        loginService.doLogin(alias, password, new LoginObserver());
    }

    public void validateLogin(String alias, String password) {
        if (alias.length() > 0 && alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public class LoginObserver extends SimpleFailMessageObserver implements SingleInputObserver<User> {
        final public String LOGIN_DESCRIPTION = "login";
        @Override
        public void handleSuccess(User user) {
            ((LoginView)view).startLogin(user);
        }

        @Override
        public String getDescription() {
            return LOGIN_DESCRIPTION;
        }
    }



}
