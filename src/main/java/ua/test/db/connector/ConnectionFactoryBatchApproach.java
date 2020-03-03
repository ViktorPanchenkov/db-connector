package ua.test.db.connector;

import ua.test.db.entity.InsertMetaDataEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Реализация batch подхода.
 * для записи данных в DB.
 */
public interface ConnectionFactoryBatchApproach {

    /**
     * Execute Select from Db.
     * Not closes connection
     *
     * @param sql SQL statement
     * @throws SQLException when can't execute query
     */
    default List<Map<String, Object>> executeSelect(final Connection connection, final String sql) throws SQLException {

        final List<Map<String, Object>> sqlResult = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet result = statement.executeQuery()) {
                ResultSetMetaData metaData = result.getMetaData();
                while (result.next()) {
                    final Map<String, Object> map = new HashMap<>();
                    for (int index = 1; index <= metaData.getColumnCount(); index++) {
                        map.put(metaData.getColumnName(index), result.getObject(index));
                    }
                    sqlResult.add(map);
                }
            }

        }

        return sqlResult;
    }

    /**
     * Execute sql statement.
     *
     * @param sql SQL statement
     */
    default void executeStmt(final Connection connection, final String sql) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        }
    }

    /**
     * Execute Insert to Db.
     *
     * @param sql       SQL statement
     * @param batchSize size of batch to execute
     * @param data      list with data to be inserted
     */
    default <T> void executeBatchInsert(final Connection connection, final String sql, final int batchSize,
                                        final Collection<T> data)
            throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            AtomicInteger count = new AtomicInteger();
            data.forEach(entity -> {
                String[] classData = ((InsertMetaDataEntity) entity).getClassData();
                setBatchFields(batchSize, statement, classData, count);
            });
            // Insert remaining records
            statement.executeBatch();
        }
    }


    /**
     * Execute Delete from Db.
     * Not closes connection
     *
     * @param sql       SQL statement
     * @param batchSize size of batch to execute
     * @param data      set wit data(issue key) to be deleted
     */
    default <T> void executeBatchDelete(final Connection connection,
                                        final String sql,
                                        final int batchSize, final Set<T> data)
            throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            AtomicInteger count = new AtomicInteger();
            data.forEach(key -> {

                try {
                    statement.setObject(1, key);
                    statement.addBatch();
                    if (count.incrementAndGet() % batchSize == 0) {
                        statement.executeBatch();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            // Insert remaining records
            statement.executeBatch();
        }
    }

    /**
     * Execute Delete from Db.
     * Not closes connection
     *
     * @param sql       SQL statement
     * @param batchSize size of batch to execute
     * @param data      collection with fields to determine record that to be deleted
     */
    default <T> void executeBatchDelete(final Connection connection,
                                        final String sql,
                                        final int batchSize, final List<List<T>> data)
            throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            AtomicInteger count = new AtomicInteger();
            data.forEach(entity -> {

                Object[] fieldData = entity.toArray();
                setBatchFields(batchSize, statement, fieldData, count);
            });
            // Insert remaining records
            statement.executeBatch();
        }
    }

    /**
     * Filling batch statement.
     *
     * @param statement PreparedStatement
     * @param batchSize size of batch to execute
     * @param fieldData collection with fields to determine record that to be deleted
     * @param increment define when execute batch
     */
    default void setBatchFields(int batchSize,
                                PreparedStatement statement, Object[] fieldData, AtomicInteger increment) {
        try {
            for (int i = 0; i < fieldData.length; i++) statement.setObject(i + 1, fieldData[i]);
            statement.addBatch();
            if (increment.incrementAndGet() % batchSize == 0) statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
