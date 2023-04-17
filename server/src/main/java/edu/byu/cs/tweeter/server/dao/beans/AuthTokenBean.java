package edu.byu.cs.tweeter.server.dao.beans;


import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class AuthTokenBean {
    String token;
    long timestamp;
    String alias;

    public AuthTokenBean() {
    }

    public AuthTokenBean(String token, long timestamp, String alias) {
        this.token = token;
        this.timestamp = timestamp;
        this.alias = alias;
    }

    @DynamoDbPartitionKey
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "AuthTokenBean{" +
                "token='" + token + '\'' +
                ", timestamp=" + timestamp +
                ", alias='" + alias + '\'' +
                '}';
    }
}
