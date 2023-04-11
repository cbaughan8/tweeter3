package edu.byu.cs.tweeter.model.net.response;

public abstract class CountResponse extends Response {

    int count;

    public CountResponse(String message) {
        super(false, message);
    }

    public CountResponse(int count) {
        super(true, null);
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
