package edu.byu.cs.tweeter.client.presenter;


import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public abstract class Presenter {
    protected final PresenterView view;

    public Presenter(PresenterView view){
        this.view = view;
    }

    public interface PresenterView {
        void displayMessage(String message);
    }

    public abstract class FailedToObserver {

        public abstract String getDescription();

        public void displayFailure(String message) {
            view.displayMessage("Failed to " + getDescription() + ": " + message);
        }

        public void displayError(String message) {
            view.displayMessage("Failed to " + getDescription() + " because of exception: " + message);
        }

        public void displayMessage(String message) {
            view.displayMessage(message);
        }
    }

    public abstract class SimpleFailMessageObserver extends FailedToObserver implements ServiceObserver {

        @Override
        public void handleFailure(String message) {
            displayFailure(message);
        }

        @Override
        public void handleException(String message) {
            displayError(message);
        }
    }
}
