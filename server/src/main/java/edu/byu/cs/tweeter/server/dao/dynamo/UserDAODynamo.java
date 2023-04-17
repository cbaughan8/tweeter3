package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.server.dao.beans.UserBean;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;


public class UserDAODynamo extends DynamoDAO implements UserDAO {
    private static final String TableName = "users";
    private static final DynamoDbTable<UserBean> table = enhancedClient.table(TableName, TableSchema.fromBean(UserBean.class));

    public void create(UserBean userBean) {
        try {
            table.putItem(userBean);
        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
        }
    }

    public UserBean get(String alias) {
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        return table.getItem(key);
    }
}
