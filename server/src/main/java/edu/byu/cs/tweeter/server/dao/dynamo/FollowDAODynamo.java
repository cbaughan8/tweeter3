package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.beans.DataPage;
import edu.byu.cs.tweeter.server.dao.beans.FollowsBean;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

public class FollowDAODynamo extends DynamoDAO implements FollowDAO {

    private static final String TableName = "follows";
    public static final String IndexName = "follows_index";

    private boolean hasMorePages;

    private static final String followerHandle = "follower_handle";
    private static final String followeeHandle = "followee_handle";

    private static final String followerName = "follower_name";
    private static final String followeeName = "followee_name";

    private static final DynamoDbTable<FollowsBean> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowsBean.class));
    private static final DynamoDbIndex<FollowsBean> index = enhancedClient.table(TableName, TableSchema.fromBean(FollowsBean.class)).index(IndexName);

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    public void putItemInTable(String followersHandleVal,
                                      String followerNameVal,
                                      String followeesHandleVal,
                                      String followeeNameVal) {

        HashMap<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put(followerHandle, AttributeValue.builder().s(followersHandleVal).build());
        itemValues.put(followeeHandle, AttributeValue.builder().s(followeesHandleVal).build());
        itemValues.put(followerName, AttributeValue.builder().s(followerNameVal).build());
        itemValues.put(followeeName, AttributeValue.builder().s(followeeNameVal).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TableName)
                .item(itemValues)
                .build();

        try {
            PutItemResponse response = dynamoDbClient.putItem(request);
            System.out.println(TableName +" was successfully updated. The request id is "+response.responseMetadata().requestId());

        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The Amazon DynamoDB table \"%s\" can't be found.\n", TableName);
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public void deleteDynamoDBItem(String followerHandleVal, String followeeHandleVal) {
        HashMap<String,AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put(followerHandle, AttributeValue.builder()
                .s(followerHandleVal)
                .build());
        keyToGet.put(followeeHandle, AttributeValue.builder()
                .s(followeeHandleVal)
                .build());

        DeleteItemRequest deleteReq = DeleteItemRequest.builder()
                .tableName(TableName)
                .key(keyToGet)
                .build();

        try {
            dynamoDbClient.deleteItem(deleteReq);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void updateTableItem(
            String followerHandleVal,
            String followeeHandleVal,
            String followerNameVal,
            String followeeNameVal){

        HashMap<String,AttributeValue> itemKey = new HashMap<>();
        itemKey.put(followerHandle, AttributeValue.builder()
                .s(followerHandleVal)
                .build());
        itemKey.put(followeeHandle, AttributeValue.builder()
                .s(followeeHandleVal)
                .build());

        HashMap<String, AttributeValueUpdate> updatedValues = new HashMap<>();
        updatedValues.put(followerName, AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(followerNameVal).build())
                .action(AttributeAction.PUT)
                .build());
        updatedValues.put(followeeName, AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(followeeNameVal).build())
                .action(AttributeAction.PUT)
                .build());

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(TableName)
                .key(itemKey)
                .attributeUpdates(updatedValues)
                .build();

        try {
            dynamoDbClient.updateItem(request);
        } catch (ResourceNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("The Amazon DynamoDB table was updated!");
    }

    public DataPage<FollowsBean> getPageOfFollowees(String targetUserAlias, int pageSize, String lastUserAlias ) {
        DynamoDbTable<FollowsBean> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowsBean.class));
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(isNonEmptyString(lastUserAlias)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(followerHandle, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(followeeHandle, AttributeValue.builder().s(lastUserAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FollowsBean> result = new DataPage<FollowsBean>();

        PageIterable<FollowsBean> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowsBean> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });

        return result;
    }

    public DataPage<FollowsBean> getPageOfFollowers(String targetUserAlias, int pageSize, String lastUserAlias ) {
        DynamoDbIndex<FollowsBean> index = enhancedClient.table(TableName, TableSchema.fromBean(FollowsBean.class)).index(IndexName);
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(isNonEmptyString(lastUserAlias)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(followeeHandle, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(followerHandle, AttributeValue.builder().s(lastUserAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FollowsBean> result = new DataPage<FollowsBean>();

        SdkIterable<Page<FollowsBean>> sdkIterable = index.query(request);
        PageIterable<FollowsBean> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowsBean> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });

        return result;
    }

    public void create(FollowsBean bean) {
        try {
            table.putItem(bean);
        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(FollowsBean bean) {
        Key key = Key.builder()
                .partitionValue(bean.getFollower_handle())
                .sortValue(bean.getFollowee_handle())
                .build();
        table.deleteItem(key);
    }

    @Override
    public FollowsBean getFollower(String followerAlias, String followeeAlias) {
        Key key = Key.builder()
                .partitionValue(followerAlias)
                .sortValue(followeeAlias)
                .build();
        return table.getItem(key);
    }

    @Override
    public List<FollowsBean> getFollowers(FollowersRequest request) {
        Key key = Key.builder()
                .partitionValue(request.getFolloweeAlias())
                .build();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false);
        QueryEnhancedRequest enhancedRequest = requestBuilder.build();
        List<FollowsBean> followers = new ArrayList<>();
        SdkIterable<Page<FollowsBean>> iterable = index.query(enhancedRequest);
        PageIterable<FollowsBean> pages = PageIterable.create(iterable);
        pages.stream()
                .limit(1)
                .forEach(page -> {
                    hasMorePages = page.lastEvaluatedKey() != null;
                    followers.addAll(page.items());
                });
        return followers;
    }

    @Override
    public List<FollowsBean> getFollowees(FollowingRequest request) {
        DynamoDbTable<FollowsBean> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowsBean.class));
        Key key = Key.builder()
                .partitionValue(request.getFollowerAlias())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(request.getLimit());

        if (isNotEmptyString(request.getLastFolloweeAlias())) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("follower_handle", AttributeValue.builder().s(request.getFollowerAlias()).build());
            startKey.put("followee_handle", AttributeValue.builder().s(request.getLastFolloweeAlias()).build());
            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest enhancedRequest = requestBuilder.build();

        List<FollowsBean> followers = new ArrayList<>();
        FollowingResponse response = new FollowingResponse(new ArrayList<>(), false);
        PageIterable<FollowsBean> pages = table.query(enhancedRequest);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowsBean> page) -> {
                    hasMorePages = page.lastEvaluatedKey() != null;
                    followers.addAll(page.items());
                });
        return followers;
    }

    @Override
    public boolean hasMorePages() {
        return hasMorePages;
    }

    private static boolean isNotEmptyString(String value) {
        return (value != null && value.length() > 0);
    }



}
