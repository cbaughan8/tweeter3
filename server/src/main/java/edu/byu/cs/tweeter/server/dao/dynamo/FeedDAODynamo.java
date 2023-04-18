package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.dao.beans.FeedBean;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedDAO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class FeedDAODynamo extends DynamoDAO implements FeedDAO {

    private static final String TableName = "feeds";
    private boolean hasMorePages;
    private static final DynamoDbTable<FeedBean> table = enhancedClient.table(TableName, TableSchema.fromBean(FeedBean.class));

    public List<FeedBean> getStatuses(FeedRequest request) {
        Key key = Key.builder()
                .partitionValue(request.getTargetUser().getAlias())
                .build();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(request.getLimit()).scanIndexForward(false);
        if (isNotEmptyString(String.valueOf(request.getLastStatus().getTimestamp()))) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("reciever_alias", AttributeValue.builder().s(request.getTargetUser().getAlias()).build());
            startKey.put("timestamp", AttributeValue.builder().n(String.valueOf(request.getLastStatus().getTimestamp())).build());
            requestBuilder.exclusiveStartKey(startKey);
        }
        QueryEnhancedRequest enhancedRequest = requestBuilder.build();

        List<FeedBean> statuses = new ArrayList<>();
        FeedResponse response = new FeedResponse(new ArrayList<>(), false);
        PageIterable<FeedBean> pages = table.query(enhancedRequest);
        pages.stream()
                .limit(1)
                .forEach((Page<FeedBean> page) -> {
                    hasMorePages = page.lastEvaluatedKey() != null;
                    statuses.addAll(page.items());
                });
        return statuses;
    }


    public boolean hasMorePages() {
        return hasMorePages;
    }
    private static boolean isNotEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

}
