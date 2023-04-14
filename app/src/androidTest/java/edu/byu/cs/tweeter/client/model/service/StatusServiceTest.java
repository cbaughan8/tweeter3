package edu.byu.cs.tweeter.client.model.service;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.PagesObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

class StatusServiceTest {

    private CountDownLatch countDownLatch;
    private User mockUser;

    @Test
    void loadMoreItemsStory() throws InterruptedException {
        mockUser = new User("Carson", "Baughan", "@cb", "image");
        StoryObserver observer = Mockito.spy(new StoryObserver());
        StatusService statusService = Mockito.spy(new StatusService());

        int PAGE_SIZE = 10;
        Status lastStatus = null;
        resetCountDownLatch();

        statusService.loadMoreItemsStory(mockUser ,PAGE_SIZE, lastStatus, observer);
        awaitCountDownLatch();
        List<Status> expectedStory = FakeData.getInstance().getFakeStatuses().subList(0, 10);
        Mockito.verify(observer, times(1)).handleSuccess(anyList(), anyBoolean());
        Assertions.assertTrue(observer.isSuccess());
        Assertions.assertNull(observer.getMessage());
        Assertions.assertEquals(expectedStory, observer.getStory());
        Assertions.assertTrue(observer.getHasMorePages());
        Assertions.assertNull(observer.getException());

    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    private class StoryObserver implements PagesObserver<Status> {

        private boolean success;
        private String message;
        private List<Status> story;
        private boolean hasMorePages;
        private Exception exception;

        @Override
        public void handleSuccess(List<Status> data, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.story = data;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.success = false;
            this.message = message;
            this.story = null;
            this.hasMorePages = false;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleException(String message) {
            this.success = false;
            this.message = message;
            this.story = null;
            this.hasMorePages = false;
            this.exception = null;

            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public List<Status> getStory() {
            return story;
        }

        public boolean getHasMorePages() {
            return hasMorePages;
        }

        public Exception getException() {
            return exception;
        }
    }
}