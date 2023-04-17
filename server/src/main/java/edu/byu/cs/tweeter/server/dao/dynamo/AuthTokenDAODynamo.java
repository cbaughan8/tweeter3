package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.server.dao.beans.AuthTokenBean;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class AuthTokenDAODynamo extends DynamoDAO implements AuthTokenDAO {
    private static final String TableName = "authTokens";
    private static final DynamoDbTable<AuthTokenBean> table = enhancedClient.table(TableName, TableSchema.fromBean(AuthTokenBean.class));


    @Override
    public void create(AuthTokenBean authTokenBean) {
        try {
            table.putItem(authTokenBean);
        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public AuthTokenBean get(String token) {
        Key key = Key.builder()
                .partitionValue(token)
                .build();
        return table.getItem(key);
    }

    @Override
    public void update(AuthTokenBean authTokenBean) {
        try {
            table.updateItem(authTokenBean);
        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(String token) {
        Key key = Key.builder()
                .partitionValue(token)
                .build();
        table.deleteItem(key);
    }

}
