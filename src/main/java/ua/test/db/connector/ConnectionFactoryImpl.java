package ua.test.db.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.test.db.enums.DbName;
import ua.test.db.utils.PropertyLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для подключения к бд.
 */
class ConnectionFactoryImpl implements ConnectionFactoryBatchApproach {

    static final Logger LOG = LoggerFactory.getLogger(ConnectionFactoryImpl.class);
    static final String CONNECTION_IS_CLOSED = "Connection is closed!";

    private Connection connection;
    private String dbDriverName;
    private String dbConnectionString;
    private String user;
    private String password;

    /**
     * Создает подключение к указанной БД.
     */
    ConnectionFactoryImpl(DbName dbName) {
        loadProperty(dbName);
        this.connection = initConnection();

    }

    /**
     * Возвращает ссылку на подключение.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Проверяет подключение.
     *
     * @throws IllegalArgumentException если подключение закрыто
     */
    public void checkConnection() throws SQLException {
        if (this.connection.isClosed()) {
            throw new IllegalArgumentException(CONNECTION_IS_CLOSED);
        }
    }

    /**
     * Закрывает подключение.
     */
    public void closeConnection() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
            this.connection = null;
        }
    }

    private void loadProperty(final DbName dbName) {
        final PropertyLoader propertyLoader;

        if (dbName.equals(DbName.TEST_RAIL)) {
            propertyLoader = new PropertyLoader(DbName.TEST_RAIL);
        } else {
            propertyLoader = new PropertyLoader(DbName.TST_METRICS);
        }

        this.dbDriverName = propertyLoader.load("deriver").toString();
        this.dbConnectionString = propertyLoader.load("connection_string").toString();
        this.user = propertyLoader.load("user").toString();
        this.password = propertyLoader.load("pass").toString();

    }

    private Connection initConnection() {
        try {
            Class.forName(dbDriverName);
            return DriverManager.getConnection(dbConnectionString, this.user, this.password);
        } catch (SQLException e) {
            LOG.error("DB connection error!", e);
            LOG.error(e.getMessage());
        } catch (ClassNotFoundException e) {
            LOG.error("Error during JDBC driver initialization!", e);
            LOG.error(e.getMessage());
        }

        return null;
    }

}
