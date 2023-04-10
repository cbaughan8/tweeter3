package edu.byu.cs.tweeter.client.presenter;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import edu.byu.cs.tweeter.client.model.service.RegisterService;
import edu.byu.cs.tweeter.client.model.service.observer.SingleInputObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends Presenter {

    private final RegisterService registerService;


    public interface RegisterView extends PresenterView {
        void startRegistration(User registeredUser);
    }

    public RegisterPresenter(PresenterView view) {
        super(view);
        this.registerService = new RegisterService();
    }

    public void validateRegistration(String firstNameText, String lastNameText, String aliasText,
                                     String passwordText, Drawable drawableImage) {
        if (firstNameText.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastNameText.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (aliasText.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (aliasText.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (aliasText.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (passwordText.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (drawableImage == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    public void doRegistration(String firstName, String lastName, String alias, String password, String imageBytesBase64) {
        registerService.doRegistration(firstName, lastName, alias, password ,imageBytesBase64, new RegisterObserver());

    }


    public class RegisterObserver extends SimpleFailMessageObserver implements SingleInputObserver<User> {

        final public String REGISTER_DESCRIPTION = "register";
        @Override
        public void handleSuccess(User user) {
            ((RegisterView)view).startRegistration(user);
        }

        @Override
        public String getDescription() {
            return REGISTER_DESCRIPTION;
        }
    }
}
