package edu.byu.cs.tweeter.server.dao.beans;


import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class StoryBean {
    String sender_alias;
    long timestamp;
    String post;
    List<String> mentions;
    List<String> urls;

    public StoryBean() {
    }

    public StoryBean(String sender_alias, long timestamp, String post, List<String> mentions, List<String> urls) {
        this.sender_alias = sender_alias;
        this.timestamp = timestamp;
        this.post = post;
        this.mentions = mentions;
        this.urls = urls;
    }



    @DynamoDbPartitionKey
    public String getSender_alias() {
        return sender_alias;
    }

    public void setSender_alias(String sender_alias) {
        this.sender_alias = sender_alias;
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
        return "Story{" +
                "sender_alias='" + sender_alias + '\'' +
                ", timestamp=" + timestamp +
                ", post='" + post + '\'' +
                ", mentions=" + mentions +
                ", urls=" + urls +
                '}';
    }
}
