package edu.byu.cs.tweeter.server.dao.beans;


import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FeedBean {

    String poster_alias;
    String follower_alias;
    long timestamp;
    String post;
    List<String> mentions;
    List<String> urls;

    @DynamoDbPartitionKey
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
                ", follower_alias='" + follower_alias + '\'' +
                ", timestamp=" + timestamp +
                ", post='" + post + '\'' +
                ", mentions=" + mentions +
                ", urls=" + urls +
                '}';
    }
}
