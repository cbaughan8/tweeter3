package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.beans.StoryBean;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class StoryDAODynamo extends DynamoDAO implements StoryDAO {

    private static final String TableName = "storys";
    private boolean hasMorePages;

    private static final DynamoDbTable<StoryBean> table = enhancedClient.table(TableName, TableSchema.fromBean(StoryBean.class));


    @Override
    public List<StoryBean> getStatuses(StoryRequest request) {
        Key key = Key.builder()
                .partitionValue(request.getTargetUser().getAlias())
                .build();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(request.getLimit())
                .scanIndexForward(false);
        System.out.println(request);
        System.out.println(request.getLastStatus());
        if (request.getLastStatus() != null) {
            if (isNotEmptyString(String.valueOf(request.getLastStatus().getTimestamp()))) {
                Map<String, AttributeValue> startKey = new HashMap<>();
                startKey.put("sender_alias", AttributeValue.builder().s(request.getTargetUser().getAlias()).build());
                startKey.put("timestamp", AttributeValue.builder().s(String.valueOf(request.getLastStatus().getTimestamp())).build());
                requestBuilder.exclusiveStartKey(startKey);
            }
        }
        QueryEnhancedRequest enhancedRequest = requestBuilder.build();

        List<StoryBean> statuses = new ArrayList<>();
        StoryResponse response = new StoryResponse(new ArrayList<>(), false);
        PageIterable<StoryBean> pages = table.query(enhancedRequest);
        pages.stream()
                .limit(1)
                .forEach((Page<StoryBean> page) -> {
                    hasMorePages = page.lastEvaluatedKey() != null;
                    statuses.addAll(page.items());
                });
        return statuses;
    }

    @Override
    public void create(StoryBean storyBean) {
        try {
            table.putItem(storyBean);
        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
        }
    }

    public StoryResponse getStory(StoryRequest request) {
        System.out.println("lastStatus" + request.lastStatus);
        Pair<List<Status>, Boolean> dummyData = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
        return new StoryResponse(dummyData.getFirst(), dummyData.getSecond());
    }

    private static boolean isNotEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    private int getStatusesStartingIndex(String lastStatusAlias, List<Status> allStatuses) {

        int statusesIndex = 0;

        if(lastStatusAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatusAlias.equals(allStatuses.get(i).getUser().getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusesIndex = i + 1;
                    break;
                }
            }
        }

        return statusesIndex;
    }

    @Override
    public boolean hasMorePages() {
        return hasMorePages;
    }

    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
