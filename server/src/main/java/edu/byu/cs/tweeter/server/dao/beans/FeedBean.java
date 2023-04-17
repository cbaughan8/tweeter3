package edu.byu.cs.tweeter.server.dao.beans;


import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FeedBean {

    String receiver_alias;
    long timestamp;

    String poster_alias;
    String post;
    List<String> mentions;
    List<String> urls;

    public FeedBean() {
    }

    public FeedBean(String receiver_alias, long timestamp, String poster_alias, String post, List<String> mentions, List<String> urls) {
        this.receiver_alias = receiver_alias;
        this.timestamp = timestamp;
        this.poster_alias = poster_alias;
        this.post = post;
        this.mentions = mentions;
        this.urls = urls;
    }

    @DynamoDbPartitionKey
    public String getReceiver_alias() {
        return receiver_alias;
    }

    public void setReceiver_alias(String receiver_alias) {
        this.receiver_alias = receiver_alias;
    }

    public String getPoster_alias() {
        return poster_alias;
    }

    public void setPoster_alias(String poster_alias) {
        this.poster_alias = poster_alias;
    }

    @DynamoDbSortKey
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "poster_alias='" + poster_alias + '\'' +
                ", receiver_alias='" + receiver_alias + '\'' +
                ", timestamp=" + timestamp +
                ", post='" + post + '\'' +
                ", mentions=" + mentions +
                ", urls=" + urls +
                '}';
    }
}
