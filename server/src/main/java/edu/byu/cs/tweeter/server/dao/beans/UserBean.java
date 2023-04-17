package edu.byu.cs.tweeter.server.dao.beans;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class UserBean {

    String alias;
    String first_name;
    String last_name;
    String image_url;
    int password_hash;
    int followee_count;
    int follower_count;

    public UserBean() {
    }

    public UserBean(String alias, String first_name, String last_name, String image_url, int password_hash, int followee_count, int follower_count) {
        this.alias = alias;
        this.first_name = first_name;
        this.last_name = last_name;
        this.image_url = image_url;
        this.password_hash = password_hash;
        this.followee_count = followee_count;
        this.follower_count = follower_count;
    }

    @DynamoDbPartitionKey
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(int password_hash) {
        this.password_hash = password_hash;
    }

    public int getFollowee_count() {
        return followee_count;
    }

    public void setFollowee_count(int followee_count) {
        this.followee_count = followee_count;
    }

    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    @Override
    public String toString() {
        return "User{" +
                "alias='" + alias + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", image_url='" + image_url + '\'' +
                ", password_hash=" + password_hash +
                ", followee_count=" + followee_count +
                ", follower_count=" + follower_count +
                '}';
    }
}
